package com.boundlessgeo.staccato.schema;

import com.boundlessgeo.staccato.collection.CollectionMetadata;
import com.boundlessgeo.staccato.service.SchemaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service responsible for returning JSON schemas and STAC specification.
 *
 * @author joshfix
 * Created on 12/13/17
 */
@Slf4j
@Service
public class DefaultSchemaService implements SchemaService {

    @Autowired(required = false)
    private List<CollectionMetadata> collections;
    private Map<String, Class> collectionMap = new HashMap<>();
    @Autowired
    private ObjectMapper mapper;
    private JsonSchemaGenerator genny;

    @PostConstruct
    private void init() {
        genny = new JsonSchemaGenerator(mapper);
        if (null != collections && !collections.isEmpty()) {
            for (CollectionMetadata collection : collections) {
                collectionMap.put(collection.getId(), collection.getProperties().getClass());
            }
        }
    }

    public Mono<Object> getSchemaByName(String name) {
        try {
            return Mono.just(genny.generateSchema(collectionMap.get(name)));

        } catch (Exception ex) {
            return Mono.just(null);
        }
    }

    public Mono<Object> getApiDescription() {
        try {
            Resource resource = new ClassPathResource("openapi.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            return Mono.just(mapper.readValue(reader, Object.class));
        } catch (IOException e) {
            log.error("Error reading openapi.json from resources.", e);
        }
        return Mono.just("Error reading OpenAPI doucment.");
    }

}
