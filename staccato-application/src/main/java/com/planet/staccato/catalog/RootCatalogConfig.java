package com.planet.staccato.catalog;

import com.planet.staccato.config.LinksConfigProps;
import com.planet.staccato.config.StacConfigProps;
import com.planet.staccato.config.StaccatoMediaType;
import com.planet.staccato.model.Catalog;
import com.planet.staccato.model.Link;
import com.planet.staccato.oaf.DefaultOafService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.planet.staccato.config.StaccatoRelType.QUERYABLES_LINK_REL;
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
public class RootCatalogConfig {

    private final StacConfigProps configProps;
    private final DefaultOafService oafService;

    public static final String ID = "staccato";
    public static final String TITLE = "Staccato";
    public static final String DESCRIPTION_TEMPLATE = "STAC v%s implementation by Planet Labs";
    public static final String SELF_LINK_REL = "self";
    public static final String SELF_LINK_HREF = "/";
    public static final String SEARCH_LINK_REL = "search";
    public static final String SEARCH_LINK_HREF = "/search";
    public static final String SERVICE_DESC_LINK_REL = "service-desc";
    public static final String SERVICE_DESC_LINK_HREF = "/api";
    public static final String CONFORMANCE_LINK_REL = "conformance";
    public static final String CONFORMANCE_LINK_HREF = "/conformance";
    public static final String DATA_LINK_REL = "data";
    public static final String DATA_LINK_HREF = "/collections";
    public static final String QUERYABLES_LINK_HREF = "/queryables";

    /**
     * Creates the root catalog object.
     *
     * @return The root {@link Catalog}
     */
    @Bean
    public Catalog rootCatalog() {
        Catalog catalog = new RootCatalog()
                .conformsTo(oafService.getConformance())
                .id(ID)
                .title(TITLE)
                .version(configProps.getVersion())
                .description(String.format(DESCRIPTION_TEMPLATE, configProps.getVersion()));

        catalog.getLinks().add(Link.build()
                .rel(SELF_LINK_REL)
                .type(MediaType.APPLICATION_JSON_VALUE)
                .href(LinksConfigProps.LINK_PREFIX + SELF_LINK_HREF));

        catalog.getLinks().add(Link.build()
                .rel(SEARCH_LINK_REL)
                .type(StaccatoMediaType.APPLICATION_GEO_JSON_VALUE)
                .method(HttpMethod.GET.toString())
                .href(LinksConfigProps.LINK_PREFIX + SEARCH_LINK_HREF));


        catalog.getLinks().add(Link.build()
                .rel(SERVICE_DESC_LINK_REL)
                .type(StaccatoMediaType.VND_OAI_OPENAPI_JSON_VALUE)
                .href(LinksConfigProps.LINK_PREFIX + SERVICE_DESC_LINK_HREF));

        catalog.getLinks().add(Link.build()
                .rel(CONFORMANCE_LINK_REL)
                .type(MediaType.APPLICATION_JSON_VALUE)
                .href(LinksConfigProps.LINK_PREFIX + CONFORMANCE_LINK_HREF));

        catalog.getLinks().add(Link.build()
                .rel(DATA_LINK_REL)
                .type(MediaType.APPLICATION_JSON_VALUE)
                .href(LinksConfigProps.LINK_PREFIX + DATA_LINK_HREF));

        catalog.getLinks().add(Link.build()
                .rel(QUERYABLES_LINK_REL)
                .type(StaccatoMediaType.APPLICATION_SCHEMA_JSON_VALUE)
                .href(LinksConfigProps.LINK_PREFIX + QUERYABLES_LINK_HREF));

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