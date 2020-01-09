package com.planet.staccato.properties.extension;

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

    @JsonProperty(EXTENSION_PREFIX + ":properties")
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

    @JsonProperty(EXTENSION_PREFIX + ":tasks")
    Set<String> getTasks();
    void setTasks(Set<String> tasks);

    @JsonProperty(EXTENSION_PREFIX + ":methods")
    Set<String> getMethods();
    void setMethods(Set<String> methods);

    @JsonProperty(EXTENSION_PREFIX + ":overviews")
    LabelOverview getOverviews();
    void setLabelOverviews(LabelOverview overviews);

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
