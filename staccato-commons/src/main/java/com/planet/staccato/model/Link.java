package com.planet.staccato.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

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
    @JsonProperty("label:assets")
    private String labelAssets;

    public static Link build() {
        return new Link();
    }

    public Link href(String href) {
        this.href = href;
        return this;
    }

    public Link rel(String rel) {
        this.rel = rel;
        return this;
    }

    public Link type(String type) {
        this.type = type;
        return this;
    }

    public Link title(String title) {
        this.title = title;
        return this;
    }

    public Link hreflang(String hreflang) {
        this.hreflang = hreflang;
        return this;
    }

    public Link length(int length) {
        this.length = length;
        return this;
    }

    public Link labelAssets(String labelAssets) {
        this.labelAssets = labelAssets;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Link link = (Link) o;
        return Objects.equals(this.href, link.href) &&
                Objects.equals(this.rel, link.rel) &&
                Objects.equals(this.type, link.type) &&
                Objects.equals(this.length, link.length) &&
                Objects.equals(this.title, link.title) &&
                Objects.equals(this.labelAssets, link.labelAssets) &&
                Objects.equals(this.hreflang, link.hreflang);
    }

    @Override
    public int hashCode() {
        return Objects.hash(href, rel, type, title, length, hreflang, labelAssets);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Link {\n");

        sb.append("    href: ").append(toIndentedString(href)).append("\n");
        sb.append("    rel: ").append(toIndentedString(rel)).append("\n");
        sb.append("    roles: ").append(toIndentedString(type)).append("\n");
        sb.append("    title: ").append(toIndentedString(title)).append("\n");
        sb.append("    hreflang: ").append(toIndentedString(hreflang)).append("\n");
        sb.append("    length: ").append(toIndentedString(length)).append("\n");
        sb.append("    labelAssets: ").append(toIndentedString(labelAssets)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

}

