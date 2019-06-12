package com.planet.staccato.dto.api.extensions;

import lombok.Data;

import java.util.ArrayList;

/**
 * @author joshfix
 * Created on 2019-06-11
 */
@Data
public class SortExtension extends ArrayList<SortExtension.SortTerm> {

    @Data
    public static class SortTerm {
        private String field;
        private SortDirection direction;

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
                    if (String.valueOf(b.value).equals(text)) {
                        return b;
                    }
                }
                return null;
            }
        }
    }
}
