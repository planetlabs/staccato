package com.planet.staccato.planet;

import com.planet.staccato.collection.CatalogType;
import com.planet.staccato.collection.CollectionMetadata;
import com.planet.staccato.config.LinksConfigProps;
import com.planet.staccato.model.Link;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author joshfix
 * Created on 10/22/18
 */
@Configuration
@AllArgsConstructor
public class PlanetAutoConfig {

    // autowiring this in ensures the static LINK_PREFIX value is built
    private final LinksConfigProps linksConfigProps;

    @Bean
    public CollectionMetadata planetCollection() {
        PlanetCollectionMetadata metadata = new PlanetCollectionMetadata();
        List<Link> links = new ArrayList<>();
        links.add(Link.build()
                .rel("self")
                .href(LinksConfigProps.LINK_PREFIX + "/collections/" + metadata.getId()));

        links.add(Link.build()
                .rel("root")
                .href(LinksConfigProps.LINK_PREFIX + "/stac"));

        links.add(Link.build()
                .rel("parent")
                .href(LinksConfigProps.LINK_PREFIX + "/stac"));

        links.add(Link.build()
                .rel("items")
                .href(LinksConfigProps.LINK_PREFIX + "/collections/" + metadata.getId() + "/items"));
        metadata.setLinks(links);
        metadata.setCatalogType(CatalogType.COLLECTION);
        return metadata;
    }

    @Bean
    public CollectionMetadata planetCatalog() {
        PlanetCollectionMetadata metadata = new PlanetCollectionMetadata();
        List<Link> links = new ArrayList<>();
        links.add(Link.build()
                .rel("self")
                .href(LinksConfigProps.LINK_PREFIX + "/stac/" + metadata.getId()));

        links.add(Link.build()
                .rel("root")
                .href(LinksConfigProps.LINK_PREFIX + "/stac"));

        links.add(Link.build()
                .rel("parent")
                .href(LinksConfigProps.LINK_PREFIX + "/stac"));

        links.add(Link.build()
                .rel("items")
                .href(LinksConfigProps.LINK_PREFIX + "/stac/" + metadata.getId() + "/items"));
        metadata.setLinks(links);
        metadata.setCatalogType(CatalogType.CATALOG);
        return metadata;
    }
}
