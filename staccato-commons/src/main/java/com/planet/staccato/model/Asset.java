package com.planet.staccato.model;

import lombok.Data;

import java.util.ArrayList;
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

    public Asset description(String description) {
        setDescription(description);
        return this;
    }

    public Asset roles(List<String> roles) {
        this.roles = roles;
        return this;
    }

    public Asset addRole(String role) {
        if (roles == null) {
            roles = new ArrayList<>();
        }
        roles.add(role);
        return this;
    }

    public Asset addRoles(List<String> roles) {
        if (roles == null) {
            roles = new ArrayList<>();
        }
        roles.addAll(roles);
        return this;
    }

}
