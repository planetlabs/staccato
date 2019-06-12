package com.planet.staccato.dto.api.extensions;


import lombok.Data;

import java.util.Set;

/**
 * @author joshfix
 * Created on 2019-06-11
 */
@Data
public class FieldsExtension {

    private Set<String> include;
    private Set<String> exclude;

    public FieldsExtension include(Set<String> include) {
        setInclude(include);
        return this;
    }

    public FieldsExtension exclude(Set<String> exclude) {
        setExclude(exclude);
        return this;
    }
}
