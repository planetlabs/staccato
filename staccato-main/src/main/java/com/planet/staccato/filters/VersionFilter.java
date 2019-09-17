package com.planet.staccato.filters;

import com.planet.staccato.config.StacConfigProps;
import com.planet.staccato.dto.api.SearchRequest;
import com.planet.staccato.model.Item;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Removes the centroid from the item before returning the api response.
 *
 * @author joshfix
 * Created on 10/17/18
 */
@Component
public class VersionFilter implements ItemSearchFilter {

    private final String version;
    private final static Set<String> TYPES = new HashSet<>(Arrays.asList("*"));

    public VersionFilter(StacConfigProps configProps) {
        version = configProps.getVersion();
    }
    @Override
    public Set<String> types() {
        return TYPES;
    }

    @Override
    public Item doFilter(Item item, SearchRequest request) {
        return item.stacVersion(version);
    }

}
