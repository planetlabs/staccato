package com.planet.staccato.planet;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author joshfix
 * Created on 4/18/18
 */
public interface Planet {

    String name = "planet";
    String PREFIX = "pl";

    @JsonAlias("ground_control")
    @JsonProperty(PREFIX + ":ground_control")
    Boolean getGroundControl();
    void setGroundControl(Boolean groundControl);

    @JsonAlias("item_type")
    @JsonProperty(PREFIX + ":item_type")
    String getItemType();
    void setItemType(String itemType);

    @JsonAlias("provider")
    @JsonProperty(PREFIX + ":provider")
    String getProvider();
    void setProvider(String provider);

    @JsonAlias("published")
    @JsonProperty(PREFIX + ":published")
    String getPublished();
    void setPublished(String published);

    @JsonAlias("quality_category")
    @JsonProperty(PREFIX + ":quality_category")
    String getQualityCategory();
    void setQualityCategory(String quailtyCategory);

    @JsonAlias("satellite_azimuth")
    @JsonProperty(PREFIX + ":satellite_azimuth")
    Double getSatelliteAzimuth();
    void setSatelliteAzimuth(Double satelliteAzimuth);

    @JsonAlias("satellite_id")
    @JsonProperty(PREFIX + ":satellite_id")
    String getSatelliteId();
    void setSatelliteId(String satelliteId);

    @JsonAlias("strip_id")
    @JsonProperty(PREFIX + ":strip_id")
    String getStripId();
    void setStripId(String stripId);

    @JsonAlias("updated")
    @JsonProperty(PREFIX + ":updated")
    String getPlUpdated();
    void setPlUpdated(String updated);

}
