package com.planet.staccato.schema;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

/**
 * Defines the controller interface for returning schema an STAC specification documents
 *
 * @author joshfix
 * Created on 11/29/17
 */
public interface SchemaApi {

    @GetMapping(value = "/stac/schema", produces = { MediaType.APPLICATION_JSON_VALUE })
    Mono<Object> getApiDescription();

    @GetMapping(value = "/stac/schema/{name}", produces = { MediaType.APPLICATION_JSON_VALUE })
    Mono<Object> getSchemaByName(@PathVariable("name") String name);

}
