package com.planet.staccato.wfs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.planet.staccato.config.LinksConfigProps;
import com.planet.staccato.config.StacConfigProps;
import com.planet.staccato.model.Catalog;
import com.planet.staccato.model.Conformance;
import com.planet.staccato.model.Link;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @author joshfix
 * Created on 2019-09-23
 */
@Slf4j
@Service
public class DefaultWfsService {

    private final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    private final Conformance conformance = new Conformance();
    private final Catalog catalog = new Catalog();
    private StacConfigProps configProps;

    public DefaultWfsService(StacConfigProps configProps, LinksConfigProps linksConfigProps) {
        this.configProps = configProps;
    }

    @PostConstruct
    public void init() {
        initConformance();
        initCatalog();
    }

    private void initConformance() {
        conformance.setConformsTo(List.of("http://www.opengis.net/spec/ogcapi-features-1/1.0/conf/core",
                //"http://www.opengis.net/spec/ogcapi-features-1/1.0/conf/oas30",
                "http://www.opengis.net/spec/ogcapi-features-1/1.0/conf/html",
                "http://www.opengis.net/spec/ogcapi-features-1/1.0/conf/geojson",
                "http://www.opengis.net/spec/ogcapi-features-1/1.0/conf/x-cql-text"));
    }

    private void initCatalog() {
        catalog.setId("staccato");
        catalog.setTitle("Staccato");
        catalog.setVersion(configProps.getVersion());
        catalog.setDescription("STAC v" + configProps.getVersion() + " implementation by Planet Labs");

        catalog.getLinks().add(Link.build()
                .rel("service-desc")
                .type("application/vnd.oai.openapi+json;version=3.0")
                .href(LinksConfigProps.LINK_PREFIX + "/api"));

        catalog.getLinks().add(Link.build()
                .rel("conformance")
                .type(MediaType.APPLICATION_JSON_VALUE)
                .href(LinksConfigProps.LINK_PREFIX + "/conformance"));

        catalog.getLinks().add(Link.build()
                .rel("data")
                .type(MediaType.APPLICATION_JSON_VALUE)
                .href(LinksConfigProps.LINK_PREFIX + "/collections"));

        catalog.getLinks().add(Link.build()
                .rel("self")
                .type(MediaType.APPLICATION_JSON_VALUE)
                .href(LinksConfigProps.LINK_PREFIX + "/"));
    }

    public Mono<Object> getApi() {
        try {
            Resource catalogResource = new UrlResource("https://raw.githubusercontent.com/radiantearth/stac-spec/v0.8.0-rc1/api-spec/openapi/WFS3.yaml");
            BufferedReader catalogReader = new BufferedReader(new InputStreamReader(catalogResource.getInputStream()));
            return Mono.just(mapper.readValue(catalogReader, Object.class));
        } catch (IOException e) {
            log.error("Error reading API document", e);
            throw new RuntimeException("Unable to read API document from.");
        }
    }

    public Mono<Conformance> getConformance() {
        return Mono.just(conformance);
    }

    public Mono<Catalog> getLandingPage() {
        return Mono.just(catalog);
    }
}
