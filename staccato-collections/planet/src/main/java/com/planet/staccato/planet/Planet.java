package com.planet.staccato.planet;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author joshfix
 * Created on 4/18/18
 */
public interface Planet {

    String EXTENSION_PREFIX = "pl";

    @JsonAlias("ground_control")
    @JsonProperty(EXTENSION_PREFIX + ":ground_control")
    Boolean getGroundControl();
    void setGroundControl(Boolean groundControl);

    @JsonAlias("item_type")
    @JsonProperty(EXTENSION_PREFIX + ":item_type")
    String getItemType();
    void setItemType(String itemType);

    @JsonAlias("provider")
    @JsonProperty(EXTENSION_PREFIX + ":provider")
    String getProvider();
    void setProvider(String provider);

    @JsonAlias("published")
    @JsonProperty(EXTENSION_PREFIX + ":published")
    String getPublished();
    void setPublished(String published);

    @JsonAlias("quality_category")
    @JsonProperty(EXTENSION_PREFIX + ":quality_category")
    String getQualityCategory();
    void setQualityCategory(String quailtyCategory);

    @JsonAlias("satellite_azimuth")
    @JsonProperty(EXTENSION_PREFIX + ":satellite_azimuth")
    Double getSatelliteAzimuth();
    void setSatelliteAzimuth(Double satelliteAzimuth);

    @JsonAlias("satellite_id")
    @JsonProperty(EXTENSION_PREFIX + ":satellite_id")
    String getSatelliteId();
    void setSatelliteId(String satelliteId);

    @JsonAlias("strip_id")
    @JsonProperty(EXTENSION_PREFIX + ":strip_id")
    String getStripId();
    void setStripId(String stripId);

    @JsonAlias("updated")
    @JsonProperty(EXTENSION_PREFIX + ":updated")
    String getPlUpdated();
    void setPlUpdated(String updated);

}
