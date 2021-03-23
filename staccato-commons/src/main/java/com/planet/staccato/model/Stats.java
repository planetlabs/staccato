package com.planet.staccato.model;

import lombok.Data;

/**
 * @author joshfix
 * Created on 2019-09-16
 */
@Data
public class Stats {

    private Object minimum;
    private Object maximum;

    public Stats minimum(Object min) {
        setMinimum(min);
        return this;
    }

    public Stats maximum(Object max) {
        setMaximum(max);
        return this;
    }
}
