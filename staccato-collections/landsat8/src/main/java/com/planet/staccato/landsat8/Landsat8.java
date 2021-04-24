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

    String EXTENSION_PREFIX = "landsat";

    @Subcatalog
    @Mapping(type = MappingType.KEYWORD)
    @JsonAlias("wrs_path")
    @JsonProperty(EXTENSION_PREFIX + ":wrs_path")
    Integer getWrsPath();
    void setWrsPath(Integer wrsPath);

    @Subcatalog
    @Mapping(type = MappingType.KEYWORD)
    @JsonAlias("wrs_row")
    @JsonProperty(EXTENSION_PREFIX + ":wrs_row")
    Integer getWrsRow();
    void setWrsRow(Integer wrsRow);

    @JsonAlias("earth_sun_distance")
    @JsonProperty(EXTENSION_PREFIX + ":earth_sun_distance")
    Double getEarthSunDistance();
    void setEarthSunDistance(Double earthSunDistance);

    @JsonAlias("image_quality_oli")
    @JsonProperty(EXTENSION_PREFIX + ":image_quality_oli")
    Integer getImageQualityOli();
    void setImageQualityOli(Integer imageQualityOli);

    @JsonAlias("image_quality_tirs")
    @JsonProperty(EXTENSION_PREFIX + ":image_quality_tirs")
    Integer getImageQualityTirs();
    void setImageQualityTirs(Integer imageQualityTirs);

    @Mapping(type = MappingType.KEYWORD)
    @JsonAlias("scene_id")
    @JsonProperty(EXTENSION_PREFIX + ":scene_id")
    String getSceneId();
    void setSceneId(String sceneId);

    @Mapping(type = MappingType.KEYWORD)
    @JsonAlias("product_id")
    @JsonProperty(EXTENSION_PREFIX + ":product_id")
    String getProductId();
    void setProductId(String productId);

    @Mapping(type = MappingType.KEYWORD)
    @JsonAlias({"processing_level", "landsat:processingLevel"})
    @JsonProperty(EXTENSION_PREFIX + ":processing_level")
    String getProcessingLevel();
    void setProcessingLevel(String processingLevel);
}
