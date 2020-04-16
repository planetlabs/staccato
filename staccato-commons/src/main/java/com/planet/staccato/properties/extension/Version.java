package com.planet.staccato.properties.extension;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.planet.staccato.elasticsearch.annotation.Mapping;
import com.planet.staccato.elasticsearch.annotation.MappingType;

/**
 * Version extension: https://github.com/radiantearth/stac-spec/blob/master/extensions/version/README.md
 *
 * @author joshfix
 * Created on 1/6/20
 */
public interface Version {

    @Mapping(type = MappingType.KEYWORD)
    @JsonProperty("version")
    String getVersion();
    void setVersion(String version);

    @Mapping(type = MappingType.BOOLEAN)
    @JsonProperty("deprecated")
    boolean isDeprecated();
    void setDeprecated(boolean deprecated);
}
