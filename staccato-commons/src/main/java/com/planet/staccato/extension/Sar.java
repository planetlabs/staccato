package com.planet.staccato.extension;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.planet.staccato.elasticsearch.annotation.Mapping;
import com.planet.staccato.elasticsearch.annotation.MappingType;
import lombok.Data;

import java.util.Collection;

/**
 * Defines the fieldsExtension and Jackson property values for fieldsExtension in the SAR extension.
 * @see <a href="https://github.com/radiantearth/stac-spec/tree/master/extensions/sar">SAR Extension</a>
 * @author joshfix
 * Created on 2019-05-15
 */
public interface Sar {

    String EXTENSION_PREFIX = "sar";

    @Mapping(type = MappingType.KEYWORD)
    @JsonProperty(EXTENSION_PREFIX + ":instrument_mode")
    String getInstrumentMode();
    void setInstrumentMode(String instrumentMode);

    @Mapping(type = MappingType.KEYWORD)
    @JsonProperty(EXTENSION_PREFIX + ":frequency_band")
    String getFrequencyBand();
    void setFrequencyBand(String frequencyBand);

    @Mapping(type = MappingType.DOUBLE)
    @JsonProperty(EXTENSION_PREFIX + ":center_wavelength")
    Double getCenterWavelength();
    void setCenterWavelength(Double centerWavelength);

    @Mapping(type = MappingType.DOUBLE)
    @JsonProperty(EXTENSION_PREFIX + ":center_frequency")
    Double getCenterFrequency();
    void setCenterFrequency(Double centerFrequency);

    @Mapping(type = MappingType.KEYWORD)
    @JsonProperty(EXTENSION_PREFIX + ":polarization")
    Collection<String> getPolarization();
    void setPolarization(Collection<String> polarization);

    @JsonProperty(EXTENSION_PREFIX + ":bands")
    Collection<Band> getBands();
    void setBands(Collection<Band> bands);

    @Mapping(type = MappingType.KEYWORD)
    @JsonProperty(EXTENSION_PREFIX + ":pass_direction")
    String getPassDirection();
    void setPassDirection(String passDirection);

    @Mapping(type = MappingType.KEYWORD)
    @JsonProperty(EXTENSION_PREFIX + ":type")
    String getType();
    void setType(String type);

    @Mapping(type = MappingType.DOUBLE)
    @JsonProperty(EXTENSION_PREFIX + ":resolution_range")
    Double getResolutionRange();
    void setResolutionRange(Double resolutionRange);

    @Mapping(type = MappingType.DOUBLE)
    @JsonProperty(EXTENSION_PREFIX + ":resolution_azimuth")
    Double getResolutionAzimuth();
    void setResolutionAzimuth(Double resolutionAzimuth);

    @Mapping(type = MappingType.DOUBLE)
    @JsonProperty(EXTENSION_PREFIX + ":pixel_spacing_range")
    Double getPixelSpacingRange();
    void setPixelSpacingRange(String pixelSpacingRange);

    @Mapping(type = MappingType.DOUBLE)
    @JsonProperty(EXTENSION_PREFIX + ":pixel_spacing_azimuth")
    Double getPixelSpacingAzimuth();
    void setPixelSpacingAzimuth(Double pixelSpacingAzimuth);

    @Mapping(type = MappingType.DOUBLE)
    @JsonProperty(EXTENSION_PREFIX + ":looks_range")
    Double getLooksRange();
    void setLooksRange(Double looksRange);

    @Mapping(type = MappingType.DOUBLE)
    @JsonProperty(EXTENSION_PREFIX + ":looks_azimuth")
    Double getLooksAzimuth();
    void setLooksAzimuth(Double looksAzimuth);

    @Mapping(type = MappingType.DOUBLE)
    @JsonProperty(EXTENSION_PREFIX + ":looks_equivalent_number")
    Double getLooksEquivalentNumber();
    void setLooksEquivalentNumber(Double looksEquivalentNumber);

    @Mapping(type = MappingType.KEYWORD)
    @JsonProperty(EXTENSION_PREFIX + ":observation_direction")
    String getObservationDirection();
    void setObservationDirection(String observationDirection);

    @Mapping(type = MappingType.INTEGER)
    @JsonProperty(EXTENSION_PREFIX + ":absolute_orbit")
    Integer getAbsoluteOrbit();
    void setAbsoluteOrbit(Integer absoluteOrbit);

    @Mapping(type = MappingType.INTEGER)
    @JsonProperty(EXTENSION_PREFIX + ":relative_orbit")
    Integer getRelativeOrbit();
    void setRelativeOrbit(Integer relativeOrbit);

    @Mapping(type = MappingType.DOUBLE)
    @JsonProperty(EXTENSION_PREFIX + ":incidence_angle")
    Double getIncidenceAngle();
    void setIncidenceAngle(Double incidenceAngle);

    @Data
    class Band {
        private String name;
        private String description;
        @JsonProperty("data_type")
        private String dataType;
        private String unit;
        private String polarization;
    }
}
