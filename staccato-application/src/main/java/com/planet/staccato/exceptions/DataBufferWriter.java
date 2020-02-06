package com.planet.staccato.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Writes bytes to the http response
 *
 * @author joshfix
 * Created on 3/7/19
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataBufferWriter {

    private final ObjectMapper objectMapper;

    public <T> Mono<Void> write(ServerHttpResponse httpResponse, T object) {
        DataBufferFactory bufferFactory = httpResponse.bufferFactory();
        return httpResponse
                .writeWith(Mono.fromSupplier(() -> {
                    try {
                        return bufferFactory.wrap(objectMapper.writeValueAsBytes(object));
                    } catch (Exception ex) {
                        log.warn("Error writing response", ex);
                        return bufferFactory.wrap(new byte[0]);
                    }
                }));
    }
}
