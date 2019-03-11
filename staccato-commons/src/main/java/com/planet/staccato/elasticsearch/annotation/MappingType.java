package com.planet.staccato.elasticsearch.annotation;

/**
 * @author joshfix
 * Created on 2/12/19
 */
public enum MappingType {
    KEYWORD("keyword"), TEXT("text"), DATE("date"), GEO_SHAPE("geo_shape"), GEO_POINT("geo_point"), BOOLEAN("boolean"),
    BINARY("binary"), INTEGER_RANGE("integer_range"), FLOAT_RANGE("float_range"), LONG_RANGE("long_range"),
    DOUBLE_RANGE("double_range"), DATE_RANGE("date_range"), OBJECT("object"), NESTED("nested"), IP("ip"),
    COMPLETION("completion"), TOKEN_COUNT("token_count"), MURMUR3("murmur3"), PERCOLATOR("percolator"), JOIN("join"),
    INTEGER("integer"), LONG("long"), SHORT("short"), BYTE("byte"), DOUBLE("double"), FLOAT("float"),
    HALF_FLOAT("half_float"), SCALED_FLOAT("scaled_float");

    private String value;

    MappingType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static MappingType fromValue(String text) {
        for (MappingType b : MappingType.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}