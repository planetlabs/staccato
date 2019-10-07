package com.planet.staccato.collection;

import com.planet.staccato.config.LinksConfigProps;
import com.planet.staccato.model.Link;

import java.util.ArrayList;
import java.util.List;

/**
 * @author joshfix
 * Created on 2019-09-17
 */
public class LinkHelper {


    public static List<Link> buildCollectionLinks(String collectionId) {
        List<Link> links = new ArrayList<>();
        links.add(Link.build()
                .rel("self")
                .href(LinksConfigProps.LINK_PREFIX + "/collections/" + collectionId));

        links.add(Link.build()
                .rel("root")
                .href(LinksConfigProps.LINK_PREFIX + "/stac"));

        links.add(Link.build()
                .rel("parent")
                .href(LinksConfigProps.LINK_PREFIX + "/stac"));

        links.add(Link.build()
                .rel("items")
                .type("application/geo+json")
                .href(LinksConfigProps.LINK_PREFIX + "/collections/" + collectionId + "/items"));
        return links;
    }

    public static List<Link> buildCatalogLinks(String catalogId) {
        List<Link> links = new ArrayList<>();
        links.add(Link.build()
                .rel("self")
                .href(LinksConfigProps.LINK_PREFIX + "/stac/" + catalogId));

        links.add(Link.build()
                .rel("root")
                .href(LinksConfigProps.LINK_PREFIX + "/stac"));

        links.add(Link.build()
                .rel("parent")
                .href(LinksConfigProps.LINK_PREFIX + "/stac"));

        links.add(Link.build()
                .rel("items")
                .href(LinksConfigProps.LINK_PREFIX + "/stac/" + catalogId + "/items"));
        return links;
    }

}
