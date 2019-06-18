package com.planet.staccato.filters;

import com.planet.staccato.model.Item;
import com.planet.staccato.model.Link;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Disallows adding items with self links.  Self links will be dynamically generated for every item in the response
 * using the {@link LinkBuilderFilter}.
 *
 * @author joshfix
 * Created on 1/31/18
 */
@Component
public class SelfLinkExclusionFilter implements ItemIndexFilter {

    private final static Set<String> types = new HashSet<>(Arrays.asList("*"));

    @Override
    public Set<String> types() {
        return types;
    }

    @Override
    public Item doFilter(Item item) {
        if (null != item.getLinks())
        for (Iterator<Link> iterator = item.getLinks().iterator(); iterator.hasNext();) {
            Link link = iterator.next();
            if (link.getRel().equals("self")) {
                iterator.remove();
            }
        }
        return item;
    }
}
