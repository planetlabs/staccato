package com.planet.staccato.es.collection;

import com.planet.staccato.collection.CatalogType;
import com.planet.staccato.collection.CollectionMetadata;
import com.planet.staccato.es.stats.ElasticStatsService;
import com.planet.staccato.service.CollectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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
    private Map<String, CollectionMetadata> collections = new HashMap<>();

    public ElasticsearchCollectionService(ElasticStatsService aggregationService,
                                          List<CollectionMetadata> collectionMetadataList) {
        this.aggregationService = aggregationService;

        // build a map of collection ids to CollectionMetadataAdapter objects so they can easily be retrieved
        collectionMetadataList.forEach(cm -> {
            if (cm.getCatalogType() == CatalogType.COLLECTION) {
                collections.put(cm.getId(), cm);
            }
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
