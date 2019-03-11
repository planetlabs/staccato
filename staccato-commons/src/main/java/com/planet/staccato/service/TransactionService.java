package com.planet.staccato.service;

import com.planet.staccato.dto.StacTransactionResponse;
import com.planet.staccato.model.Item;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Defines the API that must be implemented to provide a TransactionService implementation.
 *
 * @author joshfix
 * Created on 10/17/18
 */
public interface TransactionService {

    Mono<StacTransactionResponse> putItems(Flux<Item> items);
    Mono<StacTransactionResponse> putItems(Flux<Item> items, String collectionId);
    Mono<StacTransactionResponse> updateItem(String id, String body);
    Mono<StacTransactionResponse> updateItem(String id, String body, String collectionId);
    Mono<StacTransactionResponse> updateItems(Flux<Item> items);
    Mono<StacTransactionResponse> updateItems(Flux<Item> items, String collectionId);
    Mono<StacTransactionResponse> deleteItem(String id);
    Mono<StacTransactionResponse> deleteItem(String id, String collectionId);

}
