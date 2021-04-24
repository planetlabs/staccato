package com.planet.staccato.service;

import com.planet.staccato.collection.CollectionMetadata;
import com.planet.staccato.model.Collections;
import com.planet.staccato.queryables.Queryables;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Defines the API that must be implemented to provide a CollectionService implementation.
 *
 * @author joshfix
 * Created on 10/17/18
 */
public interface CollectionService {

    Map<String, CollectionMetadata> getCollectionMetadataMap();
    Mono<Collections> getCollectionsMono();
    Mono<CollectionMetadata> getCollectionMetadata(String collectionId);
    Mono<Queryables> getQueryables(String collectionId);

}
