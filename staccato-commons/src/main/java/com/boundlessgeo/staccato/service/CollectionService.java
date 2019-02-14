package com.boundlessgeo.staccato.service;

import com.boundlessgeo.staccato.collection.CollectionMetadata;
import com.boundlessgeo.staccato.model.ItemCollection;
import reactor.core.publisher.Mono;

/**
 * Defines the API that must be implemented to provide a CollectionService implementation.
 *
 * @author joshfix
 * Created on 10/17/18
 */
public interface CollectionService {

    Mono<ItemCollection> getItemsInitialScroll(String collectionId, Integer limit);

    Mono<ItemCollection> getItemsScroll(String collectionId, String next);

    Mono<CollectionMetadata> getCollectionMetadata(String collectionId);
}
