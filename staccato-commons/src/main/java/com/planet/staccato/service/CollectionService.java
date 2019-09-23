package com.planet.staccato.service;

import com.planet.staccato.collection.CollectionMetadata;
import com.planet.staccato.model.Collections;
import reactor.core.publisher.Mono;

/**
 * Defines the API that must be implemented to provide a CollectionService implementation.
 *
 * @author joshfix
 * Created on 10/17/18
 */
public interface CollectionService {

    Mono<Collections> getCollections();
    Mono<CollectionMetadata> getCollectionMetadata(String collectionId);

}
