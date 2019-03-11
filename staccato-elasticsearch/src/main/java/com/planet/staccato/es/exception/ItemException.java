package com.planet.staccato.es.exception;

/**
 * Custom exception to be used when errors are encountered handling items.
 *
 * @author joshfix
 * Created on 1/17/18
 */
public class ItemException extends RuntimeException {

    public ItemException(String message) {
        super(message);
    }
}
