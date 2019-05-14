package com.planet.staccato.api;

import lombok.AllArgsConstructor;

/**
 * StacException
 */
@AllArgsConstructor
public class InvalidParameterException extends RuntimeException {

    public InvalidParameterException(String message) {
        super(message);
    }

}

