package com.planet.staccato.landsat8;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.planet.staccato.extension.EO;
import com.planet.staccato.model.MandatoryProperties;
import lombok.Data;

import java.util.List;

/**
 * @author joshfix
 * Created on 1/12/18
 */
@Data
@JsonTypeName("landsat-8-l1")
@JsonDeserialize(as = Landsat8ItemProperties.class)
public class Landsat8ItemProperties implements MandatoryProperties, EO, Landsat8 {

    // MandatoryProperties
    private String datetime;

    // Collection field as part of the commons extension (merged from the collection metadata)
    //private String collection = Landsat8CollectionMetadata.ID;

    // EO
    private String platform;
    private String instrument;
    private Double cloudCover;
    private Integer offNadir;
    private Double gsd;
    private Double azimuth;
    private Double sunAzimuth;
    private Double sunElevation;
    private String epsg;
    private String constellation;
    private List<Band> bands;

    // Landsat8
    private Integer wrsPath;
    private Integer wrsRow;
    private Integer imageQualityTirs;
    private Integer imageQualityOli;
    private Double earthSunDistance;
    private String sceneId;
    private String productId;
    private String processingLevel;
}

