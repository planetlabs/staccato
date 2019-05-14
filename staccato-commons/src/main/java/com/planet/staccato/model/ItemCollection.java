package com.planet.staccato.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ItemCollection
 */
public class ItemCollection {

    private TypeEnum type;
    private List<Item> features = new ArrayList<>();
    private String nextPageToken;
    private List<Link> links;
    //private ItemCollectionLinks links;


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

    /**
     * Get roles
     *
     * @return roles
     **/
    public TypeEnum getType() {
        return type;
    }

    public void setType(TypeEnum type) {
        this.type = type;
    }

    public ItemCollection features(List<Item> features) {
        this.features = features;
        return this;
    }

    public ItemCollection addFeaturesItem(Item featuresItem) {
        this.features.add(featuresItem);
        return this;
    }

    /**
     * Get api
     *
     * @return api
     **/
    public List<Item> getFeatures() {
        return features;
    }

    public void setItems(List<Item> features) {
        this.features = features;
    }

    public ItemCollection nextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
        return this;
    }

    /**
     * A token to obtain the page paginated data set
     *
     * @return nextPageToken
     **/
    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public List<Link> getLinks() {
        return links;
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
/*
  public ItemCollection links(ItemCollectionLinks links) {
    this.links = links;
    return this;
  }

  public ItemCollectionLinks getLinks() {
    return links;
  }

  public void setLinks(ItemCollectionLinks links) {
    this.links = links;
  }
*/

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ItemCollection itemCollection = (ItemCollection) o;
        return Objects.equals(this.type, itemCollection.type) &&
                Objects.equals(this.features, itemCollection.features) &&
                Objects.equals(this.nextPageToken, itemCollection.nextPageToken);// &&
        // Objects.equals(this.links, itemCollection.links);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, features, nextPageToken/*, links*/);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ItemCollection {\n");

        sb.append("    roles: ").append(toIndentedString(type)).append("\n");
        sb.append("    api: ").append(toIndentedString(features)).append("\n");
        sb.append("    nextPageToken: ").append(toIndentedString(nextPageToken)).append("\n");
        // sb.append("    links: ").append(toIndentedString(links)).append("\n");
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

