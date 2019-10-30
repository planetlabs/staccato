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
    private final static String STAC_VERSION_KEY = "stac_version";
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
        Set<String> include = null;
        Set<String> exclude = null;

        if (request.getFields() != null) {
            include = request.getFields().getInclude();
            exclude = request.getFields().getExclude();
        }

        if (include != null && !include.isEmpty()) {
            return include.contains(STAC_VERSION_KEY) ? item.stacVersion(version) : item;
        } else if (exclude != null && !exclude.isEmpty()) {
            if (exclude.contains(STAC_VERSION_KEY)) {
                return item;
            }
        }
        return item.stacVersion(version);
    }

}
