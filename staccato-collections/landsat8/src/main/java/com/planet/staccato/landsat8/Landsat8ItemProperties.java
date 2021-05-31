package com.planet.staccato.landsat8;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.planet.staccato.model.Provider;
import com.planet.staccato.properties.CoreProperties;
import com.planet.staccato.properties.extension.EO;
import com.planet.staccato.properties.extension.View;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author joshfix
 * Created on 1/12/18
 */
@Data
@JsonTypeName("landsat-8-l1")
@JsonDeserialize(as = Landsat8ItemProperties.class)
public class Landsat8ItemProperties implements CoreProperties, EO, View, Landsat8 {

    // CoreProperties
    private String datetime;
    private String startDatetime;
    private String endDatetime;
    private String created;
    private String updated;
    private String title;
    private String license;
    private String platform;
    private String mission;
    private String constellation;
    private Double gsd;
    private List<Provider> providers;
    private List<String> instruments;

    // Collection field as part of the commons extension (merged from the collection metadata)
    //private String collection = Landsat8CollectionMetadata.ID;

    // EO
    private Double cloudCover;
    //private Double gsd;
    private List<Band> bands;

    // View
    private Double offNadir;
    private Double incidenceAngle;
    private Double azimuth;
    private Double sunAzimuth;
    private Double sunElevation;

    // Landsat8
    @Min(0)
    @Max(251)
    private Integer wrsPath;
    @Min(0)
    @Max(119)
    private Integer wrsRow;
    @Min(0)
    private Integer imageQualityTirs;
    @Min(0)
    private Integer imageQualityOli;
    @Min(0)
    private Double earthSunDistance;
    private String sceneId;
    private String productId;
    private String processingLevel;

}

