package com.planet.staccato.grpc.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.primitives.Doubles;
import com.google.protobuf.ByteString;
import com.planet.staccato.SearchRequestUtils;
import com.planet.staccato.SerializationUtils;
import com.planet.staccato.dto.api.extensions.FieldsExtension;
import com.planet.staccato.grpc.generated.ApiIdRequest;
import com.planet.staccato.grpc.generated.ApiItemBytes;
import com.planet.staccato.grpc.generated.ApiRequest;
import com.planet.staccato.grpc.generated.ReactorApiServiceGrpc;
import com.planet.staccato.service.ApiService;
import com.salesforce.grpc.contrib.spring.GrpcService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

/**
 * @author joshfix
 * Created on 2/14/19
 */
@Slf4j
@GrpcService
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "staccato.grpc", value = "enabled", havingValue = "true")
public class GrpcApiService extends ReactorApiServiceGrpc.ApiServiceImplBase {

    private final ApiService apiService;
    private final ObjectMapper mapper;

    @Override
    public Flux<ApiItemBytes> search(Mono<ApiRequest> request) {
        log.debug("Incoming gRPC api request.");

        return request
                //double[] bbox, String datetime, String query, Integer limit, Integer next, FieldsExtension fields, String[] ids, String[] collections, Object intersects
                .flatMapMany(r -> {
                            try {
                                return apiService.getItemsFlux(SearchRequestUtils.generateSearchRequest(Doubles.toArray(r.getBboxList()),
                                        r.getTime(), r.getSearch(), r.getLimit(), r.getNext(),
                                        mapper.readValue(r.getFields(), FieldsExtension.class),
                                        r.getIdsList().toArray(new String[r.getIdsCount()]),
                                        r.getCollectionsList().toArray(new String[r.getCollectionsCount()]),
                                        r.getIntersects(), null))
                                        .map(item -> SerializationUtils.serializeItem(item, mapper))
                                        .map(item -> ApiItemBytes.newBuilder().setItem(ByteString.copyFrom(item)).build());
                            } catch (IOException e) {
                                return Mono.error(new RuntimeException("Error deserializing fields property.", e));
                            }
                        }
                );
    }

    @Override
    public Mono<ApiItemBytes> searchById(Mono<ApiIdRequest> request) {
        log.debug("Incoming gRPC api by id request.");
        return request
                .flatMapMany(r -> apiService.getItem(r.getId(), null))
                .map(item -> SerializationUtils.serializeItem(item, mapper))
                .single()
                .map(item -> ApiItemBytes.newBuilder().setItem(ByteString.copyFrom(item)).build())
                .doOnError(e -> log.error("Error encountered during gRPC request while searching by ID. ", e));
    }

}