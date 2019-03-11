package com.planet.staccato.exception;

/**
 * A custom exception to be used when errors are encountered serializing items.
 *
 * @author joshfix
 * Created on 12/5/17
 */
public class SerializationException extends RuntimeException {

    public SerializationException(String message) {
        super(message);
    }
}
