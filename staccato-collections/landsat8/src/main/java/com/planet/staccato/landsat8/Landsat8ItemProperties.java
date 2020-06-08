package com.planet.staccato.landsat8;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.planet.staccato.model.Provider;
import com.planet.staccato.properties.CoreProperties;
import com.planet.staccato.properties.extension.EO;
import lombok.Data;

import java.util.List;

/**
 * @author joshfix
 * Created on 1/12/18
 */
@Data
@JsonTypeName("landsat-8-l1")
@JsonDeserialize(as = Landsat8ItemProperties.class)
public class Landsat8ItemProperties implements CoreProperties, EO, Landsat8 {

    public static void main(String... args) {
        Landsat8ItemProperties l = new Landsat8ItemProperties();

    }

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
    private List<Provider> providers;
    private List<String> instruments;

    // Collection field as part of the commons extension (merged from the collection metadata)
    //private String collection = Landsat8CollectionMetadata.ID;

    // EO
    private Double cloudCover;
    private Integer offNadir;
    private Double gsd;
    private Double azimuth;
    private Double sunAzimuth;
    private Double sunElevation;
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

