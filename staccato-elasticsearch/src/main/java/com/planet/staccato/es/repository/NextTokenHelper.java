package com.planet.staccato.es.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Base64;

/**
 * @author joshfix
 * Created on 2019-09-16
 */
@Slf4j
public class NextTokenHelper {

    protected static final ObjectMapper mapper = new ObjectMapper();

    public static String serialize(Object object) {
        try {
            return Base64.getEncoder().encodeToString(mapper.writeValueAsBytes(object));
        } catch (Exception e) {
            log.warn("Error serializing sort values.");
        }
        return null;
    }


    public static Object[] deserialize(String string) {
        try {
            return mapper.readValue(Base64.getDecoder().decode(string), Object[].class);
        } catch (Exception e) {
            log.warn("Error deserializing sort values from string: " + string);
        }
        return null;
    }
}
