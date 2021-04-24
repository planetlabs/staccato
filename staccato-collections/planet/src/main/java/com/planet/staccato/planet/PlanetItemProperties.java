package com.planet.staccato.planet;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.planet.staccato.elasticsearch.annotation.Mapping;
import com.planet.staccato.elasticsearch.annotation.MappingType;
import com.planet.staccato.model.Provider;
import com.planet.staccato.properties.CoreProperties;
import com.planet.staccato.properties.extension.EO;
import com.planet.staccato.properties.extension.View;
import lombok.Data;

import java.util.List;

/**
 * @author joshfix
 * Created on 1/12/18
 */
@Data
@JsonTypeName("pl")
public class PlanetItemProperties implements CoreProperties, EO, View, Planet {

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
    //private String collection = PlanetCollectionMetadata.ID;

    // EO
    private String instrument;
    private Double cloudCover;
    private String epsg;
    private List<Band> bands;


    // View
    private Double offNadir;
    private Double incidenceAngle;
    private Double azimuth;
    private Double sunAzimuth;
    private Double sunElevation;

    // Planet
    private Boolean groundControl;
    private String itemType;
    private String provider;
    private String published;
    private String qualityCategory;
    private Double satelliteAzimuth;
    @Mapping(type = MappingType.KEYWORD)
    private String satelliteId;
    @Mapping(type = MappingType.KEYWORD)
    private String stripId;
    @Mapping(type = MappingType.DATE)
    private String plUpdated;

}

