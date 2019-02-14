package com.boundlessgeo.staccato.error;

import lombok.Data;

/**
 * DTO for responding with STAC error message.
 *
 * @author joshfix
 * Created on 12/12/17
 */
@Data
public class ErrorResponse {
    private String description;
    private int code;
}
