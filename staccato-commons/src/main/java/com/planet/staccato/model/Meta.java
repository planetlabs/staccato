package com.planet.staccato.model;

import lombok.Data;

/**
 * @author joshfix
 * Created on 2019-06-05
 */
@Data
public class Meta {
    private int page;
    private int limit;
    private int found;
    private int returned;

    public Meta page(int page) {
        setPage(page);
        return this;
    }

    public Meta limit(int limit) {
        setLimit(limit);
        return this;
    }

    public Meta found(int found) {
        setFound(found);
        return this;
    }

    public Meta returned(int returned) {
        setReturned(returned);
        return this;
    }

}


