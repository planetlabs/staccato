package com.planet.staccato.service;

import com.planet.staccato.collection.CollectionMetadata;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Defines the API that must be implemented to provide a CollectionService implementation.
 *
 * @author joshfix
 * Created on 10/17/18
 */
public interface CollectionService {

    Flux<CollectionMetadata> getCollections();
    Mono<CollectionMetadata> getCollectionMetadata(String collectionId);

}
