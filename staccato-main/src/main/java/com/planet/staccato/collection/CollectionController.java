package com.planet.staccato.collection;

import com.planet.staccato.SearchRequestUtils;
import com.planet.staccato.dto.api.SearchRequest;
import com.planet.staccato.dto.api.extensions.FieldsExtension;
import com.planet.staccato.model.Item;
import com.planet.staccato.model.ItemCollection;
import com.planet.staccato.service.ApiService;
import com.planet.staccato.service.CollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
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

    private final CollectionService collectionService;
    private final ApiService apiService;

    @Override
    public Mono<CollectionMetadata> getCollection(@PathVariable("collectionId") String collectionId) {
        return collectionService.getCollectionMetadata(collectionId);
    }

    @Override
    public Mono<ItemCollection> getCollectionItems(@PathVariable("collectionId") String collectionId,
                                                   double[] bbox,
                                                   String time,
                                                   String query,
                                                   Integer limit,
                                                   String next,
                                                   String[] ids,
                                                   FieldsExtension fields,
                                                   Object intersects) {
        SearchRequest searchRequest = SearchRequestUtils.generateSearchRequest(bbox, time, query, limit, next,
                fields, ids, new String[]{collectionId}, intersects, null);
        return collectionService.getItemsInitialScroll(searchRequest);
    }

    @Override
    public Mono<ItemCollection> getCollectionItemsScroll(@PathVariable("collectionId") String collectionId,
                                                         double[] bbox,
                                                         String time,
                                                         String query,
                                                         Integer limit,
                                                         String next,
                                                         String[] ids,
                                                         FieldsExtension fields,
                                                         Object intersects) {
        SearchRequest searchRequest = SearchRequestUtils.generateSearchRequest(bbox, time, query, limit, next,
                fields, ids, new String[]{collectionId}, intersects, null);
        return collectionService.getItemsScroll(searchRequest);
    }

    @Override
    public Mono<Item> getCollectionItems(@PathVariable("collectionId") String collectionId,
                                         @PathVariable("itemId") String itemId) {
        return apiService.getItem(itemId, collectionId);
    }
}
