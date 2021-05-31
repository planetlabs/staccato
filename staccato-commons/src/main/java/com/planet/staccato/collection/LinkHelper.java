package com.planet.staccato.collection;

import com.planet.staccato.config.LinksConfigProps;
import com.planet.staccato.config.StaccatoMediaType;
import com.planet.staccato.model.Link;

import java.util.ArrayList;
import java.util.List;

import static com.planet.staccato.config.StaccatoMediaType.APPLICATION_JSON_VALUE;
import static com.planet.staccato.config.StaccatoRelType.QUERYABLES_LINK_REL;

/**
 * @author joshfix
 * Created on 2019-09-17
 */
public class LinkHelper {

    public static List<Link> buildCollectionLinks(String collectionId) {
        List<Link> links = new ArrayList<>();
        links.add(Link.build()
                .rel("self")
                .type(APPLICATION_JSON_VALUE)
                .href(LinksConfigProps.LINK_PREFIX + "/collections/" + collectionId));

        links.add(Link.build()
                .rel("root")
                .type(APPLICATION_JSON_VALUE)
                .href(LinksConfigProps.LINK_PREFIX));

        links.add(Link.build()
                .rel("parent")
                .type(APPLICATION_JSON_VALUE)
                .href(LinksConfigProps.LINK_PREFIX));

        links.add(Link.build()
                .rel("items")
                .type(StaccatoMediaType.APPLICATION_GEO_JSON_VALUE)
                .href(LinksConfigProps.LINK_PREFIX + "/collections/" + collectionId + "/items"));

        links.add(Link.build()
                .href(LinksConfigProps.LINK_PREFIX + "/collections/" + collectionId + "/queryables")
                .rel(QUERYABLES_LINK_REL)
                .type(StaccatoMediaType.APPLICATION_SCHEMA_JSON_VALUE));
        return links;
    }

    public static List<Link> buildCatalogLinks(String catalogId) {
        List<Link> links = new ArrayList<>();
        links.add(Link.build()
                .rel("self")
                .type(APPLICATION_JSON_VALUE)
                .href(LinksConfigProps.LINK_PREFIX + "/" + catalogId));

        links.add(Link.build()
                .rel("root")
                .type(APPLICATION_JSON_VALUE)
                .href(LinksConfigProps.LINK_PREFIX));

        links.add(Link.build()
                .rel("parent")
                .type(APPLICATION_JSON_VALUE)
                .href(LinksConfigProps.LINK_PREFIX));

        links.add(Link.build()
                .rel("items")
                .type(StaccatoMediaType.APPLICATION_GEO_JSON_VALUE)
                .href(LinksConfigProps.LINK_PREFIX + "/" + catalogId + "/items"));
        return links;
    }

}
