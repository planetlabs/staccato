package com.boundlessgeo.staccato.model;

import com.boundlessgeo.staccato.extension.Collection;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Item
 */
@Data
public class Item<T extends Collection & MandatoryProperties> {

    private String id;
    private Bbox bbox;
    private Object geometry;
    private Centroid centroid;
    private TypeEnum type;
    private T properties;
    private Set<Link> links;
    protected Map<String, Asset> assets = new HashMap<>();

    public Item assets(Map<String, Asset> assets) {
        setAssets(assets);
        return this;
    }

    public Item id(String id) {
        setId(id);
        return this;
    }

    public Item bbox(Bbox bbox) {
        setBbox(bbox);
        return this;
    }

    public Item geometry(Object geometry) {
        setGeometry(geometry);
        return this;
    }

    public Item centroid(Centroid centroid) {
        setCentroid(centroid);
        return this;
    }

    public Item type(TypeEnum type) {
        setType(type);
        return this;
    }

    public Item properties(T properties) {
        setProperties(properties);
        return this;
    }

    public Item links(Set<Link> links) {
        setLinks(links);
        return this;
    }


    /**
     * The GeoJSON roles
     */
    public enum TypeEnum {
        FEATURE("Feature");

        private String value;

        TypeEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static TypeEnum fromValue(String text) {
            for (TypeEnum b : TypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Item item = (Item) o;
        return Objects.equals(this.id, item.id) &&
                Objects.equals(this.bbox, item.bbox) &&
                Objects.equals(this.geometry, item.geometry) &&
                Objects.equals(this.type, item.type) &&
                Objects.equals(this.properties, item.properties) &&
                Objects.equals(this.links, item.links);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bbox, geometry, type, properties, links);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Item {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    bbox: ").append(toIndentedString(bbox)).append("\n");
        sb.append("    geometry: ").append(toIndentedString(geometry)).append("\n");
        sb.append("    roles: ").append(toIndentedString(type)).append("\n");
        sb.append("    properties: ").append(toIndentedString(properties)).append("\n");
        sb.append("    links: ").append(toIndentedString(links)).append("\n");
        sb.append("    assets: ").append(toIndentedString(assets)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

}

