package com.planet.staccato.properties.extension;

import lombok.Data;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author joshfix
 * Created on 2/12/20
 */
public interface StacExtension {

    default Collection<String> getExtensions() {
        return ExtensionCenter.get();
    }

}