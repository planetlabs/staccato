package com.boundlessgeo.staccato.filter;

import com.boundlessgeo.staccato.config.LinksConfigProps;
import com.boundlessgeo.staccato.config.StacConfigProps;
import com.boundlessgeo.staccato.dto.SearchRequest;
import com.boundlessgeo.staccato.model.Item;
import com.boundlessgeo.staccato.model.Link;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Builds links to BCIS
 *
 * @author joshfix
 * Created on 2/12/18
 */
@Component
public class WmsLinkBuilderFilter implements ItemSearchFilter {

    private static String BCIS_LINK_BASE;
    private static String LINK_BASE;
    private final StacConfigProps stacConfigProps;
    private final static Set<String> types = new HashSet<>(Arrays.asList("landsat-8-l1"));

    @Override
    public Set<String> types() {
        return types;
    }

    public WmsLinkBuilderFilter(StacConfigProps stacConfigProps, LinksConfigProps linksConfigProps) {
        this.stacConfigProps = stacConfigProps;

        LinksConfigProps.Bcis bcis = linksConfigProps.getBcis();
        StringBuilder sb = new StringBuilder(bcis.getScheme())
                .append("://")
                .append(bcis.getHost());

        if (bcis.getPort() != 80) {
            sb.append(":").append(bcis.getPort());
        }

        LINK_BASE = sb.toString() + "/";
        BCIS_LINK_BASE = sb.append(bcis.getContextPath())
                .toString();

    }

    @Override
    public Item doFilter(Item item, SearchRequest request) {
        if (!shouldFilter(request)) {
            return item;
        }

        Set<Link> links = item.getLinks();
        if (null == links) {
            links = new HashSet<>();
            item.setLinks(links);
        }

        item.getLinks().add(Link.build()
                .href(BCIS_LINK_BASE + item.getId() + "/wms?service=WMS&request=GetMap&layers=B1&version=1.1.1&" +
                        "SRS=epsg:4326&width=512&height=512&transparent=true&bbox=" + item.getBbox().toString())
                .rel("bcis")
                .type("image/tiff")
                .title("Boundless Cloud Imagery Service - GetMap - Single band"));

        item.getLinks().add(Link.build()
                .href(BCIS_LINK_BASE + item.getId() + "/wms?service=WMS&request=GetCapabilities&version=1.3.0")
                .rel("bcis")
                .type("text/xml")
                .title("Boundless Cloud Imagery Service - GetCapabilities"));

        item.getLinks().add(Link.build()
                .href(BCIS_LINK_BASE + item.getId() +  "/wms?service=WMS&request=GetMap&layers=rgb&version=1.1.1&" +
                        "SRS=epsg:4326&width=512&height=512&styles=transfer(contrast:6.5;brightness:1.05)&transparent=true&bbox=" + item.getBbox().toString())
                .rel("bcis")
                .type("image/tiff")
                .title("Boundless Cloud Imagery Service - GetMap - RGB image"));

        return item;
    }

    private boolean shouldFilter(SearchRequest request) {
        Object o = request.getPropertyname();
        List<String> propertyname = null;

        if (null != o && o instanceof String[]) {
            propertyname = Arrays.asList((String[]) o);
        } else {
            // no specific propertynames were requested -- proceed with adding links
            return true;
        }

        if (!propertyname.contains("links")) {
            // if propertynames were requested but did not contain links, do not add links
            return false;
        }
        // propertynames were present and did contain links, proceed with adding links
        return true;
    }
}
