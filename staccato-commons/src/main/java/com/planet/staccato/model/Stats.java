package com.planet.staccato.model;

import lombok.Data;

/**
 * @author joshfix
 * Created on 2019-09-16
 */
@Data
public class Stats {

    private Object min;
    private Object max;

    public Stats min(Object min) {
        setMin(min);
        return this;
    }

    public Stats max(Object max) {
        setMax(max);
        return this;
    }
}
