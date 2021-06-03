package com.planet.staccato.oaf;

import com.planet.staccato.config.StaccatoMediaType;
import com.planet.staccato.model.Conformance;
import com.planet.staccato.queryables.Queryables;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author joshfix
 * Created on 2019-09-23
 */
public interface OafApi {

    @GetMapping(path = "/api",
            produces = {StaccatoMediaType.VND_OAI_OPENAPI_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
    Mono<Object> getApi();

    @GetMapping(path = "/conformance", produces = MediaType.APPLICATION_JSON_VALUE)
    Mono<List<String>> getConformance();

    @GetMapping(path = "/queryables", produces = StaccatoMediaType.APPLICATION_SCHEMA_JSON_VALUE)
    Flux<Queryables> getQueryables();

}
