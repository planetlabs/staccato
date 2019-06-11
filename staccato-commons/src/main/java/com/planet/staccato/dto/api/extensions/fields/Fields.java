package com.planet.staccato.dto.api.extensions.fields;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Set;

/**
 * @author joshfix
 * Created on 2019-06-11
 */
public class Fields {

    private Set<String> include;
    private Set<String> exclude;

}
