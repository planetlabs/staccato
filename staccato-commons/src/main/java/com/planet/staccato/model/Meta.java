package com.planet.staccato.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

/**
 * @author joshfix
 * Created on 2019-06-05
 */
@Data
public class Meta {

    // limit is special because in the event that the value does not get set, the spec says it must return null
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private Long limit;
    private long page;
    private long found;
    private long returned;
    private String next;

    public Meta page(long page) {
        setPage(page);
        return this;
    }

    public Meta limit(long limit) {
        setLimit(limit);
        return this;
    }

    public Meta found(long found) {
        setFound(found);
        return this;
    }

    public Meta returned(long returned) {
        setReturned(returned);
        return this;
    }

    public Meta next(String next) {
        setNext(next);
        return this;
    }

}


