package com.planet.staccato.queryables;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.planet.staccato.properties.extension.EO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
public class Queryables {

    @JsonProperty("$schema")
    private String schema;

    @JsonProperty("id")
    private String id;

    private String title;
    private String type;
    private String description;
    private Map<String, QueryableProperty> properties = new HashMap<>();

    public static final String STRING_TYPE = "string";
    public static final String INTEGER_TYPE = "integer";
    public static final String FLOAT_TYPE = "float";
    public static final String OBJECT_TYPE = "object";
    public static final String BOOLEAN_TYPE = "boolean";

    public Queryables() {
        properties.put("datetime", new QueryableProperty()
                .setTitle("Date and time of the assets")
                .setType(STRING_TYPE)
                .setFormat(QueryableProperty.TemporalFormatEnum.DATE_TIME));

        properties.put("startDatetime", new QueryableProperty()
                .setTitle("Start date and time of the assets")
                .setType(STRING_TYPE)
                .setFormat(QueryableProperty.TemporalFormatEnum.DATE_TIME));

        properties.put("end_date_time", new QueryableProperty()
                .setTitle("End date and time of the assets")
                .setType(STRING_TYPE)
                .setFormat(QueryableProperty.TemporalFormatEnum.DATE_TIME));

        properties.put("created", new QueryableProperty()
                .setTitle("Date and time created in UTC")
                .setType(STRING_TYPE)
                .setFormat(QueryableProperty.TemporalFormatEnum.DATE_TIME));

        properties.put("updated", new QueryableProperty()
                .setTitle("Date and time updated")
                .setType(STRING_TYPE)
                .setFormat(QueryableProperty.TemporalFormatEnum.DATE_TIME));

        properties.put("title", new QueryableProperty()
                .setTitle("Title")
                .setType(STRING_TYPE)
                .setMinLength(1));

        properties.put("license", new QueryableProperty()
                .setTitle("License")
                .setType(STRING_TYPE)
                .setMinLength(1));

        properties.put("platform", new QueryableProperty()
                .setTitle("Title")
                .setType(STRING_TYPE)
                .setMinLength(1));

        properties.put("mission", new QueryableProperty()
                .setTitle("Title")
                .setType(STRING_TYPE)
                .setMinLength(1));

        properties.put("constellation", new QueryableProperty()
                .setTitle("Title")
                .setType(STRING_TYPE)
                .setMinLength(1));

        properties.put("gsd", new QueryableProperty()
                .setTitle("Ground sample distance")
                .setType(FLOAT_TYPE)
                .setMinimum(0.0));
    }

    public void addEOProperties() {
        properties.put("eo:cloud_cover", new QueryableProperty()
                .setTitle("Cloud cover")
                .setType(FLOAT_TYPE)
                .setMaximum(100.0)
                .setMinimum(0.0));

    }

    public void addViewProperties() {
        properties.put("view:offNadir", new QueryableProperty()
                .setTitle("Off Nadir")
                .setType(FLOAT_TYPE)
                .setMinimum(0.0)
                .setMaximum(90.0));

        properties.put("view:incidenceAngle", new QueryableProperty()
                .setTitle("Center incidence angle")
                .setType(FLOAT_TYPE)
                .setMinimum(0.0)
                .setMaximum(90.0));

        properties.put("view:azimuth", new QueryableProperty()
                .setTitle("Azimuth angle")
                .setType(FLOAT_TYPE)
                .setMinimum(0.0)
                .setMaximum(360.0));

        properties.put("view:sun_azimuth", new QueryableProperty()
                .setTitle("Sun Azimuth")
                .setType(FLOAT_TYPE)
                .setMinimum(0.0)
                .setMaximum(360.0));

        properties.put("view:sun_elevation", new QueryableProperty()
                .setTitle("Sun Elevation")
                .setType(FLOAT_TYPE)
                .setMinimum(-90.0)
                .setMaximum(90.0));
    }
}
