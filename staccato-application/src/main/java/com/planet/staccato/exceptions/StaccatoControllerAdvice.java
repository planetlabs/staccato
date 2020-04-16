package com.planet.staccato.exceptions;

import com.planet.staccato.exception.StacException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * Handles all exceptions that occur in Spring controllers and converts them to a STAC compliant error response.
 *
 * @author joshfix
 * Created on 1/31/18
 */
@RestControllerAdvice
@RequiredArgsConstructor
public class StaccatoControllerAdvice {

    private final ErrorResponseComposer composer;

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ExceptionHandler(Throwable.class)
    public Mono<ResponseEntity<StacException>> handleException(Exception ex) {
        Optional<StacException> optionalStacException = composer.compose(ex);

        StacException stacException = (optionalStacException.isEmpty())
                ? new StacException(String.valueOf(HttpStatus.BAD_REQUEST.value()), ex.getMessage())
                : optionalStacException.get();

        return Mono.just(new ResponseEntity<>(stacException, HttpStatus.valueOf(Integer.parseInt(stacException.getCode()))));
    }
}
