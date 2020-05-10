package com.planet.staccato.landsat8;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.planet.staccato.collection.Subcatalog;
import com.planet.staccato.elasticsearch.annotation.Mapping;
import com.planet.staccato.elasticsearch.annotation.MappingType;

/**
 * @author joshfix
 * Created on 4/18/18
 */
public interface Landsat8 {

    String NAME = "landsat";
    String PREFIX = "landsat";

    @Subcatalog
    @Mapping(type = MappingType.KEYWORD)
    @JsonAlias("wrs_path")
    @JsonProperty(PREFIX + ":wrs_path")
    Integer getWrsPath();
    void setWrsPath(Integer wrsPath);

    @Subcatalog
    @Mapping(type = MappingType.KEYWORD)
    @JsonAlias("wrs_row")
    @JsonProperty(PREFIX + ":wrs_row")
    Integer getWrsRow();
    void setWrsRow(Integer wrsRow);

    @JsonAlias("earth_sun_distance")
    @JsonProperty(PREFIX + ":earth_sun_distance")
    Double getEarthSunDistance();
    void setEarthSunDistance(Double earthSunDistance);

    @JsonAlias("image_quality_oli")
    @JsonProperty(PREFIX + ":image_quality_oli")
    Integer getImageQualityOli();
    void setImageQualityOli(Integer imageQualityOli);

    @JsonAlias("image_quality_tirs")
    @JsonProperty(PREFIX + ":image_quality_tirs")
    Integer getImageQualityTirs();
    void setImageQualityTirs(Integer imageQualityTirs);

    @Mapping(type = MappingType.KEYWORD)
    @JsonAlias("scene_id")
    @JsonProperty(PREFIX + ":scene_id")
    String getSceneId();
    void setSceneId(String sceneId);

    @Mapping(type = MappingType.KEYWORD)
    @JsonAlias("product_id")
    @JsonProperty(PREFIX + ":product_id")
    String getProductId();
    void setProductId(String productId);

    @Mapping(type = MappingType.KEYWORD)
    @JsonAlias("processingLevel")
    @JsonProperty(PREFIX + ":processingLevel")
    String getProcessingLevel();
    void setProcessingLevel(String processingLevel);
}
