package com.planet.staccato.properties.extension;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.planet.staccato.collection.CollectionMetadata;
import com.planet.staccato.model.Item;

import java.util.List;

/**
 * Defines the fieldsExtension and Jackson property values for fieldsExtension in the single item extension.
 * @see <a href="https://github.com/radiantearth/stac-spec/tree/master/extensions/single-item">Single Item Extension</a>
 * @author joshfix
 * Created on 2/11/19
 */
public interface SingleFileStac {

    String type = "FeatureCollection";

    @JsonProperty("collections")
    List<CollectionMetadata> getCollections();
    void setCollections(List<CollectionMetadata> collectionMetadata);

    @JsonProperty("features")
    List<Item> getItems();
    void setItems(List<Item> items);
}
