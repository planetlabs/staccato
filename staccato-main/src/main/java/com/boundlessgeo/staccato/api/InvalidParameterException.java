package com.boundlessgeo.staccato.api;

import lombok.AllArgsConstructor;

import java.util.Objects;

/**
 * StacException
 */
@AllArgsConstructor
public class InvalidParameterException extends RuntimeException {


    public InvalidParameterException(String message) {
        super(message);
    }




}

