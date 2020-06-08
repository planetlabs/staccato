package com.planet.staccato.properties.extension;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.planet.staccato.properties.ogc.TileMatrix2D;
import lombok.Data;

import java.util.Map;

/**
 * Defines the tiled assets extension and Jackson property values
 * @see <a href="https://github.com/radiantearth/stac-spec/tree/master/extensions/tiled-assets#item-collection-and-catalog-properties">Tiled Assets Extension</a>
 * @author joshfix
 * Created on 06/08/2020
 */
public interface TiledAssetsSets {

    String EXTENSION_PREFIX = "tiles";

    @JsonProperty(EXTENSION_PREFIX + ":tile_matrix_sets")
    Map<String, TileMatrix2D> getTileMatrixSets();
    void setTileMatrixSets(Map<String, TileMatrix2D> tileMatrixSets);

    @Data
    class TileMatrixSetLink {
        private String url;
        @JsonProperty(EXTENSION_PREFIX + ":well_known_scale_set")
        private String wellKnownScaleSet;
    }
}
