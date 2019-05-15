package com.planet.staccato.extension;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.planet.staccato.elasticsearch.annotation.Mapping;
import com.planet.staccato.elasticsearch.annotation.MappingType;
import com.planet.staccato.model.Provider;

import java.util.Collection;

/**
 * Defines the fields and Jackson property values for fields in the single item extension.
 * @see <a href="https://github.com/radiantearth/stac-spec/tree/master/extensions/single-item">Single Item Extension</a>
 * @author joshfix
 * Created on 2/11/19
 */
public interface SingleItem {

    @Mapping(type = MappingType.KEYWORD)
    @JsonProperty("item:license")
    String getLicense();
    void setLicense(String license);

    @JsonProperty("item:providers")
    Collection<Provider> getProviders();
    void setProviders(Collection<Provider> providers);

}
