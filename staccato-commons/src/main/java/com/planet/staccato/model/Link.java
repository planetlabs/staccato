package com.planet.staccato.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Link
 */
@Data
public class Link {

    private String href;
    private String rel;
    private String type;
    private String hreflang;
    private String title;
    private Integer length;
    private String method;
    private Map<String, String> headers;
    private Map<String, String> body;
    private Boolean merge;
    @JsonProperty("label:assets")
    private String labelAssets;

    public static Link build() {
        return new Link();
    }

    public Link href(String href) {
        setHref(href);
        return this;
    }

    public Link rel(String rel) {
        setRel(rel);
        return this;
    }

    public Link type(String type) {
        setType(type);
        return this;
    }

    public Link title(String title) {
        setTitle(title);
        return this;
    }

    public Link hreflang(String hreflang) {
        setHreflang(hreflang);
        return this;
    }

    public Link length(int length) {
        setLength(length);
        return this;
    }

    public Link method(String method) {
        setMethod(method);
        return this;
    }

    public Link headers(Map<String, String> headers) {
        setHeaders(headers);
        return this;
    }

    public Link addHeader(String key, String value) {
        if (this.headers == null) {
            this.headers = new HashMap<>();
        }
        this.headers.put(key, value);
        return this;
    }

    public Link addHeaders(Map<String, String> headers) {
        if (this.headers == null) {
            this.headers = new HashMap<>();
        }
        headers.putAll(headers);
        return this;
    }

    public Link body(Map<String, String> body) {
        setBody(body);
        return this;
    }

    public Link addBodyEntry(String key, String value) {
        if (this.body == null) {
            this.body = new HashMap<>();
        }
        body.put(key, value);
        return this;
    }

    public Link merge(boolean merge) {
        setMerge(merge);
        return this;
    }

    public Link addBodyEntries(Map<String, String> bodyEntries) {
        if (this.body == null) {
            this.body = new HashMap<>();
        }
        body.putAll(bodyEntries);
        return this;
    }

    public Link labelAssets(String labelAssets) {
        this.labelAssets = labelAssets;
        return this;
    }

}

