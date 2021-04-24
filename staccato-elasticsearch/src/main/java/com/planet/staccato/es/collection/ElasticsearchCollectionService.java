package com.planet.staccato.es.collection;

import com.planet.staccato.collection.CatalogType;
import com.planet.staccato.collection.CollectionMetadata;
import com.planet.staccato.config.LinksConfigProps;
import com.planet.staccato.es.stats.ElasticStatsService;
import com.planet.staccato.exception.StaccatoRuntimeException;
import com.planet.staccato.model.Collections;
import com.planet.staccato.model.Link;
import com.planet.staccato.queryables.Queryables;
import com.planet.staccato.service.CollectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service class implementing logic required to support the collection API.
 *
 * @author joshfix
 * Created on 11/29/17
 */
@Slf4j
@Service
public class ElasticsearchCollectionService implements CollectionService {

    private final ElasticStatsService aggregationService;
    private Map<String, CollectionMetadata> collectionMetadataMap = new HashMap<>();
    private List<Link> collectionsLinks = new ArrayList<>();
    private Map<String, Queryables> queryablesMap = new HashMap<>();

    public ElasticsearchCollectionService(
            ElasticStatsService aggregationService,
            List<CollectionMetadata> collectionMetadataList,
            List<Queryables> queryables
    ) {
        this.aggregationService = aggregationService;

        initLinks();

        // build map of collection ids to queryables
        queryables.forEach(q -> queryablesMap.put(q.getId(), q));

        // build a map of collection ids to CollectionMetadataAdapter objects so they can easily be retrieved
        collectionMetadataList.forEach(cm -> {
            if (cm.getCatalogType() == CatalogType.COLLECTION) {
                collectionMetadataMap.put(cm.getId(), cm);
            }
        });
    }

    private void initLinks() {
        collectionsLinks.add(new Link()
                .rel("self")
                .type(MediaType.APPLICATION_JSON_VALUE)
                .href(LinksConfigProps.LINK_PREFIX + "/collections"));
    }

    /**
     * Returns all collections.
     *
     * @return Flux of CollectionMetadata objects
     */
    @Override
    public Mono<Collections> getCollectionsMono() {
        return Flux.fromIterable(collectionMetadataMap.values())
                .map(collection -> collection.extent(aggregationService.getExtent(collection.getId(), null)))
                .collectList()
                .map(collectionsList -> new Collections().links(collectionsLinks).collections(collectionsList));
    }

    public Map<String, CollectionMetadata> getCollectionMetadataMap() {
        return collectionMetadataMap;
    }

    /**
     * Calculates the extent for a collection and returns the {@link CollectionMetadata CollectionMetadata} object
     *
     * @param collectionId The ID of the collection
     * @return the {@Link CollectionMetadata CollectionMetadata} object wrapped in a Mono
     */
    @Override
    public Mono<CollectionMetadata> getCollectionMetadata(String collectionId) {
        if (collectionMetadataMap.containsKey(collectionId)) {
            CollectionMetadata metadata = collectionMetadataMap.get(collectionId);
            return Mono.just(aggregationService.getExtent(collectionId, null))
                    .map(extent -> metadata.extent(extent));
        }
        throw new StaccatoRuntimeException("No collection found with id '" + collectionId + "'.", 404);
    }

    @Override
    public Mono<Queryables> getQueryables(String collectionId) {
        return Mono.just(queryablesMap.get(collectionId));
    }
}
