package com.planet.staccato.properties.extension;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.planet.staccato.elasticsearch.annotation.Mapping;
import com.planet.staccato.elasticsearch.annotation.MappingType;

import java.util.Collection;

/**
 * Defines the fieldsExtension and Jackson property values for fieldsExtension in the checksum extension.
 * @see <a href="https://github.com/radiantearth/stac-spec/tree/master/extensions/checksum">Checksum Extension</a>
 * @author joshfix
 * Created on 2019-05-15
 */
public interface Checksum extends StacExtension {

    String PREFIX = "checksum";
    String NAME = "checksum";

    @Mapping(type = MappingType.KEYWORD)
    @JsonProperty(PREFIX + ":multihash")
    String getMultihash();
    void setMultihash(String multihash);

}
