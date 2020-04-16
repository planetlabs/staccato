package com.planet.staccato.dto.api.extensions;


import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * @author joshfix
 * Created on 2019-06-11
 */
@Data
public class FieldsExtension {

    private Set<String> include;
    private Set<String> exclude;

    public FieldsExtension() {}

    public FieldsExtension(String fields) {
        if (null != fields) {
            for (String field : fields.split(",")) {
                add(field);
            }
        }
    }

    public FieldsExtension include(Set<String> include) {
        setInclude(include);
        return this;
    }

    public FieldsExtension include(String field) {
        if (this.include == null) {
            this.include = new HashSet<>();
        }
        include.add(field);
        return this;
    }

    public FieldsExtension exclude(Set<String> exclude) {
        setExclude(exclude);
        return this;
    }

    public FieldsExtension exclude(String field) {
        if (this.exclude == null) {
            this.exclude = new HashSet<>();
        }
        exclude.add(field);
        return this;
    }

    public boolean isIncluded(String field) {
        return include != null && include.contains(field);
    }

    public boolean isExcluded(String field) {
        return exclude != null && exclude.contains(field);
    }

    public boolean add(String s) {
        if (s.startsWith("-")) {
            if (exclude == null) exclude = new HashSet<>();
            exclude.add(s.substring(1));
        } else {
            if (include == null) include = new HashSet<>();
            include.add(s);
        }
        return true;
    }
}
