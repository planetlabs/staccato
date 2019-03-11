package com.planet.staccato;

/**
 * Interface to define methods that must be provided in a custom initializer.  All Spring registered beans that
 * implement this interface will be executed during application startup.
 *
 * @author joshfix
 * Created on 1/18/18
 */
public interface StacInitializer {
    void init();
    String getName();
}
