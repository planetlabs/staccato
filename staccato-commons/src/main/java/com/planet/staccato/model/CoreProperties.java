package com.planet.staccato.model;

import com.planet.staccato.elasticsearch.annotation.Mapping;
import com.planet.staccato.elasticsearch.annotation.MappingType;

import java.util.Set;

/**
 * @author joshfix
 * Created on 10/15/18
 */
public interface CoreProperties {

    @Mapping(type = MappingType.DATE)
    String getDatetime();
    void setDatetime(String datetime);

    @Mapping(type = MappingType.DATE)
    String getCreated();
    void setCreated(String created);

    @Mapping(type = MappingType.DATE)
    String getUpdated();
    void setUpdated(String updated);

    @Mapping(type = MappingType.KEYWORD)
    String getLicense();
    void setLicense(String license);

    @Mapping(type = MappingType.KEYWORD)
    String getTitle();
    void setTitle(String title);

    @Mapping(type = MappingType.KEYWORD)
    Set<Provider> getProviders();
    void setProviders(Set<Provider> providers);
}
