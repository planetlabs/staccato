package com.planet.staccato.dto.api.extensions;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

/**
 * @author joshfix
 * Created on 2019-06-11
 */
@Data
public class SortExtension extends ArrayList<SortExtension.SortTerm> {

    @Data
    @AllArgsConstructor
    public static class SortTerm {
        private String field;
        private SortDirection direction;

        public SortTerm field(String field) {
            setField(field);
            return this;
        }

        public SortTerm direction(SortDirection direction) {
            setDirection(direction);
            return this;
        }

        public SortTerm direction(String direction) {
            setDirection(SortDirection.fromValue(direction));
            return this;
        }

        public enum SortDirection {
            ASC("asc"), DESC("desc");

            private String value;

            SortDirection(String value) {
                this.value = value;
            }

            public String getValue() {
                return value;
            }

            @Override
            public String toString() {
                return String.valueOf(value);
            }

            public static SortExtension.SortTerm.SortDirection fromValue(String text) {
                for (SortExtension.SortTerm.SortDirection b : SortExtension.SortTerm.SortDirection.values()) {
                    if (String.valueOf(b.value).equalsIgnoreCase(text)) {
                        return b;
                    }
                }
                return null;
            }
        }
    }
}
