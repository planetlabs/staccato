package com.planet.staccato.dto;

import lombok.Data;

/**
 * Model of convinience for representing the parameters in a api request.
 *
 * @author joshfix
 * Created on 12/7/17
 */
@Data
public class SearchRequest {

    private double[] bbox;
    private String time;
    private String query;
    private Integer limit;
    private Integer page;
    private String[] ids;
    private String[] collections;
    private String[] propertyname;
    private Object intersects;
}
