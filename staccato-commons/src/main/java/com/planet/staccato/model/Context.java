package com.planet.staccato.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author joshfix
 * Created on 2019-06-05
 */
@Data
public class Context {

    // limit is special because in the event that the value does not get set, the spec says it must return null
    @JsonInclude
    private Long limit;
    private long matched;
    private long returned;

    public Context limit(long limit) {
        setLimit(limit);
        return this;
    }

    public Context matched(long matched) {
        setMatched(matched);
        return this;
    }

    public Context returned(long returned) {
        setReturned(returned);
        return this;
    }

}


