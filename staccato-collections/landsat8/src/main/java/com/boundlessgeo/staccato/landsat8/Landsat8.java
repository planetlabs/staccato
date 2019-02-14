package com.boundlessgeo.staccato.landsat8;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author joshfix
 * Created on 4/18/18
 */
public interface Landsat8 {

    @JsonAlias("wrs_path")
    @JsonProperty("landsat:wrs_path")
    String getWrsPath();
    void setWrsPath(String wrsPath);

    @JsonAlias("wrs_row")
    @JsonProperty("landsat:wrs_row")
    String getWrsRow();
    void setWrsRow(String wrsRow);

    @JsonAlias("earth_sun_distance")
    @JsonProperty("landsat:earth_sun_distance")
    Double getEarthSunDistance();
    void setEarthSunDistance(Double earthSunDistance);

    @JsonAlias("geometric_rmse_model")
    @JsonProperty("landsat:geometric_rmse_model")
    Double getGeometricRmseModel();
    void setGeometricRmseModel(Double geometricRmseModel);

    @JsonAlias("ground_control_points_verify")
    @JsonProperty("landsat:ground_control_points_verify")
    Double getGroundControlPointsVerify();
    void setGroundControlPointsVerify(Double groundControlPointsVerify);

    @JsonAlias("ground_control_points_model")
    @JsonProperty("landsat:ground_control_points_model")
    String getGroundControlPointsModel();
    void setGroundControlPointsModel(String groundControlPointsModel);

    @JsonAlias("image_quality_tirs")
    @JsonProperty("landsat:image_quality_tirs")
    String getImageQualityTirs();
    void setImageQualityTirs(String imageQualityTirs);

    @JsonAlias("geometric_rmse_model_x")
    @JsonProperty("landsat:geometric_rmse_model_x")
    String getGeometricRmseModelX();
    void setGeometricRmseModelX(String geometricRmseModelX);

    @JsonAlias("geometric_rmse_model_y")
    @JsonProperty("landsat:geometric_rmse_model_y")
    String getGeometricRmseModelY();
    void setGeometricRmseModelY(String geometricRmseModelY);

    @JsonAlias("geometric_rmse_verify")
    @JsonProperty("landsat:geometric_rmse_verify")
    String getGeometricRmseVerify();
    void setGeometricRmseVerify(String geometricRmseVerify);

    @JsonAlias("image_quality_oli")
    @JsonProperty("landsat:image_quality_oli")
    String getImageQualityOli();
    void setImageQualityOli(String imageQualityOli);

    @JsonAlias("entityId")
    @JsonProperty("landsat:entityId")
    String getEntityId();
    void setEntityId(String entityId);

    @JsonAlias("processingLevel")
    @JsonProperty("landsat:processingLevel")
    String getProcessingLevel();
    void setProcessingLevel(String processingLevel);
}
