package com.planet.staccato.extension;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.planet.staccato.elasticsearch.annotation.Mapping;
import com.planet.staccato.elasticsearch.annotation.MappingType;

/**
 * Defines the fields and Jackson property values for fields in the checksum extension.
 * @see <a href="https://github.com/radiantearth/stac-spec/tree/master/extensions/checksum">Checksum Extension</a>
 * @author joshfix
 * Created on 2019-05-15
 */
public interface Checksum {

    String EXTENSION_PREFIX = "checksum";

    @Mapping(type = MappingType.KEYWORD)
    @JsonProperty(EXTENSION_PREFIX + ":md5")
    String getMd5();
    void setMd5(String md5);

    @Mapping(type = MappingType.KEYWORD)
    @JsonProperty(EXTENSION_PREFIX + ":sha1")
    String getSha1();
    void setSha1(String sha1);

    @Mapping(type = MappingType.KEYWORD)
    @JsonProperty(EXTENSION_PREFIX + ":sha2")
    String getSha2();
    void setSha2(String sha2);

    @Mapping(type = MappingType.KEYWORD)
    @JsonProperty(EXTENSION_PREFIX + ":sha3")
    String getSha3();
    void setSha3(String sha3);
    
}
