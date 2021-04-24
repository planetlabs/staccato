package com.planet.staccato.collection;

import com.planet.staccato.config.StaccatoMediaType;
import com.planet.staccato.dto.api.SearchRequest;
import com.planet.staccato.model.Collections;
import com.planet.staccato.model.Item;
import com.planet.staccato.model.ItemCollection;
import com.planet.staccato.queryables.Queryables;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

/**
 * Defines the controller interface for the STAC collection specification
 *
 * @author joshfix
 * Created on 10/15/18
 * @see <a href="https://github.com/radiantearth/stac-spec/tree/master/collection-spec">collection-spec</a>
 */
public interface CollectionApi {

    @GetMapping(value = "/collections", produces = MediaType.APPLICATION_JSON_VALUE)
    Mono<Collections> getCollections();

    @GetMapping(value = "/collections/{collectionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    Mono<CollectionMetadata> getCollection(@PathVariable("collectionId") String collectionId);

    @GetMapping(value = "/collections/{collectionId}/queryables",
            produces = {StaccatoMediaType.APPLICATION_GEO_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
    Mono<Queryables> getQueryables(@PathVariable("collectionId") String collectionId);

    @GetMapping(value = "/collections/{collectionId}/items",
            produces = {StaccatoMediaType.APPLICATION_GEO_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
    Mono<ItemCollection> getCollectionItems(@PathVariable("collectionId") String collectionId,
                                            @RequestParam(value = "filter-lang", required = false) String fitlerLang,
                                            @RequestParam(value = "filter-crs", required = false) String filterCrs,
                                            SearchRequest searchRequest);

    @GetMapping(value = "/collections/{collectionId}/items/{itemId}",
            produces = {StaccatoMediaType.APPLICATION_GEO_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
    Mono<Item> getCollectionItems(@PathVariable("collectionId") String collectionId,
                                  @PathVariable("itemId") String itemId);

}
