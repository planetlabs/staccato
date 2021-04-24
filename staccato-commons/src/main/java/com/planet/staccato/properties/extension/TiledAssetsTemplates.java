package com.planet.staccato.properties.extension;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.planet.staccato.model.Asset;

import java.util.Map;

/**
 * Defines the tiled assets extension and Jackson property values
 * @see <a href="https://github.com/radiantearth/stac-spec/tree/master/extensions/tiled-assets#item-fields">Tiled Assets Extension</a>
 * @author joshfix
 * Created on 06/08/2020
 */
public interface TiledAssetsTemplates {

    @JsonProperty("asset_templates")
    Map<String, Asset> getAssetTemplates();
    void setAssetTemplates(Map<String, Asset> assetTemplates);

}
