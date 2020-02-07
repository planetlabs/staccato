package com.planet.staccato.schema;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import com.planet.staccato.collection.CollectionMetadata;
import com.planet.staccato.service.SchemaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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

    private Map<String, Object> schemaMap = new HashMap<>();
    @Autowired
    private ObjectMapper mapper;
    private JsonSchemaGenerator genny;

    @PostConstruct
    private void init() {
        genny = new JsonSchemaGenerator(mapper);


        if (null != collections && !collections.isEmpty()) {

            for (CollectionMetadata collection : collections) {
                try {
                    Class clazz = collection.getProperties().getClass();
                    schemaMap.put(collection.getId(),
                            mapper.readValue(mapper.writeValueAsString(genny.generateSchema(clazz)), Object.class));
                } catch (Exception e) {
                    log.error("Error generating item property schema for class '" +
                            collection.getProperties().getClass() + "'", e);
                }
            }

            try {
                Resource catalogResource = new UrlResource("https://raw.githubusercontent.com/radiantearth/stac-spec/v0.9.0-rc1/catalog-spec/json-schema/catalog.json");
                Resource collectionResource = new UrlResource("https://raw.githubusercontent.com/radiantearth/stac-spec/v0.9.0-rc1/collection-spec/json-schema/collection.json");
                Resource itemResource = new UrlResource("https://raw.githubusercontent.com/radiantearth/stac-spec/v0.9.0-rc1/item-spec/json-schema/item.json");

                BufferedReader catalogReader = new BufferedReader(new InputStreamReader(catalogResource.getInputStream()));
                BufferedReader collectionReader = new BufferedReader(new InputStreamReader(collectionResource.getInputStream()));
                BufferedReader itemReader = new BufferedReader(new InputStreamReader(itemResource.getInputStream()));

                schemaMap.put("catalog-spec", mapper.readValue(catalogReader, Object.class));
                schemaMap.put("collection-spec", mapper.readValue(collectionReader, Object.class));
                schemaMap.put("item-spec", mapper.readValue(itemReader, Object.class));
            } catch (IOException e) {
                log.error("Error reading remote schemas.", e);
            }
        }
    }

    public Mono<Object> getSchemaByName(String name) {

        if (schemaMap.containsKey(name)) {
            try {
                return Mono.just(schemaMap.get(name));
            } catch (Exception ex) {
                return Mono.empty();
            }
        }
        throw new RuntimeException("Unable to locate schema for " + name);

    }

    public Mono<Object> getApiDescription() {
            return Mono.just(schemaMap);
    }

}
