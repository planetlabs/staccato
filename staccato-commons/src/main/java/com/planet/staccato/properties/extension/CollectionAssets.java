package com.planet.staccato.properties.extension;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.planet.staccato.model.Asset;

import java.util.Map;

/**
 * Defines the Collection Assets Extension and Jackson property values.
 * @see <a href="https://github.com/radiantearth/stac-spec/tree/master/extensions/collection-assets">Collection Assets Extension</a>
 * @author joshfix
 * Created on 2020-06-08
 */
public interface CollectionAssets {

    @JsonProperty("assets")
    Map<String, Asset> getAssets();
    void setAssets(Map<String, Asset> assets);
}
