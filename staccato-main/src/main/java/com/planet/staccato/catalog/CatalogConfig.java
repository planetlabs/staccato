package com.planet.staccato.catalog;

import com.planet.staccato.config.LinksConfigProps;
import com.planet.staccato.model.Catalog;
import com.planet.staccato.model.Link;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * Spring configuration for catalog related items.
 *
 * @author joshfix
 * Created on 10/25/18
 */
@Component
@Configuration
public class CatalogConfig {

    /**
     * Creates the root catalog object.
     *
     * @return The root {@link Catalog}
     */
    @Bean
    public Catalog rootCatalog() {
        Catalog catalog = new Catalog();

        catalog.setId("staccato");
        catalog.setTitle("Staccato");
        catalog.setVersion("0.6.1");
        catalog.setDescription("STAC v0.6.2 implementation by Boundless Spatial");

        catalog.getLinks().add(Link.build()
                .rel("self")
                .href(LinksConfigProps.LINK_PREFIX + "/stac"));
        return catalog;
    }

    /**
     * Creates the /stac route that returns the root catalog
     *
     * @return The {@link RouterFunction} for the root catalog.
     */
    @Bean
    public RouterFunction<ServerResponse> rootCatalogRoute() {
        return route(GET("/stac"), (request) -> ServerResponse.ok().body(fromObject(rootCatalog())));
    }

}
