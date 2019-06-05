package com.planet.staccato.model;

import lombok.Data;

/**
 * @author joshfix
 * Created on 2019-06-05
 */
@Data
public class Meta {
    private long page;
    private long limit;
    private long found;
    private long returned;

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

}


