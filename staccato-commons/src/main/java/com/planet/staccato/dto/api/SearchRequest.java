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
    // cql-text is the default filter-lang for Staccato. not providing a filter-lang value should default to cql-text.
    @JsonProperty("filter-lang")
    private String filterLang = DEFAULT_FILTER_LANG;
    @JsonProperty("filter-crs")
    private String filterCrs = DEFAULT_FILTER_CRS;

    public static final String DEFAULT_FILTER_CRS = "http://www.opengis.net/def/crs/OGC/1.3/CRS84";
    public static final String DEFAULT_FILTER_LANG = FilterLangEnum.CQL_TEXT.getValue();

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

    public void setFilterCrs(String filterCrs) {
        this.filterCrs = (filterCrs == null || filterCrs.isBlank()) ? DEFAULT_FILTER_CRS : filterCrs;
    }

    public SearchRequest filterLang(String filterLang) {
        setFilterLang(filterLang);
        return this;
    }

    public void setFilterLang(String filterLang) {
        this.filterLang = (filterLang == null || filterLang.isBlank()) ? DEFAULT_FILTER_LANG : filterLang;
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
            //return FilterLangEnum.CQL_TEXT;
            return null;
        }
    }
}

