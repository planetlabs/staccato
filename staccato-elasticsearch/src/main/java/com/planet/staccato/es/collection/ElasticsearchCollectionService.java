package com.planet.staccato.es.collection;

import com.planet.staccato.collection.CollectionMetadata;
import com.planet.staccato.config.LinksConfigProps;
import com.planet.staccato.dto.SearchRequest;
import com.planet.staccato.es.QueryBuilderHelper;
import com.planet.staccato.es.ScrollWrapper;
import com.planet.staccato.es.repository.ElasticsearchRepository;
import com.planet.staccato.es.stats.ElasticStatsService;
import com.planet.staccato.filter.ItemsFilterProcessor;
import com.planet.staccato.model.Item;
import com.planet.staccato.model.ItemCollection;
import com.planet.staccato.model.Link;
import com.planet.staccato.service.CollectionService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.planet.staccato.SearchRequestUtils.generateSearchRequest;

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
     * @param searchRequest The API search request
     * @return The collection of items wrapped in a Mono
     */
    @Override
    public Mono<ItemCollection> getItemsInitialScroll(SearchRequest searchRequest) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilderHelper.buildQuery(searchRequest);

        if (searchRequest.getCollections() == null || searchRequest.getCollections().length != 1) {
            throw new RuntimeException("Unable to determine collection type.");
        }
        String collectionId = searchRequest.getCollections()[0];
        ScrollWrapper wrapper =
                repository.initialScroll(collectionId, searchRequest.getLimit(), boolQueryBuilder);

        return processor.searchItemFlux(
                wrapper.getItemFlux(), searchRequest)
                .collectList()
                .map(itemList -> {
                    ItemCollection itemCollection = new ItemCollection()
                            .features(itemList)
                            .type(ItemCollection.TypeEnum.FEATURECOLLECTION);
                    if (!itemList.isEmpty() && null != wrapper.getScrollId()) {
                        itemCollection.addLink(new Link()
                                .href(LINK_BASE + collectionId + "/items?page=" + wrapper.getScrollId())
                                .rel("page"));
                    }
                    return itemCollection;
                });
    }

    /**
     * Retreives the page page of item results using the Elasticsearch scroll API
     *
     * @param searchRequest The API search request
     * @return The collection of items wrapped in a Mono
     */
    @Override
    public Mono<ItemCollection> getItemsScroll(SearchRequest searchRequest) {
        Flux<Item> itemFlux = repository.scroll(searchRequest.getPage());

        if (searchRequest.getCollections() == null || searchRequest.getCollections().length != 1) {
            throw new RuntimeException("Unable to determine collection type.");
        }
        String collectionId = searchRequest.getCollections()[0];

        return processor.searchItemFlux(itemFlux, searchRequest)
                .collectList()
                // take the api list build an item collection from it
                .map(itemList -> {
                    ItemCollection itemCollection = new ItemCollection()
                            .features(itemList)
                            .type(ItemCollection.TypeEnum.FEATURECOLLECTION);
                    if (!itemList.isEmpty()) {
                        itemCollection.addLink(new Link()
                                .href(LINK_BASE + collectionId + "/items?page=" + searchRequest.getPage())
                                .rel("page"));
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
