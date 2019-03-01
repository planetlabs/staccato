package com.boundlessgeo.staccato.service;

import com.boundlessgeo.staccato.collection.CollectionMetadata;
import com.boundlessgeo.staccato.model.Item;
import com.boundlessgeo.staccato.model.ItemCollection;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * Defines the API that must be implemented to provide a CatalogService implementation.
 *
 * @author joshfix
 * Created on 2/8/19
 */
public interface CatalogService {
    Mono<Item> getItem(String itemId, String collectionId);

    List<String> getValuesForField(CollectionMetadata collection, List<String> path);

    Mono<ItemCollection> getItems(String collectionId, Map<String, String> pathVariables);
}
