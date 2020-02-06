package com.planet.staccato.filters;

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
public class CentroidExclusionFilter implements ItemSearchFilter {

    private final static Set<String> TYPES = new HashSet<>(Arrays.asList("*"));

    @Override
    public Set<String> types() {
        return TYPES;
    }

    @Override
    public Item doFilter(Item item, SearchRequest request) {
        return item.centroid(null);
    }

}
