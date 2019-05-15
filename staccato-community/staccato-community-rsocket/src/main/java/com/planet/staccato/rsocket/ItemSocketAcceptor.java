package com.planet.staccato.rsocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.planet.staccato.SerializationUtils;
import com.planet.staccato.collection.CollectionMetadata;
import com.planet.staccato.service.ApiService;
import io.rsocket.*;
import io.rsocket.util.DefaultPayload;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RSocket acceptor.  Handles incoming requests for items and item collections.
 * TODO: define a better schema for the objects and request names stored in metadata and move to a plugins
 * @author joshfix
 * Created on 10/4/18
 */
@Slf4j
public class ItemSocketAcceptor implements SocketAcceptor {

    private final ObjectMapper mapper;
    private final ApiService service;
    private Map<String, byte[]> collectionMetadataMap = new HashMap<>();

    public ItemSocketAcceptor(ObjectMapper mapper, ApiService service, List<CollectionMetadata> collectionMetadataList) throws Exception{
        this.mapper = mapper;
        this.service = service;
        for (CollectionMetadata cm : collectionMetadataList) {
            collectionMetadataMap.put(cm.getId(), mapper.writeValueAsBytes(cm));
        }
    }

    @Override
    public Mono<RSocket> accept(ConnectionSetupPayload setupPayload, RSocket reactiveSocket) {
        return Mono.just(
                new AbstractRSocket() {
                    @Override
                    public Mono<Payload> requestResponse(Payload payload) {
                        log.debug("Incoming RSocket request with payload: " + payload.getDataUtf8());
                        String operation = payload.getMetadataUtf8();

                        switch (operation) {

                            case "getCollection":
                                return Mono.just(DefaultPayload.create(collectionMetadataMap.get(payload.getDataUtf8())));
                            case "getItem":
                            default:
                                return service.getItem(payload.getDataUtf8(), null)
                                        .map(item -> SerializationUtils.serializeItem(item, mapper))
                                        .map(DefaultPayload::create)
                                        .doOnError(e -> log.error("Error getting item for RSocket response.", e));
                        }

                    }
                });
    }
}
