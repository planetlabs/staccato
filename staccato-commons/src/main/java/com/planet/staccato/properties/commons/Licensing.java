package com.planet.staccato.properties.commons;

import com.planet.staccato.elasticsearch.annotation.Mapping;
import com.planet.staccato.elasticsearch.annotation.MappingType;

/**
 * Commons properties extension
 * https://github.com/radiantearth/stac-spec/tree/master/extensions/commons
 *
 * @author joshfix
 * Created on 1/8/20
 */
public interface Licensing {

    @Mapping(type = MappingType.KEYWORD)
    String getLicense();
    void setLicense(String license);

}
