package com.planet.staccato.oaf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.planet.staccato.model.Conformance;
import com.planet.staccato.queryables.Queryables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

/**
 * @author joshfix
 * Created on 2019-09-23
 */
@Slf4j
@Service
public class DefaultOafService {

    private final List<Queryables> queryables;
    private final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    private final String[] conformance = new String[]{
            "http://www.opengis.net/spec/ogcapi-features-1/1.0/conf/core",
            //"http://www.opengis.net/spec/ogcapi-features-1/1.0/conf/oas30",
            "http://www.opengis.net/spec/ogcapi-features-1/1.0/conf/html",
            "http://www.opengis.net/spec/ogcapi-features-1/1.0/conf/geojson",
            "http://www.opengis.net/spec/ogcapi-features-1/1.0/conf/x-cql-text"
    };

    public DefaultOafService(List<Queryables> queryables) {
        this.queryables = queryables;
    }

    public Mono<Object> getApi() {
        try {
            Resource catalogResource = new UrlResource("https://raw.githubusercontent.com/radiantearth/stac-spec/568a04821935cc92de7b4b05ea6fa9f6bf8a0592/api-spec/openapi/OAFeat.yaml");
            BufferedReader catalogReader = new BufferedReader(new InputStreamReader(catalogResource.getInputStream()));
            return Mono.just(mapper.readValue(catalogReader, Object.class));
        } catch (IOException e) {
            log.error("Error reading API document", e);
            throw new RuntimeException("Unable to read API document from.");
        }
    }

    public String[] getConformance() {
        return conformance;
    }

    public Mono<List<String>> getConformanceMono() {
        return Flux.just(conformance).collectList();
    }

    public Flux<Queryables> getQueryables() {
        return Flux.fromIterable(queryables);
    }


}
