package com.planet.staccato.dto.api;

import com.planet.staccato.dto.api.extensions.FieldsExtension;
import com.planet.staccato.dto.api.extensions.SortExtension;
import lombok.Data;

/**
 * Model of convinience for representing the parameters in a api request.
 *
 * @author joshfix
 * Created on 12/7/17
 */
@Data
public class SearchRequest {

    // core fields
    private String next;
    private double[] bbox;
    private Object intersects;
    private String datetime;
    private Integer limit = 10;
    private String[] ids;
    private String[] collections;

    private String method;

    // fields extension
    private FieldsExtension fields;

    // sort extension
    private SortExtension sort;
    private String query;

    public SearchRequest bbox(double[] bbox) {
        setBbox(bbox);
        return this;
    }

    public SearchRequest datetime(String time) {
        setDatetime(time);
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

    public SearchRequest next(String next) {
        setNext(next);
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

    public SearchRequest fields(FieldsExtension fields) {
        setFields(fields);
        return this;
    }

    public SearchRequest sort(SortExtension sort) {
        setSort(sort);
        return this;
    }

    public SearchRequest intersects(Object intersects) {
        setIntersects(intersects);
        return this;
    }
}
