package com.planet.staccato.extension;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @author joshfix
 * Created on 2019-09-16
 */
public interface Label {

    String EXTENSION_PREFIX = "label";

    @JsonProperty(EXTENSION_PREFIX + ":property")
    Set<String> getProperties();
    void setProperties(Set<String> properties);

    @JsonProperty(EXTENSION_PREFIX + ":classes")
    LabelClass getClasses();
    void setLabelClass(LabelClass labelClass);

    @JsonProperty(EXTENSION_PREFIX + ":description")
    String getDescription();
    void setDescription(String description);

    @JsonProperty(EXTENSION_PREFIX + ":type")
    LabelType getType();
    void setType(LabelType type);

    @JsonProperty(EXTENSION_PREFIX + ":task")
    Set<String> getTask();
    void setTask(Set<String> task);

    @JsonProperty(EXTENSION_PREFIX + ":method")
    Set<String> getMethod();
    void setMethod(Set<String> method);

    @JsonProperty(EXTENSION_PREFIX + ":overview")
    LabelOverview getOverview();
    void setLabelOverview(LabelOverview overview);

    enum LabelType {
        VECTOR("vector"),
        RASTER("raster");

        private String value;

        LabelType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static LabelType fromValue(String text) {
            for (LabelType b : LabelType.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @Data
    class Count {
        private String name;
        private Integer count;
    }

    @Data
    class LabelOverview {
        @JsonProperty("property_key")
        private String propertyKey;
        List<Count> counts;
        List<Stats> statistics;
    }

    @Data
    class Stats {
        private String name;
        private Double value;
    }

    @Data
    class LabelClass {
        private String name;
        private List<Object> classes;
    }
}
