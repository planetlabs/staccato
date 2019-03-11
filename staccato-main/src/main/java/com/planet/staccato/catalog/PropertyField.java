package com.planet.staccato.catalog;

import lombok.Data;

/**
 * Simple POJO to map between the text used in JSON to the variable name used in this implementation.
 *
 * @author joshfix
 * Created on 10/25/18
 */
@Data
public class PropertyField {

    private String jsonName;
    private String javaName;

    public PropertyField jsonName(String jsonName) {
        setJsonName(jsonName);
        return this;
    }

    public PropertyField javaName(String javaName) {
        setJavaName(javaName);
        return this;
    }
}
