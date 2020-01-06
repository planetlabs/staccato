package com.planet.staccato.extension;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Version extension: https://github.com/radiantearth/stac-spec/blob/master/extensions/version/README.md
 *
 * @author joshfix
 * Created on 1/6/20
 */
public interface Version {

    @JsonProperty("version")
    String getVersion();
    void setVersion(String version);

    @JsonProperty("deprecated")
    boolean isDeprecated();
    void setDeprecated(boolean deprecated);
}
