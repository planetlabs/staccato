package com.boundlessgeo.staccato.grpc.service;

import com.boundlessgeo.staccato.grpc.generated.ReactorCatalogServiceGrpc;
import com.boundlessgeo.staccato.service.CatalogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salesforce.grpc.contrib.spring.GrpcService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * @author joshfix
 * Created on 2/14/19
 */
@Slf4j
@GrpcService
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "staccato.grpc", value = "enabled", havingValue = "true")
public class GrpcCatalogService extends ReactorCatalogServiceGrpc.CatalogServiceImplBase {

    private final CatalogService catalogService;
    private final ObjectMapper mapper;

}
