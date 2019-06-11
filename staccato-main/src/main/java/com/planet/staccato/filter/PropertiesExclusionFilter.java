package com.planet.staccato.filter;

import com.planet.staccato.dto.api.SearchRequest;
import com.planet.staccato.model.Item;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Removes properties from the item before returning the api response if the search request used the fields
 * parameter and did not include the properties fields.
 *
 * @author joshfix
 * Created on 02/26/19
 */
@Component
public class PropertiesExclusionFilter implements ItemSearchFilter {

    private final static Set<String> TYPES = new HashSet<>(Arrays.asList("*"));

    @Override
    public Set<String> types() {
        return TYPES;
    }

    @Override
    public Item doFilter(Item item, SearchRequest request) {
        if (null != request.getFields() && request.getFields().length > 0) {
            boolean propertiesRequested = false;
            boolean collectionRequested = false;
            for (String field : request.getFields()) {
                if (field.startsWith("properties.")) {
                    propertiesRequested = true;
                }
                if (field.equalsIgnoreCase("collection")) {
                    collectionRequested = true;
                }

            }
            if (!propertiesRequested) {
                return item.properties(null);
            }
            if (!collectionRequested) {
                item.setCollection(null);
            }

        }
        return item;

    }

}
