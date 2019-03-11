package com.planet.staccato.api;

import com.planet.staccato.exception.StacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author joshfix
 * Created on 2/28/19
 */
@Component
@Order(-2)
public class InvalidParameterExceptionHandler implements ErrorWebExceptionHandler {

    @Autowired
    private DataBufferWriter dataBufferWriter;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
        StacException stacException = new StacException(ex.getMessage()).code(HttpStatus.BAD_REQUEST.toString());
        return dataBufferWriter.write(exchange.getResponse(), stacException);
    }

}
