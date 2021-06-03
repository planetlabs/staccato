package com.planet.staccato.collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.planet.staccato.model.*;
import com.planet.staccato.properties.CoreProperties;
import lombok.Data;

import java.util.*;

/**
 * Simple extendable implementation of {@link CollectionMetadata}
 *
 * @author joshfix
 * Created on 10/15/18
 */
@Data
public class CollectionMetadataAdapter<T extends CoreProperties> implements CollectionMetadata<T> {

    protected String stacVersion;
    protected Set<String> stacExtensions;
    protected String id;
    protected String title;
    protected String description;
    protected List<String> crs;
    protected Map<String, Asset> assets = new HashMap<>();
    protected String itemType;
    protected Set<String> keywords = new HashSet<>();
    protected String version;
    protected String license;
    protected Extent extent;
    protected List<Provider> providers = new ArrayList<>();
    protected Map<String, Stats> summaries;
    @JsonIgnore
    protected T properties;
    protected List<Link> links = new ArrayList<>();
    protected CatalogType catalogType;

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
    public Map<String, Asset> getAssets() {
        return assets;
    }

    @Override
    public void setAssets(Map<String, Asset> assets) {
        this.assets = assets;
    }

    @Override
    public CollectionMetadata<T> assets(Map<String, Asset> assets) {
        setAssets(assets);
        return this;
    }

    @Override
    public CollectionMetadata<T> properties(T properties) {
        setProperties(properties);
        return this;
    }

    @Override
    public void setSummaries(Map<String, Stats> summaries) {
        this.summaries = summaries;
    }

    @Override
    public Map<String, Stats> getSummaries() {
        return summaries;
    }

    @Override
    public CollectionMetadata<T> summaries(Map<String, Stats> summaries) {
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
