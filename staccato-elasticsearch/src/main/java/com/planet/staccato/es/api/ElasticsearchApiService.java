package com.planet.staccato.es.api;

import com.planet.staccato.config.LinksConfigProps;
import com.planet.staccato.dto.api.SearchRequest;
import com.planet.staccato.es.IndexAliasLookup;
import com.planet.staccato.es.QueryBuilderHelper;
import com.planet.staccato.es.repository.ElasticsearchRepository;
import com.planet.staccato.filters.ItemsFilterProcessor;
import com.planet.staccato.model.Item;
import com.planet.staccato.model.ItemCollection;
import com.planet.staccato.service.ApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        LINK_BASE += "/api"; // generate the base string for links
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
        Mono<Item> itemMono = repository.searchById(indices, id);
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
        Set<String> indices = getIndices(searchRequest);
        BoolQueryBuilder boolQueryBuilder = QueryBuilderHelper.buildQuery(searchRequest);

        Flux<Item> itemFlux = repository.searchItemFlux(indices, boolQueryBuilder, searchRequest);
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
        Set<String> indices = getIndices(searchRequest);
        BoolQueryBuilder boolQueryBuilder = QueryBuilderHelper.buildQuery(searchRequest);

        Mono<ItemCollection> itemCollection = repository.searchItemCollection(indices, boolQueryBuilder, searchRequest);
        return processor.searchItemCollectionMono(itemCollection, searchRequest);
    }

    private Set<String> getIndices(SearchRequest searchRequest) {
        String[] collections = searchRequest.getCollections();
        Set<String> indices = new HashSet<>();
        if (null != collections) {
            for (String collection : collections) {
                indices.add(aliasLookup.getReadAlias(collection));
            }
        } else {
            indices.addAll(aliasLookup.getReadAliases());
        }
        return indices;
    }

}

