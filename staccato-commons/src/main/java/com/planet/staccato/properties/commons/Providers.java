package com.planet.staccato.properties.commons;

import com.planet.staccato.model.Provider;

import java.util.List;

/**
 * Commons properties extension
 * https://github.com/radiantearth/stac-spec/tree/master/extensions/commons
 *
 * @author joshfix
 * Created on 1/8/20
 */
public interface Providers {

    List<Provider> getProviders();
    void setProviders(List<Provider> providers);
}
