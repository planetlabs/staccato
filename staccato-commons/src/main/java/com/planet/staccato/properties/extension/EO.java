package com.planet.staccato.properties.extension;

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

    @Mapping(type = MappingType.DOUBLE)
    @JsonProperty(EXTENSION_PREFIX + ":cloud_cover")
    Double getCloudCover();
    void setCloudCover(Double cloudCover);

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

        @Mapping(type = MappingType.TEXT)
        private String description;

        @Mapping(type = MappingType.DOUBLE)
        @JsonProperty("center_wavelength")
        private Double centerWavelength;

        @Mapping(type = MappingType.DOUBLE)
        @JsonProperty("full_width_half_max")
        private Double fullWidthHalfMax;

        public static Band build() {
            return new Band();
        }

        public Band name(String name) {
            setName(name);
            return this;
        }

        public Band commonName(String commonName) {
            setCommmonName(commonName);
            return this;
        }

        public Band description(String description) {
            setDescription(description);
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
