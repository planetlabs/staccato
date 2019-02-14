package com.boundlessgeo.staccato.es.collection;

import com.boundlessgeo.staccato.collection.CollectionMetadata;
import com.boundlessgeo.staccato.config.LinksConfigProps;
import com.boundlessgeo.staccato.dto.SearchRequest;
import com.boundlessgeo.staccato.es.stats.ElasticStatsService;
import com.boundlessgeo.staccato.es.repository.ElasticsearchRepository;
import com.boundlessgeo.staccato.filter.ItemsFilterProcessor;
import com.boundlessgeo.staccato.model.Item;
import com.boundlessgeo.staccato.model.ItemCollection;
import com.boundlessgeo.staccato.model.Link;
import com.boundlessgeo.staccato.modelx.ScrollWrapper;
import com.boundlessgeo.staccato.service.CollectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.boundlessgeo.staccato.es.SearchRequestUtils.generateSearchRequest;

/**
 * Service class implementing logic required to support the collection API.
 *
 * @author joshfix
 * Created on 11/29/17
 */
@Slf4j
@Service
public class ElasticsearchCollectionService implements CollectionService {

    private final ElasticsearchRepository repository;
    private final ItemsFilterProcessor processor;
    private final ElasticStatsService aggregationService;
    private Map<String, CollectionMetadata> collections = new HashMap<>();
    private static String LINK_BASE;

    public ElasticsearchCollectionService(ElasticsearchRepository repository, ItemsFilterProcessor processor,
                                          ElasticStatsService aggregationService, LinksConfigProps linksConfigProperties,
                                          List<CollectionMetadata> collectionMetadataList) {
        this.repository = repository;
        this.processor = processor;
        this.aggregationService = aggregationService;

        // generate the base string for links
        LinksConfigProps.Self self = linksConfigProperties.getSelf();
        LINK_BASE = self.getScheme() + "://" + self.getHost();
        if (self.getPort() != 80) {
            LINK_BASE += ":" + self.getPort();
        }
        LINK_BASE += "/collections/";

        // build a map of collection ids to CollectionMetadataAdapter objects so they can easily be retrieved
        collectionMetadataList.forEach(cm -> collections.put(cm.getId(), cm));
    }

    /**
     * Retrieves the first page of a set of paginated item results
     *
     * @param collectionId The ID of the collection to query
     * @param limit The maximum number of items that should be returned in the query
     * @return The collection of items wrapped in a Mono
     */
    @Override
    public Mono<ItemCollection> getItemsInitialScroll(String collectionId, Integer limit) {
        ScrollWrapper wrapper = repository.initialScroll(collectionId, limit);
        return processor.searchItemFlux(
                wrapper.getItemFlux(), generateSearchRequest(null, null, null, limit, null, null))
                .collectList()
                .map(itemList -> {
                    ItemCollection itemCollection = new ItemCollection()
                            .features(itemList)
                            .type(ItemCollection.TypeEnum.FEATURECOLLECTION);
                    if (!itemList.isEmpty() && null != wrapper.getScrollId()) {
                        itemCollection.addLink(new Link()
                                .href(LINK_BASE + collectionId + "/items?next=" + wrapper.getScrollId())
                                .rel("next"));
                    }
                    return itemCollection;
                });
    }

    /**
     * Retreives the next page of item results using the Elasticsearch scroll API
     *
     * @param collectionId The ID of the collection to query
     * @param next The page number
     * @return The collection of items wrapped in a Mono
     */
    @Override
    public Mono<ItemCollection> getItemsScroll(String collectionId, String next) {
        Flux<Item> itemFlux = repository.scroll(next);

        SearchRequest sr = generateSearchRequest(null, null, null, 0, next, null);
        return processor.searchItemFlux(itemFlux, sr)
                .collectList()
                // take the api list build an item collection from it
                .map(itemList -> {
                    ItemCollection itemCollection = new ItemCollection()
                            .features(itemList)
                            .type(ItemCollection.TypeEnum.FEATURECOLLECTION);
                    if (!itemList.isEmpty()) {
                        itemCollection.addLink(new Link()
                                .href(LINK_BASE + collectionId + "/items?next=" + next)
                                .rel("next"));
                    }
                    return itemCollection;
                });
    }

    /**
     * Calculates the extent for a collection and returns the {@link CollectionMetadata CollectionMetadata} object
     * @param collectionId The ID of the collection
     * @return the {@Link CollectionMetadata CollectionMetadata} object wrapped in a Mono
     */
    @Override
    public Mono<CollectionMetadata> getCollectionMetadata(String collectionId) {
        if (collections.containsKey(collectionId)) {
            CollectionMetadata metadata = collections.get(collectionId);
            return Mono.just(aggregationService.getExtent(collectionId, null))
                    .map(extent -> metadata.extent(extent));
        }
        return Mono.empty();
    }


}
