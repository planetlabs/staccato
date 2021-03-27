package com.planet.staccato.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.planet.staccato.dto.api.extensions.FieldsExtension;
import com.planet.staccato.dto.api.extensions.SortExtension;
import com.planet.staccato.model.Item;
import lombok.Data;

/**
 * Model of convenience for representing the parameters in a api request.
 *
 * @author joshfix
 * Created on 12/7/17
 */
@Data
@SingleGeometry
public class SearchRequest {

    // core fields
    private String next;
    private double[] bbox;
    private Object intersects;
    private String datetime;
    private Integer limit = 10;
    private String[] ids;
    private String[] collections;

    // helper field
    private String method;

    // fields extension
    private FieldsExtension fields;

    // sort extension
    private SortExtension sortby;

    // STACQL -- not supported
    //private String query;

    // OAF CQL
    private String filter;
    @JsonProperty("filter-lang")
    private FilterLangEnum filterLang;
    @JsonProperty("filter-crs")
    private String filterCrs;

    public SearchRequest bbox(double[] bbox) {
        setBbox(bbox);
        return this;
    }

    public SearchRequest datetime(String time) {
        setDatetime(time);
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

    public SearchRequest sortby(SortExtension sortby) {
        setSortby(sortby);
        return this;
    }

    public SearchRequest intersects(Object intersects) {
        setIntersects(intersects);
        return this;
    }

    public SearchRequest method(String method) {
        setMethod(method);
        return this;
    }

    public SearchRequest filter(String filter) {
        setFilter(filter);
        return this;
    }

    public SearchRequest filterCrs(String filterCrs) {
        setFilterCrs(filterCrs);
        return this;
    }

    public SearchRequest filterLang(FilterLangEnum filterLang) {
        setFilterLang(filterLang);
        return this;
    }

    public enum FilterLangEnum {
        CQL_TEXT("cql-text"),
        CQL_JSON("cql-json");

        private String value;

        FilterLangEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static FilterLangEnum fromValue(String text) {
            for (FilterLangEnum b : FilterLangEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }
}

