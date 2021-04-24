package com.planet.staccato.collection;

/**
 * Catalogs require a minimum subset of the same fieldsExtension matched in collections, but can use all the same fieldsExtension as
 * collections.  Instead of creating two identical objects, we just use one that contains this enum to differentiate
 * if the instance is considered a collection or catalog.
 *
 * @author joshfix
 * Created on 10/25/18
 */
public enum CatalogType {
    COLLECTION("Collection"),
    CATALOG("Catalog");

    private String value;

    CatalogType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static com.planet.staccato.collection.CatalogType fromValue(String text) {
        for (com.planet.staccato.collection.CatalogType b : com.planet.staccato.collection.CatalogType.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}