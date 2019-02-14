package com.boundlessgeo.staccato.extension;

/**
 * The "collection" field is typically found as part of the collection metadata.  This is field can be useful to include
 * in each item's properties as it defines the primary collection the item belongs to.  Per the commons extension,
 * fields from the collection can be merged with the item properties.  This interface defines the "collection" field
 * and its corresponding Jackson property values.
 * @see <a href="https://github.com/radiantearth/stac-spec/tree/master/extensions/commons">Commons Extension</a>
 *
 * @author joshfix
 * Created on 2/12/19
 */
public interface CommonsCollection {

    String getCollection();
    void setCollection(String collection);
}
