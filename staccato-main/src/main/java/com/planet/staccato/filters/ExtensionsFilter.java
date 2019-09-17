package com.planet.staccato.filters;

import com.planet.staccato.dto.api.SearchRequest;
import com.planet.staccato.model.Item;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Removes the centroid from the item before returning the api response.
 *
 * @author joshfix
 * Created on 10/17/18
 */
@Component
public class ExtensionsFilter implements ItemSearchFilter {

    private Map<Class, Set<String>> cache = new HashMap<>();
    private final static Set<String> TYPES = new HashSet<>(Arrays.asList("*"));

    @Override
    public Set<String> types() {
        return TYPES;
    }

    @Override
    public Item doFilter(Item item, SearchRequest request) {
        if (item.getProperties() == null) {
            return item;
        }
        Class propertiesClass = item.getProperties().getClass();
        if (cache.containsKey(propertiesClass)) {
            return item.stacExtensions(cache.get(propertiesClass));
        }

        Set<String> extensions = new HashSet<>();
        Class[] interfaces = propertiesClass.getInterfaces();
        for (Class clazz : interfaces) {
            try {
                Field prefixField = clazz.getDeclaredField("EXTENSION_PREFIX");
                if (prefixField != null) {
                    String prefix = (String)prefixField.get(item.getProperties());
                    extensions.add(prefix);
                }
            } catch (Exception e) {
                // field doesn't exist, do nothing
            }
        }
        cache.put(propertiesClass, extensions);
        item.setStacExtensions(extensions);
        return item;
    }

}
