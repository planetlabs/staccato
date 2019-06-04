package com.planet.staccato.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author joshfix
 * Created on 1/17/18
 */
@Data
public class Asset {


    private String href;
    private String name;
    private String type;
    private String filetype;
    private String title;
    @JsonProperty("eo:bands")
    private List<String> bands;

    public Asset href(String href) {
        setHref(href);
        return this;
    }

    public Asset name(String name) {
        setName(name);
        return this;
    }

    public Asset type(String type) {
        setType(type);
        return this;
    }

    public Asset filetype(String filetype) {
        setFiletype(filetype);
        return this;
    }

    public Asset title(String title) {
        setTitle(title);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Asset {\n");

        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    href: ").append(toIndentedString(href)).append("\n");
        sb.append("    roles: ").append(toIndentedString(type)).append("\n");
        sb.append("    filetype: ").append(toIndentedString(filetype)).append("\n");
        sb.append("    bands: ").append(String.join(",", bands)).append("\n");
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
