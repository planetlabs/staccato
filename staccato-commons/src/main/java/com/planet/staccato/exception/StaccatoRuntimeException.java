package com.planet.staccato.exception;

import lombok.Data;

/**
 * @author joshfix
 * Created on 10/6/19
 */
@Data
public class StaccatoRuntimeException extends RuntimeException {

    private int code = 400;

    public StaccatoRuntimeException(String message) {
        super(message);
    }

    public StaccatoRuntimeException(String message, int code) {
        super(message);
        this.code = code;
    }

    public StaccatoRuntimeException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }
}
