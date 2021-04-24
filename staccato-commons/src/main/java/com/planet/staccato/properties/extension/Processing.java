package com.planet.staccato.properties.extension;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Defines the fieldsExtension and Jackson property values for fieldsExtension in the scientific extension.
 * @see <a href="https://github.com/stac-extensions/processing">Processing Extension</a>
 * @author joshfix
 * Created on 2/11/19
 */
public interface Processing {

    String EXTENSION_PREFIX = "processing";

    @JsonProperty(EXTENSION_PREFIX + ":lineage")
    String getLineage();
    void setLineage(String lineage);

    @JsonProperty(EXTENSION_PREFIX + ":level")
    String getLevel();
    void setLevel(String level);

    @JsonProperty(EXTENSION_PREFIX + ":facility")
    String getFacility();
    void setFacility(String facility);

    @JsonProperty(EXTENSION_PREFIX + ":software")
    Map<String, String> getSoftware();
    void setSoftware(Map<String, String> software);
}
