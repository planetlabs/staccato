package com.planet.staccato.transaction;

import com.planet.staccato.dto.StacTransactionResponse;
import com.planet.staccato.model.Item;
import com.planet.staccato.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Defines the controller interface for the STAC transaction specification
 * @see <a href="https://github.com/radiantearth/stac-spec/tree/master/extensions/transaction">transaction extension</a>
 * @author joshfix
 * Created on 08/15/2018
 */
@Slf4j
@RestController
public class TransactionController implements TransactionApi {

    @Autowired
    private TransactionService service;

    @Override
    public Mono<StacTransactionResponse> createItem(@RequestBody Flux<Item> items,
                                                    @PathVariable("collectionId") String collectionId) {
        /*
        return items
                .map(item -> item.getProperties())
                .map(p -> "hi")
        .then(Mono.just(new StacTransactionResponse()));
        */
        return service.putItems(items, collectionId).name("putItems");
    }

    @Override
    public Mono<StacTransactionResponse> createItem(@RequestBody Item item,
                                                    @PathVariable("collectionId") String collectionId,
                                                    @PathVariable("itemId") String itemId) {
        return service.putItems(Flux.just(item), collectionId);
    }

    @Override
    public Mono<StacTransactionResponse> updateItem(@RequestBody String body,
                                                    @PathVariable("collectionId") String collectionId,
                                                    @PathVariable("itemId") String itemId) {
        return service.updateItem(itemId, body, collectionId);
    }

    @Override
    public Mono<StacTransactionResponse> deleteItem(String collectionId, String id) {
        return service.deleteItem(id, collectionId).name("deleteItem");
    }
}
