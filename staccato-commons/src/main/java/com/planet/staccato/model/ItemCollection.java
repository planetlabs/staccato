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
    private Context context;
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
        if (link == null) {
            return this;
        }

        if (links == null) {
            links = new ArrayList<>(1);
        }
        links.add(link);
        return this;
    }

    public ItemCollection context(Context context) {
        setContext(context);
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
        if (stacExtensions == null) {
            return this;
        }

        if (null == this.stacExtensions) {
            this.stacExtensions = new HashSet<>();
        }
        this.stacExtensions.add(stacExtension);
        return this;
    }

    public ItemCollection addStacExtensions(Collection<String> stacExtensions) {
        if (stacExtensions == null) {
            return this;
        }

        if (null == this.stacExtensions) {
            this.stacExtensions = new HashSet<>();
        }
        this.stacExtensions.addAll(stacExtensions);
        return this;
    }

}

