package com.boundlessgeo.staccato.rsocket;

import com.boundlessgeo.staccato.collection.CollectionMetadata;
import com.boundlessgeo.staccato.service.ApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration for RSocket.
 *
 * @author joshfix
 * Created on 10/4/18
 */

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "staccato.rsocket", name = "enabled", havingValue = "true")
public class RSocketAutoConfig {

    private final ObjectMapper mapper;
    private final ApiService service;
    private final List<CollectionMetadata> collectionMetadataList;

    @Bean
    public ItemSocketAcceptor itemSocketAcceptor() throws Exception {
        return new ItemSocketAcceptor(mapper, service, collectionMetadataList);
    }

    @Bean
    public RSocketConfigProps rSocketConfigProps() {
        return new RSocketConfigProps();
    }

    @Bean
    public RSocketService rSocketService() throws Exception {
        return new RSocketService(rSocketConfigProps(), itemSocketAcceptor());
    }

}
