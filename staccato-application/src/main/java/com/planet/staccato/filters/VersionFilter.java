package com.planet.staccato.filters;

import com.planet.staccato.config.LinksConfigProps;
import com.planet.staccato.config.StacConfigProps;
import com.planet.staccato.config.StaccatoMediaType;
import com.planet.staccato.dto.api.SearchRequest;
import com.planet.staccato.model.Item;
import com.planet.staccato.model.Link;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * We do not want to store links in the database, as the deployed hostname/DNS is subject to change.  This class will
 * dynamically build links using the configured protocol/host/port.
 *
 * @author joshfix
 * Created on 2/12/18
 */
@Component
@RequiredArgsConstructor
public class VersionFilter implements ItemSearchFilter {

    private final StacConfigProps stacConfigProps;
    private final static Set<String> types = new HashSet<>(Arrays.asList("*"));

    @Override
    public Set<String> types() {
        return types;
    }

    @Override
    public Item doFilter(Item item, SearchRequest request) {
        return item.stacVersion(stacConfigProps.getVersion());
    }
}
