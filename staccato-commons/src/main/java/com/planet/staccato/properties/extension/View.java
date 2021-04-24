package com.planet.staccato.properties.extension;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.planet.staccato.elasticsearch.annotation.Mapping;
import com.planet.staccato.elasticsearch.annotation.MappingType;

/**
 * Defines the View Extension and Jackson property values
 * @see <a href="https://github.com/stac-extensions/view/">View Extension</a>
 * @author joshfix
 * Created on 4/24/2021
 */
public interface View {
    String EXTENSION_PREFIX = "view";

    @Mapping(type = MappingType.INTEGER)
    @JsonProperty(EXTENSION_PREFIX + ":off_nadir")
    @JsonAlias("eo:off_nadir")  // temp workaround - need to modify data in elasticsearch
    Double getOffNadir();
    void setOffNadir(Double offNadir);

    @Mapping(type = MappingType.DOUBLE)
    @JsonProperty(EXTENSION_PREFIX + ":incidence_angle")
    Double getIncidenceAngle();
    void setIncidenceAngle(Double incidenceAngle);

    @Mapping(type = MappingType.DOUBLE)
    @JsonProperty(EXTENSION_PREFIX + ":azimuth")
    @JsonAlias("eo:azimuth")  // temp workaround - need to modify data in elasticsearch
    Double getAzimuth();
    void setAzimuth(Double azimuth);

    @Mapping(type = MappingType.DOUBLE)
    @JsonProperty(EXTENSION_PREFIX + ":sun_azimuth")
    @JsonAlias("eo:sun_azimuth")  // temp workaround - need to modify data in elasticsearch
    Double getSunAzimuth();
    void setSunAzimuth(Double sunAzimuth);

    @Mapping(type = MappingType.DOUBLE)
    @JsonProperty(EXTENSION_PREFIX + ":sun_elevation")
    @JsonAlias("eo:sun_elevation")  // temp workaround - need to modify data in elasticsearch
    Double getSunElevation();
    void setSunElevation(Double sunElevation);
}
