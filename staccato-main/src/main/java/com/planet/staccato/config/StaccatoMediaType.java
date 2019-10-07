package com.planet.staccato.config;

import org.springframework.http.MediaType;

/**
 * @author joshfix
 * Created on 10/7/19
 */
public interface StaccatoMediaType {

    String VND_OAI_OPENAPI_JSON_VALUE = "application/vnd.oai.openapi+json;version=3.0";
    MediaType VND_OAI_OPENAPI_JSON = MediaType.valueOf(VND_OAI_OPENAPI_JSON_VALUE);

    String APPLICATION_GEO_JSON_VALUE = "application/geo+json";
    MediaType APPLICATION_GEO_JSON = MediaType.valueOf(APPLICATION_GEO_JSON_VALUE);

}
