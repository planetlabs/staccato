package com.planet.staccato.collection;

import com.planet.staccato.model.Item;
import com.planet.staccato.model.ItemCollection;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

/**
 * Defines the controller interface for the STAC collection specification
 * @see <a href="https://github.com/radiantearth/stac-spec/tree/master/collection-spec">collection-spec</a>
 *
 * @author joshfix
 * Created on 10/15/18
 */
public interface CollectionApi {

    @GetMapping(value = "/collections/{collectionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    Mono<CollectionMetadata> getCollection(@PathVariable("collectionId") String collectionId);

    @GetMapping(value = "/collections/{collectionId}/items", produces = MediaType.APPLICATION_JSON_VALUE)
    Mono<ItemCollection> getCollectionItems(@PathVariable("collectionId") String collectionId,
                                            @RequestParam(name = "limit", defaultValue = "10") Integer limit);

    @GetMapping(value = "/collections/{collectionId}/items", params = "page", produces = MediaType.APPLICATION_JSON_VALUE)
    Mono<ItemCollection> getCollectionItemsScroll(@PathVariable("collectionId") String collectionId,
                                            @RequestParam("page") Integer page);

    @GetMapping(value = "/collections/{collectionId}/items/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    Mono<Item> getCollectionItems(@PathVariable("collectionId") String collectionId,
                                  @PathVariable("itemId") String itemId);

}
