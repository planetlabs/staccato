package com.planet.staccato.es.api;

import com.planet.staccato.collection.CollectionMetadata;
import com.planet.staccato.config.LinksConfigProps;
import com.planet.staccato.config.StacConfigProps;
import com.planet.staccato.dto.api.SearchRequest;
import com.planet.staccato.es.IndexAliasLookup;
import com.planet.staccato.es.QueryBuilderHelper;
import com.planet.staccato.es.collection.ElasticsearchCollectionService;
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

    private Map<String, CollectionMetadata> collectionMetadata;
    private final ElasticsearchRepository repository;
    private final ItemsFilterProcessor processor;
    private final IndexAliasLookup aliasLookup;
    private final LinksConfigProps linksConfigProperties;
    private final ElasticsearchCollectionService collectionService;
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
        collectionMetadata = collectionService.getCollectionMetadataMap();
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
        // this should be handled by the CQL library; for now it's a workaround
        if (!filterCrs(searchRequest)) {
            return processor.searchItemFlux(Flux.empty(), searchRequest);
        }
        Set<String> indices = getIndices(searchRequest);
        BoolQueryBuilder boolQueryBuilder = QueryBuilderHelper.buildQuery(searchRequest);
        Flux<Item> itemFlux = repository.searchItemFlux(indices, boolQueryBuilder, searchRequest);
        return processor.searchItemFlux(itemFlux, searchRequest);
    }

    /**
     * Fetches multiple items that match the provided api request.
     *
     * @param searchRequest The {@link SearchRequest SearchRequest} object containing all api parameters
     * @return A flux of items
     */
    @Override
    public Mono<ItemCollection> getItemCollection(SearchRequest searchRequest) {
        // this should be handled by the CQL library; for now it's a workaround
        if (searchRequest.getCollections() == null && !filterCrs(searchRequest)) {
            return processor.searchItemCollectionMono(Mono.just(new ItemCollection()), searchRequest);
        }
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

    /**
     * If the request defined a filter-crs parameter, loop through the collections and add a collections filter for
     * each collection that contains the specified crs. This should be a function of the CQL filter; this implementation
     * is a workaround until a CQL Elasticsearch library is available.
     *
     * @param searchRequest The search request
     * @return A boolean; true means no crs was defined or there were matching collections; false means a crs was
     * defined but there are no matching collections
     */
    private boolean filterCrs(SearchRequest searchRequest) {
        if (searchRequest.getFilterCrs() == null || searchRequest.getFilterCrs().isBlank()) {
            return true;
        }

        List<String> applicableCollections = new ArrayList<>();
        collectionMetadata.forEach((key, value) -> {
            if (value.getCrs().contains(searchRequest.getFilterCrs())) {
                applicableCollections.add(key);
            }
        });
        if (applicableCollections.isEmpty()) {
            // this will prevent any collection from matching
            return false;
        }
        searchRequest.setCollections(applicableCollections.toArray(new String[]{}));
        return true;
    }
}

