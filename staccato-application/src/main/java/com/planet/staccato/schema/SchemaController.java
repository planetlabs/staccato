package com.planet.staccato.schema;

import com.planet.staccato.service.SchemaService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Controller implementation for returning schema an STAC specification documents
 *
 * @author joshfix
 * Created on 11/29/17
 */
@Slf4j
@RestController
@AllArgsConstructor
public class SchemaController implements SchemaApi {

    private final SchemaService service;

    @Override
    public Mono<Object> getApiDescription() {
        return service.getApiDescription();
    }

    @Override
    public Mono<Object> getSchemaByName(@PathVariable("name") String name) {
        return service.getSchemaByName(name);
    }

}
