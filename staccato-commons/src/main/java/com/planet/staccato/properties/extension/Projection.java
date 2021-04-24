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

    String EXTENSION_PREFIX = "proj";

    @Mapping(type = MappingType.INTEGER)
    @JsonProperty(EXTENSION_PREFIX + ":epsg")
    Integer getEpsg();
    void setEpsg(Integer epsg);

    @Mapping(type = MappingType.KEYWORD)
    @JsonProperty(EXTENSION_PREFIX + ":wkt2")
    String getWkt2();
    void setWkt2(String wkt2);

    @JsonProperty(EXTENSION_PREFIX + ":projjson")
    Object getProjjson();
    void setProjjson(String projjson);

    @JsonProperty(EXTENSION_PREFIX + ":geometry")
    Object getGeometry();
    void setGeometry(Object geometry);

    @JsonProperty(EXTENSION_PREFIX + ":bbox")
    List<Double> getBbox();
    void setBbox(List<Double> bbox);

    @Mapping(type = MappingType.GEO_POINT)
    @JsonProperty(EXTENSION_PREFIX + ":centroid")
    Centroid getCentroid();
    void setCentroid(Centroid centroid);

    @Mapping(type = MappingType.INTEGER)
    @JsonProperty(EXTENSION_PREFIX + ":shape")
    List<Integer> getShape();
    void setShape(List<Integer> shape);

    @JsonProperty(EXTENSION_PREFIX + ":transform")
    List<Double> getTransform();
    void setTransform(List<Double> transform);
}
