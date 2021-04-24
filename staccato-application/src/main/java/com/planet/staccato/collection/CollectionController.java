package com.planet.staccato.collection;

import com.planet.staccato.dto.api.SearchRequest;
import com.planet.staccato.model.Collections;
import com.planet.staccato.model.Item;
import com.planet.staccato.model.ItemCollection;
import com.planet.staccato.queryables.Queryables;
import com.planet.staccato.service.ApiService;
import com.planet.staccato.service.CollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Controller implementation for the STAC collection specification.
 *
 * @author joshfix
 * Created on 10/15/18
 * @see <a href="https://github.com/radiantearth/stac-spec/tree/master/collection-spec">collection-spec</a>
 */
@RestController
@RequiredArgsConstructor
public class CollectionController implements CollectionApi {

    private final ApiService apiService;
    private final CollectionService collectionService;

    @Override
    public Mono<Collections> getCollections() {
        return collectionService.getCollectionsMono();
    }

    @Override
    public Mono<CollectionMetadata> getCollection(@PathVariable("collectionId") String collectionId) {
        return collectionService.getCollectionMetadata(collectionId);
    }

    @Override
    public Mono<Queryables> getQueryables(String collectionId) {
        return collectionService.getQueryables(collectionId);
    }

    @Override
    public Mono<ItemCollection> getCollectionItems(@PathVariable("collectionId") String collectionId,
                                                   @RequestParam(value = "filter-lang", required = false) String filterLang,
                                                   @RequestParam(value = "filter-crs", required = false) String filterCrs,
                                                   SearchRequest searchRequest) {
        searchRequest
                .method(HttpMethod.GET.toString())
                .filterLang(filterLang)
                .filterCrs(filterCrs);
        return apiService.getItemCollection(searchRequest.collections(new String[]{collectionId}));
    }

    @Override
    public Mono<Item> getCollectionItems(@PathVariable("collectionId") String collectionId,
                                         @PathVariable("itemId") String itemId) {
        return apiService.getItem(itemId, collectionId);
    }
}
