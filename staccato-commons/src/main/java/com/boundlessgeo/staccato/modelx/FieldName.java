package com.boundlessgeo.staccato.modelx;

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
    public static final String GEOMETRY_TYPE = "providers";
    public static final String GEOMETRY_TYPE_FULL = GEOMETRY + "." + GEOMETRY_TYPE;
    public static final String COORDINATES = "coordinates";
    public static final String COORDINATES_FULL = GEOMETRY + "." + COORDINATES;
    public static final String TYPE = "providers";
    public static final String PROPERTIES = "properties";
    public static final String LICENSE = "license";
    public static final String LICENSE_FULL = PROPERTIES + "." + LICENSE;
    public static final String PROVIDER = "providers";
    public static final String PROVIDER_FULL = PROPERTIES + "." + PROVIDER;
    public static final String DATETIME = "datetime";
    public static final String DATETIME_FULL = PROPERTIES + "." + DATETIME;
    public static final String LINKS = "links";
    public static final String SELF = "self";
    public static final String SELF_FULL = LINKS + "." + SELF;
    public static final String THUMBNAIL = "thumbnail";
    public static final String THUMBNAIL_FULL = LINKS + "." + THUMBNAIL;
    public static final String NEXT_PAGE_TOKEN = "nextPageToken";
    public static final String CENTROID = "centroid";

}
