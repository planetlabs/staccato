package com.boundlessgeo.staccato.planet;

import com.boundlessgeo.staccato.elasticsearch.annotation.Mapping;
import com.boundlessgeo.staccato.elasticsearch.annotation.MappingType;
import com.boundlessgeo.staccato.extension.Collection;
import com.boundlessgeo.staccato.extension.EO;
import com.boundlessgeo.staccato.model.MandatoryProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

import java.util.List;

/**
 * @author joshfix
 * Created on 1/12/18
 */
@Data
@JsonTypeName("pl")
public class PlanetItemProperties implements MandatoryProperties, EO, Planet, Collection {

    // MandatoryProperties
    private String datetime;

    // Collection field as part of the commons extension (merged from the collection metadata)
    private String collection = PlanetCollectionMetadata.ID;

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
    private String updated;

}

