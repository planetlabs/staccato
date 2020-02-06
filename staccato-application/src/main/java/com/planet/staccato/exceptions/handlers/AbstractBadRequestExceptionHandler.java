package com.planet.staccato.exceptions.handlers;

import org.springframework.http.HttpStatus;

/**
 * @author joshfix
 * Created on 2/6/20
 */
public class AbstractBadRequestExceptionHandler<T extends Throwable> extends AbstractExceptionHandler<T> {

    public AbstractBadRequestExceptionHandler(String exceptionName) {
        super(exceptionName);
    }

    @Override
    public HttpStatus getStatus(T ex) {
        return HttpStatus.BAD_REQUEST;
    }
}
