package com.planet.staccato.service;

import com.planet.staccato.dto.SearchRequest;
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

    Flux<Item> getItemsFlux(double[] bbox, String time, String query, Integer limit, Integer page,
                            String[] ids, String[] collections, String[] propertyname);

    Mono<ItemCollection> getItems(double[] bbox, String time, String query, Integer limit, Integer page,
                                  String[] ids, String[] collections, String[] propertyname);
}
