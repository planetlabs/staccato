package com.planet.staccato.model;

import com.planet.staccato.elasticsearch.annotation.Mapping;
import com.planet.staccato.elasticsearch.annotation.MappingType;

/**
 * @author joshfix
 * Created on 10/15/18
 */
public interface MandatoryProperties {

    @Mapping(type = MappingType.DATE)
    String getDatetime();
    void setDatetime(String datetime);
}
