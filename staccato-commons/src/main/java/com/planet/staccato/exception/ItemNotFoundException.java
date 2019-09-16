package com.planet.staccato.exception;

/**
 * Custom exception used when a required item is not matched in the database.
 *
 * @author joshfix
 * Created on 12/4/17
 */
public class ItemNotFoundException extends RuntimeException {

    public ItemNotFoundException(String message) {
        super(message);
    }
}
