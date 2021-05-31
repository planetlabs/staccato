package com.planet.staccato.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

/**
 * @author joshfix
 * Created on 10/15/18
 */
@Data
public class Provider {

    @NotBlank(message = "Provider field 'name' must not be blank.")
    private String name;
    private String description;
    private String url;
    private Set<Role> roles = new HashSet<>();

    public static Provider build() {
        return new Provider();
    }

    public Provider name(String name) {
        setName(name);
        return this;
    }

    public Provider description(String description) {
        setDescription(description);
        return this;
    }

    public Provider url(String url) {
        setUrl(url);
        return this;
    }

    public Provider roles(Set<Role> roles) {
        this.setRoles(roles);
        return this;
    }

    public Provider addRole(Role role) {
        roles.add(role);
        return this;
    }

    public enum Role {
        LICENSOR("licensor"), PRODUCER("producer"), PROCESSOR("processor"), HOST("host");

        private String value;

        Role(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static Role fromValue(String text) {
            for (Role b : Role.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }

    }
}
