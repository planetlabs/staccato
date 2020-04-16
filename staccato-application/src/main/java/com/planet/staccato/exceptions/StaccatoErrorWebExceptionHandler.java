package com.planet.staccato.exceptions;

import com.planet.staccato.exception.StacException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * This class handles exceptions thrown in WebFilters.
 *
 * @author joshfix
 * Created on 2/28/19
 */
@Order(-2)
@Component
@RequiredArgsConstructor
public class StaccatoErrorWebExceptionHandler implements ErrorWebExceptionHandler {

    private final DataBufferWriter dataBufferWriter;
    private final ErrorResponseComposer composer;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        Optional<StacException> optionalStacException = composer.compose(ex);

        StacException stacException = optionalStacException.isEmpty()
                ? new StacException(String.valueOf(HttpStatus.BAD_REQUEST.value()), ex.getMessage())
                : optionalStacException.get();

        exchange.getResponse().setStatusCode(HttpStatus.valueOf(Integer.parseInt(stacException.getCode())));
        exchange.getResponse().getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return dataBufferWriter.write(exchange.getResponse(), stacException);
    }

}
