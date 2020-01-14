package com.planet.staccato.service;

import com.planet.staccato.dto.api.SearchRequest;
import com.planet.staccato.model.Item;
import com.planet.staccato.model.ItemCollection;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Defines the API that must be implemented to provide a ApiService implementation.
 *
 * @author joshfix
 * Created on 10/17/18
 */
public interface ApiService {

    Mono<Item> getItem(String id, String collectionId);

    Flux<Item> getItemsFlux(SearchRequest request);

    Mono<ItemCollection> getItemCollection(SearchRequest searchRequest);
}
