package com.boundlessgeo.staccato.service;

import com.boundlessgeo.staccato.dto.SearchRequest;
import com.boundlessgeo.staccato.model.Item;
import com.boundlessgeo.staccato.model.ItemCollection;
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

    Flux<Item> getItemsFlux(SearchRequest request, String collectionId);

    Flux<Item> getItemsFlux(double[] bbox, String time, String query, Integer limit, String next,
                            String[] propertyname, String collectionId);

    Mono<ItemCollection> getItems(double[] bbox, String time, String query, Integer limit, String next,
                                  String[] propertyname, String collectionId);
}
