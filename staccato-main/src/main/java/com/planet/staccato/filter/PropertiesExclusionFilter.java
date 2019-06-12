package com.planet.staccato.filter;

import com.planet.staccato.dto.api.SearchRequest;
import com.planet.staccato.model.Item;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Nulls the properties in the item before returning the api response if the search request used the fields extension
 * parameter and did not include any properties fields.  In addition, the collection field is always returned from
 * the backend to provide the flag for Jackson as to which properties implementation to deserialize to.  However, if
 * that field was not requested to be included or was requested to be excluded, we null it before returning the response.
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
        if (request.getFields() == null) {
            return item;
        }
        Set<String> include = request.getFields().getInclude();
        Set<String> exclude = request.getFields().getExclude();
        if ((include == null || include.isEmpty()) && (exclude == null || exclude.isEmpty())) {
            return item;
        }

        // if include fields are present, but none of them are properties fields or the collection field, null them out
        if (include != null && !include.isEmpty()) {
            boolean propertiesRequested = false;
            boolean collectionRequested = false;

            for (String field : include) {
                if (field.startsWith("properties.")) {
                    propertiesRequested = true;
                    continue;
                }
                if (field.equalsIgnoreCase("collection")) {
                    collectionRequested = true;
                    continue;
                }

            }

            if (!propertiesRequested) {
                item.setProperties(null);
            }
            if (!collectionRequested) {
                item.setCollection(null);
            }
        // if no include fields were set, but exclude fields requested collection be excluded, then... exclude it.
        } else if (exclude != null && !exclude.isEmpty()) {
            for (String field : exclude) {
                if (field.equalsIgnoreCase("collection")) {
                    item.setCollection(null);
                }
            }
        }

        return item;
    }

}
