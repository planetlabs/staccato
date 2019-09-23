package com.planet.staccato.wfs;

import com.planet.staccato.model.Catalog;
import com.planet.staccato.model.Conformance;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

/**
 * @author joshfix
 * Created on 2019-09-23
 */
public interface WfsApi {

    @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    Mono<Catalog> getLandingPage();

    @GetMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
    Mono<Object> getApi();

    @GetMapping(path = "/conformance", produces = MediaType.APPLICATION_JSON_VALUE)
    Mono<Conformance> getConformance();

}
