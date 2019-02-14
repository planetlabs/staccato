package com.boundlessgeo.staccato.filter;

import com.boundlessgeo.staccato.model.Item;

import java.util.Set;

/**
 * Defines the interface that must be implemented to apply logic to items before they are inserted into the database.
 *
 * @author joshfix
 * Created on 2/8/18
 */
public interface ItemIndexFilter {
    Set<String> types();
    Item doFilter(Item item);
}
