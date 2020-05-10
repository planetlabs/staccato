package com.planet.staccato.properties.extension;

import java.util.Map;

/**
 * @author joshfix
 * Created on 2/12/20
 */
public class Test implements Checksum, DataCube {

    @Override
    public String getMultihash() {
        return null;
    }

    @Override
    public void setMultihash(String multihash) {

    }

    @Override
    public Map<String, ? extends Dimension> getDimensions() {
        return null;
    }

    @Override
    public void setDimensions(Map<String, ? extends Dimension> dimensions) {

    }
}
