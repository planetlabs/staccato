package com.planet.staccato.landsat8;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.planet.staccato.extension.EO;
import com.planet.staccato.model.CoreProperties;
import com.planet.staccato.model.Provider;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @author joshfix
 * Created on 1/12/18
 */
@Data
@JsonTypeName("landsat-8-l1")
@JsonDeserialize(as = Landsat8ItemProperties.class)
public class Landsat8ItemProperties implements CoreProperties, EO, Landsat8 {

    // CoreProperties
    private String datetime;
    private String created;
    private String updated;
    private String title;
    private String license;
    private Set<Provider> providers;

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

