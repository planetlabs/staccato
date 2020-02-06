package com.planet.staccato.filters;

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
    private final static String PROPERTIES_PREFIX = "properties.";
    private final static String COLLECTION_FIELD = "collection";
    private final static String STAC_EXTENSIONS_FIELD = "stac_extensions";
    @Override
    public Set<String> types() {
        return TYPES;
    }

    @Override
    public Item doFilter(Item item, SearchRequest request) {
        if (request.getFields() == null) {
            return item;
        }
        Set<String> include = null;
        Set<String> exclude = null;

        if (request.getFields() != null) {
            include = request.getFields().getInclude();
            exclude = request.getFields().getExclude();
        }

        if ((include == null || include.isEmpty()) && (exclude == null || exclude.isEmpty())) {
            return item;
        }

        // if include fields are present, but none of them are properties fields or the collection field, null them out
        if (include != null && !include.isEmpty()) {

            if (!include.stream().anyMatch((s) -> s.startsWith(PROPERTIES_PREFIX))) {
                item.setProperties(null);
            }

            if (!include.contains(COLLECTION_FIELD)) {
                item.setCollection(null);
            }

            if (!include.contains(STAC_EXTENSIONS_FIELD)) {
                item.setStacExtensions(null);
            }

        } else if (exclude != null && !exclude.isEmpty()) {
            // if no include fields were set, but exclude fields requested, exclude those fields.
            for (String field : exclude) {
                if (field.equalsIgnoreCase(COLLECTION_FIELD)) {
                    item.setCollection(null);
                }
                if (field.equalsIgnoreCase(STAC_EXTENSIONS_FIELD)) {
                    item.setStacExtensions(null);
                }
            }
        }

        return item;
    }

}
