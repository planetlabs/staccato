package com.planet.staccato.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.planet.staccato.collection.CatalogType;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * @author joshfix
 * Created on 10/15/18
 */
@Data
public class Catalog {

    @JsonProperty("stac_version")
    private String stacVersion;
    private String title;
    private String id;
    private CatalogType type = CatalogType.CATALOG;
    private Map<String, Stats> summaries;
    private String description;
    private Collection<Link> links = new ArrayList<>();
    @JsonIgnore
    private Collection<Catalog> subcatalogs = new ArrayList<>();
    @JsonIgnore
    private String path;

    public Catalog version(String version) {
        setStacVersion(version);
        return this;
    }

    public Catalog title(String title) {
        setTitle(title);
        return this;
    }

    public Catalog id(String id) {
        setId(id);
        return this;
    }

    public Catalog summaries(Map<String, Stats> summaries) {
        setSummaries(summaries);
        return this;
    }

    public Catalog description(String description) {
        setDescription(description);
        return this;
    }

    public Catalog links(Collection<Link> links) {
        setLinks(links);
        return this;
    }

    public Catalog subcatalogs(Collection<Catalog> subcatalogs) {
        setSubcatalogs(subcatalogs);
        return this;
    }

    public Catalog path(String path) {
        setPath(path);
        return this;
    }
}
