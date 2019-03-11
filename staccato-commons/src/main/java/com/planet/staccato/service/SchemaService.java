package com.planet.staccato.service;

import reactor.core.publisher.Mono;

/**
 * Defines the API that must be implemented to provide an SchemaService implementation.
 *
 * @author joshfix
 * Created on 12/6/17
 */
public interface SchemaService {
    Mono<Object> getSchemaByName(String name);
    Mono<Object> getApiDescription();
}
