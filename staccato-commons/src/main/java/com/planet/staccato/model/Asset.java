package com.planet.staccato.model;

import lombok.Data;

import java.util.List;


/**
 * @author joshfix
 * Created on 1/17/18
 */
@Data
public class Asset {


    private String href;
    private String type;
    private String title;
    private String description;
    private List<String> roles;

    public Asset href(String href) {
        setHref(href);
        return this;
    }

    public Asset type(String type) {
        setType(type);
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
        sb.append("    href: ").append(toIndentedString(href)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    name: ").append(toIndentedString(title)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    roles: ").append(String.join(",", roles)).append("\n");
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
