package com.boundlessgeo.staccato.collection;

import com.boundlessgeo.staccato.extension.CommonsCollection;
import com.boundlessgeo.staccato.model.Extent;
import com.boundlessgeo.staccato.model.Link;
import com.boundlessgeo.staccato.model.MandatoryProperties;
import com.boundlessgeo.staccato.model.Provider;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Simple extendable implementation of {@link CollectionMetadata}
 *
 * @author joshfix
 * Created on 10/15/18
 */
@Data
public class CollectionMetadataAdapter<T extends CommonsCollection & MandatoryProperties> implements CollectionMetadata<T> {

    protected String stacVersion;
    protected String id;
    protected String title;
    protected String description;
    protected Set<String> keywords = new HashSet<>();
    protected String version;
    protected String license;
    protected Extent extent;
    protected List<Provider> providers = new ArrayList<>();
    protected T properties;
    //protected Class<T> propertiesClass;
    protected List<Link> links = new ArrayList<>();
    protected CatalogType catalogType;

    @Override
    public CollectionMetadata<T> stacVersion(String stacVersion) {
        setStacVersion(stacVersion);
        return this;
    }

    @Override
    public CollectionMetadata<T> id(String id) {
        setId(id);
        return this;
    }

    @Override
    public CollectionMetadata<T> title(String title) {
        setTitle(title);
        return this;
    }

    @Override
    public CollectionMetadata<T> description(String description) {
        setDescription(description);
        return this;
    }

    @Override
    public CollectionMetadata<T> keywords(Set<String> keywords) {
        setKeywords(keywords);
        return this;
    }

    @Override
    public CollectionMetadata<T> version(String version) {
        setVersion(version);
        return this;
    }

    @Override
    public CollectionMetadata<T> license(String license) {
        setLicense(license);
        return this;
    }

    @Override
    public CollectionMetadata<T> extent(Extent extent) {
        setExtent(extent);
        return this;
    }

    @Override
    public CollectionMetadata<T> providers(List<Provider> providers) {
        setProviders(providers);
        return this;
    }

    @Override
    public CollectionMetadata<T> properties(T properties) {
        setProperties(properties);
        return this;
    }
/*
    @Override
    public CollectionMetadata<T> propertiesClass(Class propertiesClass) {
        setPropertiesClass(propertiesClass);
        return this;
    }
*/
    @Override
    public CollectionMetadata<T> links(List<Link> links) {
        setLinks(links);
        return this;
    }

    @Override
    public CollectionMetadata<T> catalogType(CatalogType catalogType) {
        setCatalogType(catalogType);
        return this;
    }

}
