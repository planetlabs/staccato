package com.boundlessgeo.staccato.extension;

/**
 * The "collection" field is typically found as part of the collection metadata.  This is field can be useful to include
 * in each item's properties as it defines the primary collection the item belongs to.  This interface defines the
 * "collection" field and its corresponding Jackson property values.
 *
 * Note in the future this field is expected to move to the root of the item in the core spec, not in item properties.
 *
 * @author joshfix
 * Created on 2/12/19
 */
public interface Collection {

    String getCollection();
    void setCollection(String collection);
}
