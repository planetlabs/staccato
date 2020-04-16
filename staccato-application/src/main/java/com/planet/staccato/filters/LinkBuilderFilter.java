package com.planet.staccato.filters;

import com.planet.staccato.config.LinksConfigProps;
import com.planet.staccato.config.StacConfigProps;
import com.planet.staccato.config.StaccatoMediaType;
import com.planet.staccato.dto.api.SearchRequest;
import com.planet.staccato.model.Item;
import com.planet.staccato.model.Link;
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
                .href(LINK_BASE + "collections/" + item.getCollection() + "/items/" + item.getId())
                .rel("self")
                .type(StaccatoMediaType.APPLICATION_GEO_JSON_VALUE));
    }

    private void buildCollectionLink(Item item) {
        item.getLinks().add(Link.build()
                .href(LINK_BASE + "collections/" + item.getCollection())
                .type(MediaType.APPLICATION_JSON_VALUE)
                .rel("collection"));
    }

    private void buildRootLink(Item item) {
        item.getLinks().add(Link.build()
                .href(LINK_BASE + "stac")
                .type(MediaType.APPLICATION_JSON_VALUE)
                .rel("root"));
    }

    private Item buildThumbnailLink(Item item) {
        Link link = new Link();
        link.href(THUMB_BASE + item.getId() + ".png").rel("thumbnail").type("image/png");
        Set<Link> links = item.getLinks();
        if (null == links) {
            links = new HashSet<>();
            item.setLinks(links);
        }
        links.add(link);
        return item;
    }

    private boolean shouldFilter(SearchRequest request) {
        // no special inclusions or exclusions
        if (request.getFields() == null) {
            return true;
        }

        Set<String> include = null;
        Set<String> exclude = null;

        if (request.getFields() != null) {
            include = request.getFields().getInclude();
            exclude = request.getFields().getExclude();
        }

        // if include fields were populated and links were requested, return true and build the links
        if (include != null && !include.isEmpty()) {
            return include.contains("links");
        }

        // if exclude fields were populated and links were requested for exclusion, return false and don't build links
        if (exclude != null && !exclude.isEmpty()) {
            return !exclude.contains("links");
        }

        return true;
    }
}
