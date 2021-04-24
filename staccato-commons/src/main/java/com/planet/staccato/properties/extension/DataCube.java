package com.planet.staccato.properties.extension;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.Map;

/**
 * Defines the fieldsExtension and Jackson property values for fieldsExtension in the data cube extension.
 * @see <a href="https://github.com/radiantearth/stac-spec/tree/master/extensions/datacube">Data Cube Extension</a>
 * @author joshfix
 * Created on 2019-05-15
 */
public interface DataCube {

    String EXTENSION_PREFIX = "cube";

    @NotBlank
    @JsonProperty(EXTENSION_PREFIX + ":dimensions")
    Map<String, ? extends Dimension> getDimensions();
    void setDimensions(Map<String, ? extends Dimension> dimensions);

    @Data
    abstract class Dimension {
        private String type;
        private String description;
        private Collection extent;
        private Collection values;
    }

    @Data
    class HorizontalSpatialDimension extends Dimension {
        private String axis;
        private Double step;
        @JsonProperty("reference_system")
        private Object referenceSystem;
    }

    @Data
    class VerticalSpatialDimension extends Dimension {
        private String axis;
        private Double step;
        private String unit;
        @JsonProperty("reference_system")
        private Object referenceSystem;
    }

    @Data
    class TemporalDimension extends Dimension {
        private String step;
    }

    @Data
    class AdditionalDimension extends Dimension {
        private Double step;
        private String unit;
        @JsonProperty("reference_system")
        private String referenceSystem;
    }

}
