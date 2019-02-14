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
 * We do not want to store links in the database, as the deployed hostname/DNS is subject to change.  This class will
 * dynamically build links using the configured protocol/host/port.
 *
 * @author joshfix
 * Created on 2/12/18
 */
@Component
public class LinkBuilderFilter implements ItemSearchFilter {

    private static String LINK_BASE;
    private static String THUMB_BASE;
    private final StacConfigProps stacConfigProps;
    private final static Set<String> types = new HashSet<>(Arrays.asList("*"));

    public LinkBuilderFilter(StacConfigProps stacConfigProps, LinksConfigProps linksConfigProps) {
        this.stacConfigProps = stacConfigProps;

        StringBuilder selfSb = new StringBuilder(linksConfigProps.getSelf().getScheme())
                .append("://").append(linksConfigProps.getSelf().getHost());
        if (linksConfigProps.getSelf().getPort() != 80) {
            selfSb.append(":").append(linksConfigProps.getSelf().getPort());
        }
        LINK_BASE = selfSb.append(linksConfigProps.getSelf().getContextPath())
                .toString();


        StringBuilder thumbSb = new StringBuilder(linksConfigProps.getThumbnails().getScheme())
                .append("://").append(linksConfigProps.getThumbnails().getHost());
        if (linksConfigProps.getThumbnails().getPort() != 80) {
            thumbSb.append(":").append(linksConfigProps.getThumbnails().getPort());
        }
        THUMB_BASE = thumbSb.append(linksConfigProps.getThumbnails().getContextPath())
                .append("/")
                .toString();
    }

    @Override
    public Set<String> types() {
        return types;
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

        if (stacConfigProps.isGenerateThumbnailLinks()) {
            item = buildThumbnailLink(item);
        }

        buildSelfLink(item);
        buildCollectionLink(item);
        buildRootLink(item);

        return item;
    }

    private void buildSelfLink(Item item) {
        item.getLinks().add(Link.build()
                .href(LINK_BASE + "collections/" + item.getProperties().getCollection() + "/items/" + item.getId())
                .rel("self"));
    }

    private void buildCollectionLink(Item item) {
        item.getLinks().add(Link.build()
                .href(LINK_BASE + "collections/" + item.getProperties().getCollection())
                .rel("collection"));
    }

    private void buildRootLink(Item item) {
        item.getLinks().add(Link.build()
                .href(LINK_BASE + "stac")
                .rel("root"));
    }

    private Item buildThumbnailLink(Item item) {
        Link link = new Link();
        link.href(THUMB_BASE + item.getId() + ".png").rel("thumbnail");
        Set<Link> links = item.getLinks();
        if (null == links) {
            links = new HashSet<>();
            item.setLinks(links);
        }
        links.add(link);
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
