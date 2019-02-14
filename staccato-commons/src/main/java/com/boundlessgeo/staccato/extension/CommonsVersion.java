package com.boundlessgeo.staccato.extension;

/**
 * The "version" field is typically found as part of the collection metadata.  Per the commons extension, fields from
 * the collection can be merged with the item properties.  This interface defines the "version" field for properties.
 * @see <a href="https://github.com/radiantearth/stac-spec/tree/master/extensions/commons">Commons Extension</a>
 *
 * @author joshfix
 * Created on 2/13/19
 */
public interface CommonsVersion {

    String getVersion();
    void setVersion(String version);

}
