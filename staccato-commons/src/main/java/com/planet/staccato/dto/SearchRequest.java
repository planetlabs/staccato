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

    public SearchRequest bbox(double[] bbox) {
        setBbox(bbox);
        return this;
    }

    public SearchRequest time(String time) {
        setTime(time);
        return this;
    }

    public SearchRequest query(String query) {
        setQuery(query);
        return this;
    }

    public SearchRequest limit(int limit) {
        setLimit(limit);
        return this;
    }

    public SearchRequest page(int page) {
        setPage(page);
        return this;
    }

    public SearchRequest ids(String[] ids) {
        setIds(ids);
        return this;
    }

    public SearchRequest collections(String[] collections) {
        setCollections(collections);
        return this;
    }

    public SearchRequest propertyname(String[] propertyname) {
        setPropertyname(propertyname);
        return this;
    }

    public SearchRequest intersects(Object intersects) {
        setIntersects(intersects);
        return this;
    }
}
