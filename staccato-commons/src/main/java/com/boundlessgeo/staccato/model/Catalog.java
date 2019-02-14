package com.boundlessgeo.staccato.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author joshfix
 * Created on 10/15/18
 */
@Data
public class Catalog {

    @JsonProperty("stac_version")
    private String version;
    private String title;
    private String id;
    private String description;
    private Collection<Link> links = new ArrayList<>();
    @JsonIgnore
    private Collection<Catalog> subcatalogs = new ArrayList<>();
    @JsonIgnore
    private String path;
}
