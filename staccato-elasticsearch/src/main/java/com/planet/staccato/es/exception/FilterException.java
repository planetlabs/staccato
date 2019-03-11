package com.planet.staccato.es.exception;

/**
 * A custom exception to be used when errors are encountered parsing a api query.
 *
 * @author joshfix
 * Created on 12/5/17
 */
public class FilterException extends RuntimeException {

    public FilterException(String message) {
        super(message);
    }
}
