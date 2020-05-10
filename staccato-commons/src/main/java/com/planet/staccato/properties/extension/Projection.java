package com.planet.staccato.properties.extension;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.planet.staccato.elasticsearch.annotation.Mapping;
import com.planet.staccato.elasticsearch.annotation.MappingType;
import com.planet.staccato.model.Centroid;

import java.util.List;

/**
 * Projection extension: https://github.com/radiantearth/stac-spec/tree/master/extensions/projection
 *
 * @author joshfix
 * Created on 1/6/20
 */
public interface Projection {

    String PREFIX = "proj";

    @Mapping(type = MappingType.INTEGER)
    @JsonProperty(PREFIX + ":epsg")
    Integer getEpsg();
    void setEpsg(Integer epsg);

    @Mapping(type = MappingType.KEYWORD)
    @JsonProperty(PREFIX + ":proj4")
    String getProj4();
    void setProj4(String proj4);

    @Mapping(type = MappingType.KEYWORD)
    @JsonProperty(PREFIX + ":wkt2")
    String getWkt2();
    void setWkt2(String wkt2);

    @JsonProperty(PREFIX + ":projjson")
    Object getProjjson();
    void setProjjson(String projjson);

    @JsonProperty(PREFIX + ":geometry")
    Object getGeometry();
    void setGeometry(Object geometry);

    @JsonProperty(PREFIX + ":bbox")
    List<Double> getBbox();
    void setBbox(List<Double> bbox);

    @Mapping(type = MappingType.GEO_POINT)
    @JsonProperty(PREFIX + ":centroid")
    Centroid getCentroid();
    void setCentroid(Centroid centroid);

}
