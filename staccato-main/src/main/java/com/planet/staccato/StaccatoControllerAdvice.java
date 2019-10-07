package com.planet.staccato;

import com.planet.staccato.api.InvalidParameterException;
import com.planet.staccato.exception.StacException;
import com.planet.staccato.exception.StaccatoRuntimeException;
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
        int code = HttpStatus.BAD_REQUEST.value();
        if (ex instanceof StaccatoRuntimeException) {
            code = ((StaccatoRuntimeException)ex).getCode();
        } else if (ex instanceof InvalidParameterException) {
            code = 400;
        }
        stacException.setCode(String.valueOf(code));

        ResponseEntity<StacException> responseEntity = new ResponseEntity<>(stacException, HttpStatus.valueOf(code));

        return Mono.just(responseEntity);
    }
}
