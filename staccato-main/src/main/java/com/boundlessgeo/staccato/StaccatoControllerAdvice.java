package com.boundlessgeo.staccato;

import com.boundlessgeo.staccato.error.ErrorResponse;
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
    public Mono<ResponseEntity<ErrorResponse>> handleException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setDescription(ex.getMessage());
        errorResponse.setCode(HttpStatus.BAD_REQUEST.value());

        ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

        return Mono.just(responseEntity);
    }
}
