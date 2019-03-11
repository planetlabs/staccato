package com.planet.staccato.model;

import lombok.Data;

/**
 * @author joshfix
 * Created on 1/10/18
 */
@Data
public class Centroid {

    protected double lat;
    protected double lon;

    public Centroid lat(double lat) {
        setLat(lat);
        return this;
    }

    public Centroid lon(double lon) {
        setLon(lon);
        return this;
    }
}
