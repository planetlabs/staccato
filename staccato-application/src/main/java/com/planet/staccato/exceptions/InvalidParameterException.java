package com.planet.staccato.exceptions;

import lombok.AllArgsConstructor;

import java.util.Collection;

/**
 * Simple exception class for invalid parameters in API requests.
 *
 * @author joshfix
 * Created on 2/6/20
 */
@AllArgsConstructor
public class InvalidParameterException extends RuntimeException {

    public InvalidParameterException(String message) {
        super(message);
    }

    public InvalidParameterException(String invalidParameter, Collection<String> knownParmaeters) {
        super(buildMessage(invalidParameter, knownParmaeters));
    }

    public static String buildMessage(String invalidParameter, Collection<String> knownParameters) {
        return "Invalid request parameter \'" + invalidParameter + "\'.  "
                + "Supported parameters are: \'" + String.join("\', \'", knownParameters) + "\'.";
    }
}

