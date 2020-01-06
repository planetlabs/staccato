package com.planet.staccato.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.*;

/**
 * ItemCollection
 */
@Data
public class ItemCollection {

    private TypeEnum type;
    @JsonProperty("stac_version")
    private String stacVersion;
    @JsonProperty("stac_extensions")
    private Set<String> stacExtensions;
    @JsonProperty("search:metadata")
    private SearchMetadata metadata;
    private List<Item> features = new ArrayList<>();
    private List<Link> links;
    private long numberMatched;
    private long numberReturned;

    public enum TypeEnum {
        FEATURECOLLECTION("FeatureCollection");

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

    public ItemCollection type(TypeEnum type) {
        this.type = type;
        return this;
    }

    public ItemCollection features(List<Item> features) {
        this.features = features;
        return this;
    }

    public ItemCollection addFeaturesItem(Item featuresItem) {
        this.features.add(featuresItem);
        return this;
    }

    public ItemCollection links(List<Link> links) {
        setLinks(links);
        return this;
    }

    public ItemCollection addLink(Link link) {
        if (links == null) {
            links = new ArrayList<>(1);
        }
        links.add(link);
        return this;
    }

    public ItemCollection metadata(SearchMetadata searchMetadata) {
        setMetadata(searchMetadata);
        return this;
    }

    public ItemCollection numberMatched(long numberMatched) {
        setNumberMatched(numberMatched);
        return this;
    }

    public ItemCollection numberReturned(long numberReturned) {
        setNumberReturned(numberReturned);
        return this;
    }

    public ItemCollection stacVersion(String stacVersion) {
        setStacVersion(stacVersion);
        return this;
    }

    public ItemCollection stacExtensions(Set<String> stacExtensions) {
        setStacExtensions(stacExtensions);
        return this;
    }

    public ItemCollection addStacExtension(String stacExtension) {
        if (null == this.stacExtensions) {
            this.stacExtensions = new HashSet<>();
        }
        this.stacExtensions.add(stacExtension);
        return this;
    }

    public ItemCollection addStacExtensions(Collection<String> stacExtensions) {
        if (null == this.stacExtensions) {
            this.stacExtensions = new HashSet<>();
        }
        this.stacExtensions.addAll(stacExtensions);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ItemCollection itemCollection = (ItemCollection) o;
        return Objects.equals(this.type, itemCollection.type)
                && Objects.equals(this.features, itemCollection.features)
                && Objects.equals(this.metadata, itemCollection.metadata)
                && Objects.equals(this.numberMatched, itemCollection.numberMatched)
                && Objects.equals(this.numberReturned, itemCollection.numberReturned)
                && Objects.equals(this.links, itemCollection.links)
                && Objects.equals(this.stacVersion, itemCollection.stacVersion)
                && Objects.equals(this.stacExtensions, itemCollection.stacExtensions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, features);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ItemCollection {\n");

        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    features: ").append(toIndentedString(features)).append("\n");
        sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
        sb.append("    numberMatched: ").append(toIndentedString(numberMatched)).append("\n");
        sb.append("    numberReturned: ").append(toIndentedString(numberReturned)).append("\n");
        sb.append("    links: ").append(toIndentedString(links)).append("\n");
        sb.append("    stacVersion: ").append(toIndentedString(stacVersion)).append("\n");
        sb.append("    stacExtensions: ").append(toIndentedString(String.join(",", stacExtensions))).append("\n");
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

