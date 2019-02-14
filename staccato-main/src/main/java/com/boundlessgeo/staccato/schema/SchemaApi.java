package com.boundlessgeo.staccato.schema;

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

    @GetMapping(value = "/api", produces = { "application/json" })
    Mono<Object> getApiDescription();

    @GetMapping(value = "/api/{name}", produces = { "application/json" })
    Mono<Object> getSchemaByName(@PathVariable("name") String name);

}
