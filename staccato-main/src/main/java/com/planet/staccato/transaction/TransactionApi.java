package com.planet.staccato.transaction;

import com.planet.staccato.dto.StacTransactionResponse;
import com.planet.staccato.model.Item;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *  Defines the controller interface for the STAC API specification
 *  @see <a href="https://github.com/radiantearth/stac-spec/tree/master/extensions/transaction">transaction extension</a>
 * @author joshfix
 * Created on 08/15/2018
 */
public interface TransactionApi {

    @PostMapping(value = "/collections/{collectionId}/items", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    Mono<StacTransactionResponse> createItem(@RequestBody Flux<Item> items, @PathVariable("collectionId") String collectionId);

    @PutMapping(value = "/collections/{collectionId}/items/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    Mono<StacTransactionResponse> createItem(@ModelAttribute Item item, @PathVariable("collectionId") String collectionId,
                                             @PathVariable("itemId") String itemId);

    @PatchMapping(path = "/collections/{collectionId}/items/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    Mono<StacTransactionResponse> updateItem(@RequestBody String body, @PathVariable("collectionId") String collectionId,
                                             @PathVariable("itemId") String itemId);

    @DeleteMapping(path = "/collections/{collectionId}/items/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    Mono<StacTransactionResponse> deleteItem(@PathVariable("collectionId") String collectionId, @PathVariable("id") String id);

}
