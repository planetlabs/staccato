package com.planet.staccato.es.api;

import com.planet.staccato.config.LinksConfigProps;
import com.planet.staccato.dto.SearchRequest;
import com.planet.staccato.es.IndexAliasLookup;
import com.planet.staccato.es.QueryBuilderHelper;
import com.planet.staccato.SearchRequestUtils;
import com.planet.staccato.es.config.ElasticsearchConfigProps;
import com.planet.staccato.es.repository.ElasticsearchRepository;
import com.planet.staccato.filter.ItemsFilterProcessor;
import com.planet.staccato.model.Item;
import com.planet.staccato.model.ItemCollection;
import com.planet.staccato.model.Link;
import com.planet.staccato.service.ApiService;
import joptsimple.internal.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Service class implementing logic required to support the STAC api API.
 *
 * @author joshfix
 * Created on 10/17/18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticsearchApiService implements ApiService {

    private final ElasticsearchConfigProps configProps;
    private final ElasticsearchRepository repository;
    private final ItemsFilterProcessor processor;
    private final IndexAliasLookup aliasLookup;
    private final LinksConfigProps linksConfigProperties;
    private static String LINK_BASE;

    /**
     * Generates the base string for links
     */
    @PostConstruct
    public void init() {
        LinksConfigProps.Self self = linksConfigProperties.getSelf();
        LINK_BASE = self.getScheme() + "://" + self.getHost();
        if (self.getPort() != 80) {
            LINK_BASE += ":" + self.getPort();
        }
        LINK_BASE += "/stac/api"; // generate the base string for links
    }

    /**
     * Fetches a single item.
     *
     * @param id           The ID of the item to retrieve
     * @param collectionId The collection of the item to retrieve
     * @return The item wrapped in a mono
     */
    @Override
    public Mono<Item> getItem(String id, String collectionId) {
        List<String> indices = (collectionId == null) ? aliasLookup.getReadAliases() : Arrays.asList(collectionId);
        Mono<Item> itemMono = repository.searchById(indices, configProps.getEs().getType(), id);
        return processor.searchItemMono(itemMono, new SearchRequest());
    }

    /**
     * Fetches multiple items that match the provided api request.
     *
     * @param searchRequest The {@link SearchRequest SearchRequest} object containing all api parameters
     * @return A flux of items
     */
    @Override
    public Flux<Item> getItemsFlux(SearchRequest searchRequest) {
        String[] propertyname = searchRequest.getPropertyname();
        Set<String> includeFields = (null != propertyname && propertyname.length > 0) ?
                new HashSet(Arrays.asList(propertyname)) : null;

        String[] collections = searchRequest.getCollections();
        Set<String> indices = new HashSet<>();
        if (null != collections) {
            for (String collection : collections) {
                indices.add(aliasLookup.getReadAlias(collection));
            }
        } else {
            indices.addAll(aliasLookup.getReadAliases());
        }

        int limit = QueryBuilderHelper.getLimit(searchRequest.getLimit());
        BoolQueryBuilder boolQueryBuilder = QueryBuilderHelper.buildQuery(searchRequest);

        Flux<Item> itemFlux = repository.searchItemFlux(indices, configProps.getEs().getType(),
                boolQueryBuilder, limit, searchRequest.getPage(), includeFields);
        return processor.searchItemFlux(
                itemFlux, searchRequest);
    }

    /**
     * Fetches multiple items that match the provided api request.
     *
     * @param searchRequest The {@link SearchRequest SearchRequest} object containing all api parameters
     * @return A flux of items
     */
    @Override
    public Mono<ItemCollection> getItems(SearchRequest searchRequest) {
        String[] propertyname = searchRequest.getPropertyname();
        Set<String> includeFields = (null != propertyname && propertyname.length > 0) ?
                new HashSet(Arrays.asList(propertyname)) : null;

        String[] collections = searchRequest.getCollections();
        Set<String> indices = new HashSet<>();
        if (null != collections) {
            for (String collection : collections) {
                indices.add(aliasLookup.getReadAlias(collection));
            }
        } else {
            indices.addAll(aliasLookup.getReadAliases());
        }

        int limit = QueryBuilderHelper.getLimit(searchRequest.getLimit());
        BoolQueryBuilder boolQueryBuilder = QueryBuilderHelper.buildQuery(searchRequest);

        Mono<ItemCollection> itemCollection = repository.searchItemCollection(indices, configProps.getEs().getType(),
                boolQueryBuilder, limit, searchRequest.getPage(), includeFields, searchRequest);
        return processor.searchItemCollectionMono(itemCollection, searchRequest);
        /*
        Integer page = searchRequest.getPage();
        final int nextPage = (null == page) ? 1 : page + 1;
        int finalLimit = QueryBuilderHelper.getLimit(searchRequest.getLimit());

        return getItemsFlux(searchRequest)
                .collectList()
                // take the api list build an item collection from it
                .map(itemList -> {
                    ItemCollection itemCollection = new ItemCollection()
                            .features(itemList)
                            .type(ItemCollection.TypeEnum.FEATURECOLLECTION);

                    // rebuild the original request link
                    double[] bbox = searchRequest.getBbox();
                    String link = LINK_BASE + "?limit=" + finalLimit;
                    if (null != bbox && bbox.length == 4) {

                        link += bbox == null ? Strings.EMPTY : "&bbox=" + bbox[0] + "," + bbox[1] + "," + bbox[2] + "," + bbox[3];
                    }

                    link += searchRequest.getTime() == null ? Strings.EMPTY : "&time=" + searchRequest.getTime();
                    link += searchRequest.getQuery() == null ? Strings.EMPTY : "&query=" + searchRequest.getQuery();
                    link += searchRequest.getIds() == null ? Strings.EMPTY : "&ids=" + String.join(",", searchRequest.getIds());
                    link += searchRequest.getCollections() == null ? Strings.EMPTY : "&collections=" + String.join(",", searchRequest.getCollections());
                    link += searchRequest.getPropertyname() == null ? Strings.EMPTY : "&propertyname=" + String.join(",", searchRequest.getPropertyname());

                    itemCollection.addLink(new Link()
                            .href(link)
                            .rel("self"));

                    // if the number of api in the collection is less than or equal to the limit, do not provide a page page token
                    if (itemCollection.getFeatures().size() >= finalLimit) {
                        itemCollection.addLink(new Link()
                                .href(link + "&page=" + nextPage)
                                .rel("page"));
                    }


                    return itemCollection;
                });
                */
    }

}

