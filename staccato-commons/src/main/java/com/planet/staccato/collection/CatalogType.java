package com.planet.staccato.collection;

/**
 * Catalogs require a minimum subset of the same fieldsExtension found in collections, but can use all the same fieldsExtension as
 * collections.  Instead of creating two identical objects, we just use one that contains this enum to differentiate
 * if the instance is considered a collection or catalog.
 *
 * @author joshfix
 * Created on 10/25/18
 */
public enum CatalogType {
    COLLECTION, CATALOG
}
