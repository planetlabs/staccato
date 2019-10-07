package com.planet.staccato.collection;

import com.planet.staccato.extension.Scientific;
import com.planet.staccato.model.CoreProperties;
import com.planet.staccato.model.Extent;
import com.planet.staccato.model.Link;
import com.planet.staccato.model.Provider;
import lombok.Data;

import java.util.*;

/**
 * Simple extendable implementation of {@link CollectionMetadata}
 *
 * @author joshfix
 * Created on 10/15/18
 */
@Data
public class CollectionMetadataAdapter<T extends CoreProperties> implements CollectionMetadata<T>, Scientific {

    protected String stacVersion;
    protected Set<String> stacExtensions;
    protected String id;
    protected String title;
    protected String description;
    protected List<String> crs;
    protected String itemType;
    protected Set<String> keywords = new HashSet<>();
    protected String version;
    protected String license;
    protected Extent extent;
    protected List<Provider> providers = new ArrayList<>();
    protected Map<String, Object> summaries;
    protected T properties;
    protected List<Link> links = new ArrayList<>();
    protected CatalogType catalogType;

    protected String doi;
    protected String citation;
    protected Collection<Publication> publications;

    @Override
    public CollectionMetadata<T> stacVersion(String stacVersion) {
        setStacVersion(stacVersion);
        return this;
    }

    @Override
    public CollectionMetadata<T> stacExtensions(Set<String> stacExtensions) {
        setStacExtensions(stacExtensions);
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

    @Override
    public CollectionMetadata<T> summaries(Map<String, Object> summaries) {
        setSummaries(summaries);
        return this;
    }

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

    @Override
    public CollectionMetadata<T> crs(String crs) {
        if (this.crs == null) {
            this.crs = new ArrayList<>();
        }
        this.crs.add(crs);
        return this;
    }

    @Override
    public CollectionMetadata<T> crs(List<String> crs) {
        setCrs(crs);
        return this;
    }

    @Override
    public CollectionMetadata<T> itemType(String itemType) {
        setItemType(itemType);
        return this;
    }

}
