package com.planet.staccato.exception;

/**
 * Custom exception for catalog related errors.
 *
 * @author joshfix
 * Created on 12/4/17
 */
public class CatalogException extends RuntimeException {

    public CatalogException(String message) {
        super(message);
    }
}
