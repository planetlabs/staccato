package com.planet.staccato;

import lombok.Getter;

/**
 * Helper class to reference JSON style paths and field names.
 *
 * @author joshfix
 * Created on 12/5/17
 */
@Getter
public class FieldName {

    public static final String ID = "id";
    public static final String BBOX = "bbox";
    public static final String GEOMETRY = "geometry";
    public static final String GEOMETRY_TYPE = "type";
    public static final String GEOMETRY_TYPE_FULL = GEOMETRY + "." + GEOMETRY_TYPE;
    public static final String COORDINATES = "coordinates";
    public static final String COORDINATES_FULL = GEOMETRY + "." + COORDINATES;
    public static final String PROPERTIES = "properties";
    public static final String DATETIME = "datetime";
    public static final String DATETIME_FULL = PROPERTIES + "." + DATETIME;
    public static final String LINKS = "links";
    public static final String ASSETS = "assets";
    public static final String NEXT_PAGE_TOKEN = "nextPageToken";
    public static final String CENTROID = "centroid";

}
