package com.boundlessgeo.staccato.landsat8;

import com.boundlessgeo.staccato.extension.Collection;
import com.boundlessgeo.staccato.extension.EO;
import com.boundlessgeo.staccato.model.MandatoryProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.util.List;

/**
 * @author joshfix
 * Created on 1/12/18
 */
@Data
@JsonTypeName("landsat-8-l1")
@JsonDeserialize(as = Landsat8ItemProperties.class)
public class Landsat8ItemProperties implements MandatoryProperties, Collection, EO, Landsat8 {

    // MandatoryProperties
    private String datetime;

    // Collection field as part of the commons extension (merged from the collection metadata)
    private String collection = Landsat8CollectionMetadata.ID;

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
    private String wrsRow;
    private String wrsPath;
    private Double earthSunDistance;
    private Double geometricRmseModel;
    private Double groundControlPointsVerify;
    private String groundControlPointsModel;
    private String imageQualityTirs;
    private String geometricRmseModelX;
    private String geometricRmseModelY;
    private String geometricRmseVerify;
    private String imageQualityOli;
    private String entityId;
    private String processingLevel;
}

