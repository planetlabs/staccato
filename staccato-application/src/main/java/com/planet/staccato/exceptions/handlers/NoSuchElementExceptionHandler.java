package com.planet.staccato.exceptions.handlers;

import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

/**
 * @author joshfix
 * Created on 2/6/20
 */
@Component
public class NoSuchElementExceptionHandler extends AbstractBadRequestExceptionHandler<NoSuchElementException> {

    public NoSuchElementExceptionHandler() {
        super(NoSuchElementException.class.getSimpleName());
    }

}
