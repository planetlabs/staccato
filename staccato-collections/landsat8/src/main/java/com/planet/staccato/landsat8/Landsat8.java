package com.planet.staccato.landsat8;

import com.planet.staccato.collection.Subcatalog;
import com.planet.staccato.elasticsearch.annotation.Mapping;
import com.planet.staccato.elasticsearch.annotation.MappingType;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author joshfix
 * Created on 4/18/18
 */
public interface Landsat8 {

    @Subcatalog
    @Mapping(type = MappingType.KEYWORD)
    @JsonAlias("wrs_path")
    @JsonProperty("landsat:wrs_path")
    Integer getWrsPath();
    void setWrsPath(Integer wrsPath);

    @Subcatalog
    @Mapping(type = MappingType.KEYWORD)
    @JsonAlias("wrs_row")
    @JsonProperty("landsat:wrs_row")
    Integer getWrsRow();
    void setWrsRow(Integer wrsRow);

    @JsonAlias("earth_sun_distance")
    @JsonProperty("landsat:earth_sun_distance")
    Double getEarthSunDistance();
    void setEarthSunDistance(Double earthSunDistance);

    @JsonAlias("image_quality_oli")
    @JsonProperty("landsat:image_quality_oli")
    Integer getImageQualityOli();
    void setImageQualityOli(Integer imageQualityOli);

    @JsonAlias("image_quality_tirs")
    @JsonProperty("landsat:image_quality_tirs")
    Integer getImageQualityTirs();
    void setImageQualityTirs(Integer imageQualityTirs);

    @Mapping(type = MappingType.KEYWORD)
    @JsonAlias("scene_id")
    @JsonProperty("landsat:scene_id")
    String getSceneId();
    void setSceneId(String sceneId);

    @Mapping(type = MappingType.KEYWORD)
    @JsonAlias("product_id")
    @JsonProperty("landsat:product_id")
    String getProductId();
    void setProductId(String productId);

    @Mapping(type = MappingType.KEYWORD)
    @JsonAlias("processingLevel")
    @JsonProperty("landsat:processingLevel")
    String getProcessingLevel();
    void setProcessingLevel(String processingLevel);
}
