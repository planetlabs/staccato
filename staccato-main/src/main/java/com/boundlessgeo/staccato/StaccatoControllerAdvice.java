package com.boundlessgeo.staccato;

import com.boundlessgeo.staccato.exception.StacException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

/**
 * Handles all exceptions that occur in Spring controllers and converts them to a STAC compliant error response.
 *
 * @author joshfix
 * Created on 1/31/18
 */
@RestControllerAdvice
public class StaccatoControllerAdvice {

    @ExceptionHandler(Throwable.class)
    public Mono<ResponseEntity<StacException>> handleException(Exception ex) {
        StacException stacException = new StacException(ex.getMessage());
        stacException.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));

        ResponseEntity<StacException> responseEntity = new ResponseEntity<>(stacException, HttpStatus.BAD_REQUEST);

        return Mono.just(responseEntity);
    }
}
