package com.boundlessgeo.staccato.extension;

/**
 * The "license" field is typically found as part of the collection metadata.  Per the commons extension, fields from
 * the collection can be merged with the item properties.  This interface defines the "license" field and its
 * corresponding Jackson property values.
 * @see <a href="https://github.com/radiantearth/stac-spec/tree/master/extensions/commons">Commons Extension</a>
 *
 * @author joshfix
 * Created on 2/13/19
 */
public interface CommonsLicense {

    String getLicense();
    void setLicense(String license);

}
