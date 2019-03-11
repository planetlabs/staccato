package com.planet.staccato.planet;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author joshfix
 * Created on 4/18/18
 */
public interface Planet {

    @JsonAlias("ground_control")
    @JsonProperty("pl:ground_control")
    Boolean getGroundControl();
    void setGroundControl(Boolean groundControl);

    @JsonAlias("item_type")
    @JsonProperty("pl:item_type")
    String getItemType();
    void setItemType(String itemType);

    @JsonAlias("provider")
    @JsonProperty("pl:provider")
    String getProvider();
    void setProvider(String provider);

    @JsonAlias("published")
    @JsonProperty("pl:published")
    String getPublished();
    void setPublished(String published);

    @JsonAlias("quality_category")
    @JsonProperty("pl:quality_category")
    String getQualityCategory();
    void setQualityCategory(String quailtyCategory);

    @JsonAlias("satellite_azimuth")
    @JsonProperty("pl:satellite_azimuth")
    Double getSatelliteAzimuth();
    void setSatelliteAzimuth(Double satelliteAzimuth);

    @JsonAlias("satellite_id")
    @JsonProperty("pl:satellite_id")
    String getSatelliteId();
    void setSatelliteId(String satelliteId);

    @JsonAlias("strip_id")
    @JsonProperty("pl:strip_id")
    String getStripId();
    void setStripId(String stripId);

    @JsonAlias("updated")
    @JsonProperty("pl:updated")
    String getUpdated();
    void setUpdated(String updated);

}
