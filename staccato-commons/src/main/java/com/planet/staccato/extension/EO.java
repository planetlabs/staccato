package com.planet.staccato.extension;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.planet.staccato.elasticsearch.annotation.Mapping;
import com.planet.staccato.elasticsearch.annotation.MappingType;
import lombok.Data;

import java.util.List;

/**
 * Defines the fieldsExtension and Jackson property values for fieldsExtension in the EO extension.
 * @see <a href="https://github.com/radiantearth/stac-spec/tree/master/extensions/eo">EO Extension</a>
 * @author joshfix
 * Created on 4/18/18
 */
public interface EO {

    String EXTENSION_PREFIX = "eo";

    @JsonProperty(EXTENSION_PREFIX + ":gsd")
    Double getGsd();
    void setGsd(Double gsd);

    @JsonProperty(EXTENSION_PREFIX + ":cloud_cover")
    Double getCloudCover();
    void setCloudCover(Double cloudCover);

    @JsonProperty(EXTENSION_PREFIX + ":off_nadir")
    Integer getOffNadir();
    void setOffNadir(Integer offNadir);

    @JsonProperty(EXTENSION_PREFIX + ":azimuth")
    Double getAzimuth();
    void setAzimuth(Double azimuth);

    @JsonProperty(EXTENSION_PREFIX + ":sun_azimuth")
    Double getSunAzimuth();
    void setSunAzimuth(Double sunAzimuth);

    @JsonProperty(EXTENSION_PREFIX + ":sun_elevation")
    Double getSunElevation();
    void setSunElevation(Double sunElevation);

    @Mapping(type = MappingType.KEYWORD)
    @JsonProperty(EXTENSION_PREFIX + ":epsg")
    String getEpsg();
    void setEpsg(String epsg);

    @JsonProperty(EXTENSION_PREFIX + ":bands")
    List<Band> getBands();
    void setBands(List<Band> bands);

    @Data
    class Band {

        @Mapping(type = MappingType.KEYWORD)
        private String name;

        @Mapping(type = MappingType.KEYWORD)
        @JsonProperty("common_name")
        private String commmonName;

        private String description;

        private Double gsd;

        private Double accuracy;

        @JsonProperty("center_wavelength")
        private Double centerWavelength;

        @JsonProperty("full_width_half_max")
        private Double fullWidthHalfMax;

        public static Band build() {
            return new Band();
        }

        @Mapping(type = MappingType.KEYWORD)
        public Band name(String name) {
            setName(name);
            return this;
        }

        @Mapping(type = MappingType.KEYWORD)
        public Band commonName(String commonName) {
            setCommmonName(commonName);
            return this;
        }

        public Band description(String description) {
            setDescription(description);
            return this;
        }

        public Band gsd(double gsd) {
            setGsd(gsd);
            return this;
        }

        public Band accuracy(double accuracy) {
            setAccuracy(accuracy);
            return this;
        }

        public Band centerWavelength(double centerWavelength) {
            setCenterWavelength(centerWavelength);
            return this;
        }

        public Band fullWidthHalfMax(double fullWidthHalfMax) {
            setFullWidthHalfMax(fullWidthHalfMax);
            return this;
        }

    }
}
