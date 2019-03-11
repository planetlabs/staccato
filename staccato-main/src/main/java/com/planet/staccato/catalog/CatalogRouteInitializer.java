package com.planet.staccato.catalog;

import com.planet.staccato.collection.CatalogType;
import com.planet.staccato.collection.CollectionMetadata;
import com.planet.staccato.config.LinksConfigProps;
import com.planet.staccato.model.Catalog;
import com.planet.staccato.model.Item;
import com.planet.staccato.model.ItemCollection;
import com.planet.staccato.model.Link;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebServerApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import javax.annotation.PostConstruct;
import java.util.List;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * This class is responsible for discovering all collections and catalogs and registering routes for each.
 *
 * @author joshfix
 * Created on 10/23/18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogRouteInitializer {

    private final List<CollectionMetadata> collectionMetadataList;
    private final AnnotationConfigReactiveWebServerApplicationContext context;
    private final LinkGenerator linkGenerator;
    private final RequestHandler requestHandler;
    private final Catalog rootCatalog;
    private final SubcatalogPropertiesService subcatalogPropertiesService;

    /**
     * Loops through all discovered collection objects, registers appropriate routes, and adds links to root catalog.
     */
    @PostConstruct
    public void init() {
        collectionMetadataList.forEach(collection -> {
            if (collection.getCatalogType() == CatalogType.CATALOG) {
                registerCatalogEndpoints(collection);
                rootCatalog.getLinks().add(
                        Link.build().href(LinksConfigProps.LINK_PREFIX + "/stac/" + collection.getProperties().getCollection()).rel("child"));
            } else {
                registerCollectionEndpoints(collection);
                rootCatalog.getLinks().add(
                        Link.build().href(LinksConfigProps.LINK_PREFIX + "/collections/" + collection.getProperties().getCollection()).rel("child"));
            }
        });
    }

    /**
     * Registers routes for subcatalogs.  Subcatalogs need routes that map to both the catalog/collection name as
     * well as any possible subpath, as there may be many subcatalog levels.
     *
     * @param collection The {@link CollectionMetadata}
     */
    public void registerCatalogEndpoints(CollectionMetadata collection) {

        // register the route for /stac/<collection_id>
        RouterFunction<ServerResponse> route =
                route(GET("/stac/" + collection.getProperties().getCollection()), (request) -> {
                    CollectionMetadata newCollection = getNewInstance(collection);
                    List<PropertyField> remainingProperties = subcatalogPropertiesService.getRemainingProperties(newCollection.getProperties().getCollection(), request.path());
                    linkGenerator.generatePropertyFieldLinks(request, newCollection, remainingProperties);
                    return ServerResponse.ok().body(fromObject(newCollection));
                });
        context.registerBean(collection.getProperties().getCollection() + "SubcatalogRoute", RouterFunction.class, () -> route);

        // register wildcard route for all path structures under /stac/<collection_id, including further subcatalogs and items
        RouterFunction<ServerResponse> subRoute =
                route(GET("/stac/" + collection.getProperties().getCollection() + "/**"), (request) -> {
                    CollectionMetadata newCollection = getNewInstance(collection);
                    if (request.path().toLowerCase().endsWith("/items")) {
                                return ServerResponse.ok().body(requestHandler.handleItemsRequest(newCollection, request), ItemCollection.class);
                            }
                            if (request.path().toLowerCase().contains("/items")) {
                                return ServerResponse.ok().body(requestHandler.handleItemRequest(newCollection, request), Item.class);
                            }
                            return ServerResponse.ok().body(requestHandler.handleSubcatalogRequest(newCollection, request), CollectionMetadata.class);
                        });
        context.registerBean(collection.getProperties().getCollection() + "SubcatalogChildRoute", RouterFunction.class, () -> subRoute);
    }

    /**
     * Registers routes for collections.  Each collections only needs a single route for the collection name.
     *
     * @param collection The {@link CollectionMetadata}
     */
    public void registerCollectionEndpoints(CollectionMetadata collection) {
        RouterFunction<ServerResponse> route =
                route(GET("/collections/" + collection.getProperties().getCollection()), (request) -> ServerResponse.ok().body(fromObject(collection)));
        context.registerBean(collection.getProperties().getCollection() + "CollectionRoute", RouterFunction.class, () -> route);
    }

    /**
     * Generates a new CollectionMetadata instance for every request.  This may not be optimal, but is currently necessary
     * to make sure changing values on the original singleton don't effect other requests or persist between requests.
     *
     * @param collection The {@link CollectionMetadata} singleton.
     * @return The {@link CollectionMetadata} clone that will be used as the response object.
     */
    private CollectionMetadata getNewInstance(CollectionMetadata collection) {
        try {
            return collection.getClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
