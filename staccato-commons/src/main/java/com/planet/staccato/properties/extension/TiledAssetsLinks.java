package com.planet.staccato.properties.extension;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.planet.staccato.properties.ogc.TileMatrix2D;
import lombok.Data;

import java.util.Map;

/**
 * Defines the tiled assets extension and Jackson property values
 * @see <a href="https://github.com/radiantearth/stac-spec/tree/master/extensions/tiled-assets#item-properties">Tiled Assets Extension</a>
 * @author joshfix
 * Created on 06/08/2020
 */
public interface TiledAssetsLinks {

    String EXTENSION_PREFIX = "tiles";

    @JsonProperty(EXTENSION_PREFIX + ":tile_matrix_set_links")
    Map<String, TileMatrix2D> getTileMatrixSetLinks();
    void setTileMatrixSets(Map<String, TileMatrix2D> tileMatrixSets);

    @Data
    class TileMatrixSetLink {
        private String url;
        @JsonProperty(EXTENSION_PREFIX + ":well_known_scale_set")
        private String wellKnownScaleSet;
    }

    @Data
    class TileMatrixLimits {
        @JsonProperty("min_tile_row")
        private Double minTileRow;
        @JsonProperty("max_tile_row")
        private Double maxTileRow;
        @JsonProperty("min_tile_col")
        private Double minTileCol;
        @JsonProperty("max_tile_col")
        private Double maxTileCol;
    }

    @Data
    class PixelBuffer {
        private Double top;
        private Double left;
        private Double bottom;
        private Double right;
        @JsonProperty("border_top")
        private Double borderTop;
        @JsonProperty("border_left")
        private Double borderLeft;
        @JsonProperty("border_bottomw")
        private Double borderBottom;
        @JsonProperty("border_right")
        private Double borderRight;
    }
}
