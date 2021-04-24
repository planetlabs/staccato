package com.planet.staccato.queryables;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collection;

@Data
@Accessors(chain = true)
public class QueryableProperty {

    private String title;
    private String type;
    private Integer minLength;
    private Integer maxLength;
    @JsonProperty("enum")
    private Collection<Object> enumCollection;
    private Double multipleOf;
    private Number minimum;
    private Number maximum;
    private Number exclusiveMinimum;
    private Number exclusiveMaximum;
    @JsonProperty("$ref")
    private String ref;
    private TemporalFormatEnum format;


    public enum TemporalFormatEnum {
        DATE_TIME("date-time"),
        DATE("date"),
        TIME("time"),
        DURATION("duration");

        private String value;

        TemporalFormatEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static TemporalFormatEnum fromValue(String text) {
            for (TemporalFormatEnum b : TemporalFormatEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

}
