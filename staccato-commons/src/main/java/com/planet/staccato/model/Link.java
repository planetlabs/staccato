package com.planet.staccato.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

/**
 * Link
 */
@Data
public class Link {

    @NotBlank(message = "Link property 'href' may not be blank.")
    private String href;
    @NotBlank(message = "Link property 'rel' may not be blank.")
    private String rel;
    private String type;
    private String hreflang;
    private String title;
    private Integer length;
    private String method;
    private Map<String, Object> headers;
    private Map<String, Object> body;
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

    public Link headers(Map<String, Object> headers) {
        setHeaders(headers);
        return this;
    }

    public Link addHeader(String key, Object value) {
        if (this.headers == null) {
            this.headers = new HashMap<>();
        }
        this.headers.put(key, value);
        return this;
    }

    public Link addHeaders(Map<String, Object> headers) {
        if (this.headers == null) {
            this.headers = new HashMap<>();
        }
        headers.putAll(headers);
        return this;
    }

    public Link body(Map<String, Object> body) {
        setBody(body);
        return this;
    }

    public Link addBodyEntry(String key, Object value) {
        if (this.body == null) {
            this.body = new HashMap<>();
        }
        body.put(key, value);
        return this;
    }

    public Link addBodyEntries(Map<String, Object> bodyEntries) {
        if (this.body == null) {
            this.body = new HashMap<>();
        }
        body.putAll(bodyEntries);
        return this;
    }

    public Link merge(boolean merge) {
        setMerge(merge);
        return this;
    }

    public Link labelAssets(String labelAssets) {
        this.labelAssets = labelAssets;
        return this;
    }

}

