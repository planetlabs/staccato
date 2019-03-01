package com.boundlessgeo.staccato.catalog;

import com.boundlessgeo.staccato.collection.CollectionMetadata;
import com.boundlessgeo.staccato.es.api.ElasticsearchApiService;
import com.boundlessgeo.staccato.model.Item;
import com.boundlessgeo.staccato.model.ItemCollection;
import com.boundlessgeo.staccato.service.AggregationService;
import com.boundlessgeo.staccato.service.CatalogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Responsible for processing any request for collections or subcatalogs.
 *
 * @author joshfix
 * Created on 10/24/18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RequestHandler {

    private final SubcatalogPropertiesService subcatalogPropertiesService;
    private final LinkGenerator linkGenerator;
    private final CatalogService catalogService;
    private final AggregationService aggregationService;
    private final ElasticsearchApiService searchService;

    public Mono<ItemCollection> handleItemsRequest(CollectionMetadata collection, ServerRequest request) {
        // api for all items with the given subcatalog parameters
        Map<String, String> subcatalogPathParamMap = subcatalogPropertiesService
                .createSubcatalogPathParamMap(collection.getProperties().getCollection(), request.path());
        return catalogService.getItems(collection.getProperties().getCollection(), subcatalogPathParamMap);
    }

    public Mono<Item> handleItemRequest(CollectionMetadata collection, ServerRequest request) {
        // the path contains /items, but it doesn't end with items... assume it ends with an item id
        String[] pathParts = request.path().split("/");
        String itemId = pathParts[pathParts.length - 1];
        return catalogService.getItem(itemId, collection.getProperties().getCollection());
    }

    /**
     * This method is executed if somebody has traversed down the /stac/xxx path into a subcatalog.
     *
     * @param collection
     * @param request
     * @return
     */
    public Mono<CollectionMetadata> handleSubcatalogRequest(CollectionMetadata collection, ServerRequest request) {
        String path = request.path();
        String collectionId = collection.getProperties().getCollection();
        List<String> parsePath = subcatalogPropertiesService.parsePath(path);
        Map<String, String> propertiesMap =
                subcatalogPropertiesService.createSubcatalogPathParamMap(collectionId, request.path());

        // calculate and set the extent given the url path filters
        collection.setExtent(aggregationService.getExtent(collectionId, propertiesMap));
        collection.setId(request.path().replace("/stac/", ""));
        generatePrettyTitle(collection, parsePath);

        int size = parsePath.size();
        if (size % 2 == 0) {
            // even -- means we have both a property name and value -- should display more property name links
            log.debug("EVEN (should display properties) - path: " + path + " - size: " + size);

            List<PropertyField> remainingProperties = subcatalogPropertiesService.getRemainingProperties(collection.getProperties().getCollection(), request.path());
            linkGenerator.generatePropertyFieldLinks(request, collection, remainingProperties);

            // if there are no remaining properties (eg, no more subcatalogs to traverse down), generate item links
            if (remainingProperties.isEmpty()) {
                StringBuilder filterBuilder = new StringBuilder();
                Iterator<Map.Entry<String, String>> it = propertiesMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> entry = it.next();
                    filterBuilder.append("properties.").append(entry.getKey()).append("=").append(entry.getValue());
                    if (it.hasNext()) {
                        filterBuilder.append(" AND ");
                    }
                }
                return searchService.getItemsFlux(null, null, filterBuilder.toString(), 10000, null, new String[]{"id"}, collectionId)
                        .map(item -> item.getId())
                        .map(id -> linkGenerator.buildItemLink(collectionId, id))
                        .map(link -> collection.getLinks().add(link))
                        .then(Mono.just(collection));
            }

            return Mono.just(collection);
        } else {
            // odd
            log.debug("ODD (should display values) - path: " + path + " - size: " + size);

            collection.getLinks().clear();

            List<String> values = catalogService.getValuesForField(collection, parsePath);
            linkGenerator.generatePropertyValueLinks(request, collection, values);

            return Mono.just(collection);
        }
    }

    private void generatePrettyTitle(CollectionMetadata collection, List<String> pathValues) {
        if (pathValues.size() == 1) {
            return;
        }
        String title = collection.getTitle() + ": ";
        for (int i = 2; i < pathValues.size(); i = i + 2) {
            String property = pathValues.get(i);
            title += property;
            if ((i + 2) <= pathValues.size()) {
                String value = pathValues.get(i + 1);
                title += " " + value;
                if ((i + 3) <= pathValues.size()) {
                    title += ", ";
                }
            }
        }
        collection.setTitle(title);
    }

}
