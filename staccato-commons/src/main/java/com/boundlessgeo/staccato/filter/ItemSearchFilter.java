package com.boundlessgeo.staccato.filter;

import com.boundlessgeo.staccato.dto.SearchRequest;
import com.boundlessgeo.staccato.model.Item;

import java.util.Set;

/**
 * Defines the interface that must be implemented to apply logic to items in api results before they are returned
 * to the client.
 *
 * @author joshfix
 * Created on 2/8/18
 */
public interface ItemSearchFilter {

    Set<String> types();
    Item doFilter(Item item, SearchRequest request);

}
