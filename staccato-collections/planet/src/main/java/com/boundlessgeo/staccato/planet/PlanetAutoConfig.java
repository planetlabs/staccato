package com.boundlessgeo.staccato.planet;

import com.boundlessgeo.staccato.collection.CatalogType;
import com.boundlessgeo.staccato.collection.CollectionMetadata;
import com.boundlessgeo.staccato.config.LinksConfigProps;
import com.boundlessgeo.staccato.model.Link;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author joshfix
 * Created on 10/22/18
 */
@Configuration
public class PlanetAutoConfig {

    @Bean
    public CollectionMetadata planetCollection() {
        PlanetCollectionMetadata metadata = new PlanetCollectionMetadata();
        List<Link> links = new ArrayList<>();
        links.add(Link.build()
                .rel("self")
                .href(LinksConfigProps.LINK_PREFIX + "/collections/" + metadata.getId()));

        links.add(Link.build()
                .rel("root")
                .href(LinksConfigProps.LINK_PREFIX + "/staccato"));

        links.add(Link.build()
                .rel("parent")
                .href(LinksConfigProps.LINK_PREFIX + "/staccato"));

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
                .href(LinksConfigProps.LINK_PREFIX + "/staccato/" + metadata.getId()));

        links.add(Link.build()
                .rel("root")
                .href(LinksConfigProps.LINK_PREFIX + "/staccato"));

        links.add(Link.build()
                .rel("parent")
                .href(LinksConfigProps.LINK_PREFIX + "/staccato"));

        links.add(Link.build()
                .rel("items")
                .href(LinksConfigProps.LINK_PREFIX + "/staccato/" + metadata.getId() + "/items"));
        metadata.setLinks(links);
        metadata.setCatalogType(CatalogType.CATALOG);
        return metadata;
    }
}
