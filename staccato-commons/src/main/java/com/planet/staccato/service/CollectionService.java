package com.planet.staccato.service;

import com.planet.staccato.collection.CollectionMetadata;
import com.planet.staccato.dto.SearchRequest;
import com.planet.staccato.model.ItemCollection;
import reactor.core.publisher.Mono;

/**
 * Defines the API that must be implemented to provide a CollectionService implementation.
 *
 * @author joshfix
 * Created on 10/17/18
 */
public interface CollectionService {

    Mono<ItemCollection> getItemsInitialScroll(SearchRequest searchRequest);

    Mono<ItemCollection> getItemsScroll(SearchRequest searchRequest);

    Mono<CollectionMetadata> getCollectionMetadata(String collectionId);
}
