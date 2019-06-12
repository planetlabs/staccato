package com.planet.staccato.dto.api;

import com.planet.staccato.dto.api.extensions.FieldsExtension;
import lombok.Data;

/**
 * Model of convinience for representing the parameters in a api request.
 *
 * @author joshfix
 * Created on 12/7/17
 */
@Data
public class SearchRequest {

    // core fieldsExtension
    private double[] bbox;
    private Object intersects;
    private String time;
    private Integer limit = 10;
    private Integer page = 1;
    private String[] ids;
    private String[] collections;

    // fields extension
    private FieldsExtension fields;
    private String query;


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

    public SearchRequest fields(FieldsExtension fields) {
        setFields(fields);
        return this;
    }

    public SearchRequest intersects(Object intersects) {
        setIntersects(intersects);
        return this;
    }
}
