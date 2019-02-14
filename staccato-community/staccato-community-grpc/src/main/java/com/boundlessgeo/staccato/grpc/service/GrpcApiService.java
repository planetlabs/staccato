package com.boundlessgeo.staccato.grpc.service;

import com.boundlessgeo.staccato.SerializationUtils;
import com.boundlessgeo.staccato.grpc.generated.ApiIdRequest;
import com.boundlessgeo.staccato.grpc.generated.ApiItemBytes;
import com.boundlessgeo.staccato.grpc.generated.ApiRequest;
import com.boundlessgeo.staccato.grpc.generated.ReactorApiServiceGrpc;
import com.boundlessgeo.staccato.service.ApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.primitives.Doubles;
import com.google.protobuf.ByteString;
import com.salesforce.grpc.contrib.spring.GrpcService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//import com.boundlessgeo.staccato.grpc.generated.*;
//import com.google.protobuf.ByteString;

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
                .flatMapMany(r -> apiService.getItemsFlux(Doubles.toArray(r.getBboxList()),
                        r.getTime(), r.getSearch(), r.getLimit(), r.getNext(),
                        r.getPropertynameList().toArray(new String[r.getPropertynameList().size()]), r.getCollection())
                        .map(item -> SerializationUtils.serializeItem(item, mapper))
                        .map(item -> ApiItemBytes.newBuilder().setItem(ByteString.copyFrom(item)).build())
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