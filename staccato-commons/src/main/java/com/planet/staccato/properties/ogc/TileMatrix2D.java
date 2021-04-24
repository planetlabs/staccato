package com.planet.staccato.properties.ogc;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.planet.staccato.elasticsearch.annotation.Mapping;
import com.planet.staccato.elasticsearch.annotation.MappingType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

/**
 * Defines the OGC Two Dimensional Tile Matrix Set JSON schema for use in the Tiled Assets STAC extension
 * @see <a href="https://github.com/radiantearth/stac-spec/tree/master/extensions/tiled-assets">Tiled Assets Extension</a>
 * @see <a href="http://schemas.opengis.net/tms/1.0/json/tms-schema.json">OGC Two Dimensional Tile Matrix Set JSON schema</a>
 * @author joshfix
 * Created on 06/08/2020
 */
public interface TileMatrix2D {

    @NotBlank
    @Mapping(type = MappingType.KEYWORD)
    @JsonProperty("type")
    TileMatrixSetType getType();
    void setType(TileMatrixSetType type);

    @Mapping(type = MappingType.TEXT)
    @JsonProperty("title")
    String getTitle();
    void setTitle(String title);

    @Mapping(type = MappingType.TEXT)
    @JsonProperty("abstract")
    String getAbstract();
    void setAbstract(String abstractText);

    @Mapping(type = MappingType.KEYWORD)
    @JsonProperty("keywords")
    Set<String> getKeywords();
    void setKeywords(Set<String> keywords);

    @NotBlank
    @Mapping(type = MappingType.KEYWORD)
    @JsonProperty("identifier")
    String getIdentifier();
    void setIdentifier(String identifier);

    @Mapping(type = MappingType.GEO_SHAPE)
    @JsonProperty("bbox")
    BoundingBox getBoundingBox();
    void setBoundingBox(BoundingBox boundingBox);

    @NotBlank
    @Mapping(type = MappingType.KEYWORD)
    @JsonProperty("supportedCRS")
    String getSupportedCrs();
    void setSupportedCrs(String crs);

    @Mapping(type = MappingType.KEYWORD)
    @JsonProperty("wellKnownScaleSet")
    String getWellKnownScaleSet();
    void setWellKnownScaleSet(String wellKnownScaleSet);

    @NotBlank
    @JsonProperty("tileMatrix")
    List<TileMatrix> getTileMatrix();
    void setTileMatrix(List<TileMatrix> tileMatrix);

    enum TileMatrixSetType {
        TILE_MATRIX_SET_TYPE("TileMatrixSetType");

        private String value;

        TileMatrixSetType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static TileMatrixSetType fromValue(String text) {
            for (TileMatrixSetType b : TileMatrixSetType.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }
    
    @Data
    class BoundingBox {
        private BoundingBoxType type;
        private List<Double> lowerCorner;
        private List<Double> upperCorner;

        enum BoundingBoxType {
            BOUNDING_BOX_TYPE("BoundingBoxType");

            private String value;

            BoundingBoxType(String value) {
                this.value = value;
            }

            public String getValue() {
                return value;
            }

            @Override
            public String toString() {
                return String.valueOf(value);
            }

            public static TileMatrix2D.BoundingBox.BoundingBoxType fromValue(String text) {
                for (TileMatrix2D.BoundingBox.BoundingBoxType b : TileMatrix2D.BoundingBox.BoundingBoxType.values()) {
                    if (String.valueOf(b.value).equals(text)) {
                        return b;
                    }
                }
                return null;
            }
        }
    }
    
    @Data
    class TileMatrix {
        @NotBlank
        private TileMatrixType type;
        private String description;
        @JsonProperty("abstract")
        private String abstractText;
        private Set<String> keywords;
        @NotBlank
        private String identifier;
        @NotBlank
        private Double scaleDenominator;
        @NotBlank
        private List<Double> topLeftCorner;
        @NotBlank
        private Integer tileWidth;
        @NotBlank
        private Integer tileHeight;
        @NotBlank
        private Integer matrixWidth;
        @NotBlank
        private Integer matrixHeight;
        private List<VariableMatrixWidth> variableMatrixWidth;

        enum TileMatrixType {
            TILE_MATRIX_TYPE("TileMatrixType");

            private String value;

            TileMatrixType(String value) {
                this.value = value;
            }

            public String getValue() {
                return value;
            }

            @Override
            public String toString() {
                return String.valueOf(value);
            }

            public static TileMatrix2D.TileMatrix.TileMatrixType fromValue(String text) {
                for (TileMatrix2D.TileMatrix.TileMatrixType b : TileMatrix2D.TileMatrix.TileMatrixType.values()) {
                    if (String.valueOf(b.value).equals(text)) {
                        return b;
                    }
                }
                return null;
            }
        }
    }
    
    @Data
    class VariableMatrixWidth {

        @NotBlank
        private VariableMatrixWidthType type;
        @NotBlank
        private Double coalesce;
        @NotBlank
        private Double minTileRow;
        @NotBlank
        private Double maxTileRow;

        enum VariableMatrixWidthType {
            VARIABLE_MATRIX_WIDTH_TYPE("VariableMatrixWidthType");

            private String value;

            VariableMatrixWidthType(String value) {
                this.value = value;
            }

            public String getValue() {
                return value;
            }

            @Override
            public String toString() {
                return String.valueOf(value);
            }

            public static TileMatrix2D.VariableMatrixWidth.VariableMatrixWidthType fromValue(String text) {
                for (TileMatrix2D.VariableMatrixWidth.VariableMatrixWidthType b : TileMatrix2D.VariableMatrixWidth.VariableMatrixWidthType.values()) {
                    if (String.valueOf(b.value).equals(text)) {
                        return b;
                    }
                }
                return null;
            }
        }
    }
}


