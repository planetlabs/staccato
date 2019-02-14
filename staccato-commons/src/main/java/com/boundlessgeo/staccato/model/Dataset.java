package com.boundlessgeo.staccato.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author joshfix
 * Created on 10/15/18
 */
@Data
public class Dataset {

    @JsonProperty("stac_version")
    private String stacVersion;
    private String id;
    private String title;
    private String description;
    private String keywords;
    private String version;
    private String license;
    private Extent extent;
    private List<Provider> providers = new ArrayList<>();
    private ItemProperties properties;

}
