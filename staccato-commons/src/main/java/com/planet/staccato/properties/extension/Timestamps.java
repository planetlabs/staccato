package com.planet.staccato.properties.extension;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Defines the timestamps extension and Jackson property values
 * @see <a href="https://github.com/radiantearth/stac-spec/tree/master/extensions/timestamps">Timestamps Extension</a>
 * @author joshfix
 * Created on 06/08/2020
 */
public interface Timestamps {

    @JsonProperty("published")
    String getPublished();
    void setPublished(String published);

    @JsonProperty("unpublished")
    String getUnpublished();
    void setUnpublished(String unpublished);

    @JsonProperty("expires")
    String getExpires();
    void setExpires(String expires);

}
