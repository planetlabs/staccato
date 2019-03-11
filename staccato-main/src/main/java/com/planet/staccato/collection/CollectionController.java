package com.planet.staccato.collection;

import com.planet.staccato.model.Item;
import com.planet.staccato.model.ItemCollection;
import com.planet.staccato.service.ApiService;
import com.planet.staccato.service.CollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Controller implementation for the STAC collection specification.
 * @see <a href="https://github.com/radiantearth/stac-spec/tree/master/collection-spec">collection-spec</a>
 *
 * @author joshfix
 * Created on 10/15/18
 */
@RestController
@RequiredArgsConstructor
public class CollectionController implements CollectionApi {

    private final CollectionService collectionService;
    private final ApiService apiService;

    @Override
    public Mono<CollectionMetadata> getCollection(@PathVariable("collectionId") String collectionId) {
        return collectionService.getCollectionMetadata(collectionId);
    }

    @Override
    public Mono<ItemCollection> getCollectionItems(@PathVariable("collectionId") String collectionId,
                                                   @RequestParam(name = "limit", defaultValue = "10") Integer limit) {
        return collectionService.getItemsInitialScroll(collectionId, limit);
    }

    @Override
    public Mono<ItemCollection> getCollectionItemsScroll(@PathVariable("collectionId") String collectionId,
                                                  @RequestParam("next") String next) {
        return collectionService.getItemsScroll(collectionId, next);
    }

    @Override
    public Mono<Item> getCollectionItems(@PathVariable("collectionId") String collectionId,
                                         @PathVariable("itemId") String itemId) {
        return apiService.getItem(itemId, collectionId);
    }
}
