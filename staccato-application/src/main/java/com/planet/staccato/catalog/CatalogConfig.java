package com.planet.staccato.catalog;

import com.planet.staccato.config.LinksConfigProps;
import com.planet.staccato.config.StacConfigProps;
import com.planet.staccato.config.StaccatoMediaType;
import com.planet.staccato.model.Catalog;
import com.planet.staccato.model.Link;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;
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
@RequiredArgsConstructor
public class CatalogConfig {

    private final StacConfigProps configProps;

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
        catalog.setVersion(configProps.getVersion());
        catalog.setDescription("STAC v" + configProps.getVersion() + " implementation by Planet Labs");

        catalog.getLinks().add(Link.build()
                .rel("self")
                .type(MediaType.APPLICATION_JSON_VALUE)
                .href(LinksConfigProps.LINK_PREFIX + "/"));

        catalog.getLinks().add(Link.build()
                .rel("search")
                .type(MediaType.APPLICATION_JSON_VALUE)
                .method(HttpMethod.GET.toString())
                .href(LinksConfigProps.LINK_PREFIX + "/search"));

        catalog.getLinks().add(Link.build()
                .rel("search")
                .type(MediaType.APPLICATION_JSON_VALUE)
                .method(HttpMethod.POST.toString())
                .href(LinksConfigProps.LINK_PREFIX + "/search"));

        catalog.getLinks().add(Link.build()
                .rel("service-desc")
                .type(StaccatoMediaType.VND_OAI_OPENAPI_JSON_VALUE)
                .href(LinksConfigProps.LINK_PREFIX + "/api"));

        catalog.getLinks().add(Link.build()
                .rel("conformance")
                .type(MediaType.APPLICATION_JSON_VALUE)
                .href(LinksConfigProps.LINK_PREFIX + "/conformance"));

        catalog.getLinks().add(Link.build()
                .rel("data")
                .type(MediaType.APPLICATION_JSON_VALUE)
                .href(LinksConfigProps.LINK_PREFIX + "/collections"));

        return catalog;
    }

    /**
     * Creates the /stac route that returns the root catalog
     *
     * @return The {@link RouterFunction} for the root catalog.
     */
    @Bean
    public RouterFunction<ServerResponse> rootCatalogRoute() {
        return route(GET("/"), (request) -> ServerResponse.ok().body(fromValue(rootCatalog())));
    }

}