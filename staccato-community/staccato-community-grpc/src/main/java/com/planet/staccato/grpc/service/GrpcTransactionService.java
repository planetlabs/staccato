package com.planet.staccato.grpc.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.ByteString;
import com.planet.staccato.dto.StacTransactionResponse;
import com.planet.staccato.grpc.generated.*;
import com.planet.staccato.model.Item;
import com.planet.staccato.service.TransactionService;
import com.salesforce.grpc.contrib.spring.GrpcService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * @author joshfix
 * Created on 2/14/19
 */
@Slf4j
@GrpcService
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "staccato.grpc", value = "enabled", havingValue = "true")
public class GrpcTransactionService extends ReactorTransactionServiceGrpc.TransactionServiceImplBase {

    private final TransactionService transactionService;
    private final ObjectMapper mapper;

    @Override
    public Mono<TransactionResponse> putItem(Mono<TransactionItemBytes> indexRequest) {
        log.debug("Incoming gRPC index request.");

        return indexRequest
                .map(TransactionItemBytes::getItem)
                .map(this::deserializeItem)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .flatMap(item -> transactionService.putItems(Flux.just(item)))
                .map(this::buildStacResponse)
                .switchIfEmpty(Mono.error(new RuntimeException("Unable to add item to the catalog.")));
    }

    @Override
    public Mono<TransactionResponse> updateItem(Mono<TransactionUpdateRequest> request) {
        log.debug("Incoming gRPC update request.");

        return request
                .flatMap(r -> transactionService.updateItem(r.getId(), r.getItem()))
                .map(this::buildStacResponse)
                .switchIfEmpty(Mono.error(new RuntimeException("Unable to update item in the catalog.")));
    }

    @Override
    public Mono<TransactionResponse> deleteItem(Mono<TransactionDeleteRequest> request) {
        log.debug("Incoming gRPC delete request.");

        return request
                .map(TransactionDeleteRequest::getId)
                .flatMap(transactionService::deleteItem)
                .doOnError(e -> log.error("Error encountered during gRPC request while searching by ID. ", e))
                .map(this::buildStacResponse);
    }

    private TransactionResponse buildStacResponse(StacTransactionResponse r) {
        TransactionResponse.Builder builder = TransactionResponse.newBuilder();
        builder.setSuccess(r.isSuccess());
        if (null != r.getId()) {
            builder.setId(r.getId());
        }
        if (null != r.getResult()) {
            Object o = r.getResult();
            if (o instanceof String) {
                builder.setResult((String)o);
            } else if (o instanceof StacTransactionResponse) {
                Object o2 = ((StacTransactionResponse)r.getResult()).getResult();
                if (null != o2 && o2 instanceof String) {
                    builder.setResult((String)o2);
                }
            }
        }
        if (null != r.getReason()) {
            builder.setReason(r.getReason());
        }
        return builder.build();
    }

    private Optional<ByteString> serializeItem(Item item) {
        try {
            return Optional.of(ByteString.copyFrom(mapper.writeValueAsBytes(item)));
        } catch (Exception e) {
            log.error("Error serializing item: " + item.toString(), e);
        }
        return Optional.empty();
    }

    private Optional<Item> deserializeItem(ByteString bytes) {
        try {
            return Optional.of(mapper.readValue(bytes.toByteArray(), Item.class));
        } catch (Exception e) {
            log.error("Error deserializing item: {}", new String(bytes.toByteArray()), e);
        }
        return Optional.empty();
    }

}
