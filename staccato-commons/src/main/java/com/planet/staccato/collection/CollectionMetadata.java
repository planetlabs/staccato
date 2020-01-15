package com.planet.staccato.collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.planet.staccato.model.Extent;
import com.planet.staccato.model.Link;
import com.planet.staccato.model.Provider;
import com.planet.staccato.properties.CoreProperties;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * All fieldsExtension required for collections.
 *
 * @author joshfix
 * Created on 10/23/18
 */
public interface CollectionMetadata<T extends CoreProperties> {

    @JsonProperty("stac_version")
    String getStacVersion();
    void setStacVersion(String stacVersion);
    CollectionMetadata<T> stacVersion(String stacVersion);

    @JsonProperty("stac_extensions")
    Set<String> getStacExtensions();
    void setStacExtensions(Set<String> stacExtensions);
    CollectionMetadata<T> stacExtensions(Set<String> stacExtensions);

    String getId();
    void setId(String id);
    CollectionMetadata<T> id(String id);

    String getTitle();
    void setTitle(String title);
    CollectionMetadata<T> title(String title);

    String getDescription();
    void setDescription(String description);
    CollectionMetadata<T> description(String description);

    Set<String> getKeywords();
    void setKeywords(Set<String> keywords);
    CollectionMetadata<T> keywords(Set<String> keywords);

    String getVersion();
    void setVersion(String version);
    CollectionMetadata<T> version(String version);

    String getLicense();
    void setLicense(String license);
    CollectionMetadata<T> license(String license);

    Extent getExtent();
    void setExtent(Extent extent);
    CollectionMetadata<T> extent(Extent extent);

    List<Provider> getProviders();
    void setProviders(List<Provider> providers);
    CollectionMetadata<T> providers(List<Provider> providers);

    T getProperties();
    void setProperties(T properties);
    CollectionMetadata<T> properties(T properties);

    Map<String, Object> getSummaries();
    void setSummaries(Map<String, Object> summaries);
    CollectionMetadata<T> summaries(Map<String, Object> summaries);

    List<Link> getLinks();
    void setLinks(List<Link> links);
    CollectionMetadata<T> links(List<Link> links);

    @JsonIgnore
    CatalogType getCatalogType();
    void setCatalogType(CatalogType catalogType);
    CollectionMetadata<T> catalogType(CatalogType catalogType);

    List<String> getCrs();
    void setCrs(List<String> crs);
    CollectionMetadata<T> crs(String crs);
    CollectionMetadata<T> crs(List<String> crs);

    String getItemType();
    void setItemType(String itemType);
    CollectionMetadata<T> itemType(String itemType);

}
