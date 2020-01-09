package com.planet.staccato.properties.commons;

import com.planet.staccato.elasticsearch.annotation.Mapping;
import com.planet.staccato.elasticsearch.annotation.MappingType;

/**
 * @author joshfix
 * Created on 1/8/20
 */
public interface Metadata {

    @Mapping(type = MappingType.DATE)
    String getCreated();
    void setCreated(String created);

    @Mapping(type = MappingType.DATE)
    String getUpdated();
    void setUpdated(String updated);

}
