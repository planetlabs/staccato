package com.planet.staccato.properties.extension;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.planet.staccato.model.Asset;

import java.util.Map;

/**
 * Defines the item assets xtension and Jackson property values
 * @see <a href="https://github.com/radiantearth/stac-spec/tree/master/extensions/item-assets">Item Assets Extension</a>
 * @author joshfix
 * Created on 06/08/2020
 */

public interface ItemAssets {

    @JsonProperty("item_assets")
    Map<String, Asset> getItemAssets();
    void setItemAssets(Map<String, Asset> itemAssets);

}
