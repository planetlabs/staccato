package com.planet.staccato.landsat8;

import com.planet.staccato.collection.CatalogType;
import com.planet.staccato.collection.CollectionMetadata;
import com.planet.staccato.collection.LinkHelper;
import com.planet.staccato.config.LinksConfigProps;
import com.planet.staccato.config.StacConfigProps;
import com.planet.staccato.queryables.QueryableProperty;
import com.planet.staccato.queryables.Queryables;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.planet.staccato.config.StaccatoSchemas.JSON_SCHEMA;

/**
 * @author joshfix
 * Created on 10/22/18
 */
@Configuration
@AllArgsConstructor
public class Landsat8AutoConfig {

    // autowiring this in ensures the static LINK_PREFIX value is built
    private final LinksConfigProps linksConfigProps;
    private final StacConfigProps stacConfigProps;

    @Bean
    public CollectionMetadata landsat8Collection() {
        Landsat8CollectionMetadata metadata = new Landsat8CollectionMetadata();
        metadata.setStacVersion(stacConfigProps.getVersion());
        metadata.links(LinkHelper.buildCollectionLinks(metadata.getId()))
                .catalogType(CatalogType.COLLECTION);
        return metadata;
    }

    @Bean
    public CollectionMetadata landsat8Catalog() {
        Landsat8CollectionMetadata metadata = new Landsat8CollectionMetadata();
        metadata.setStacVersion(stacConfigProps.getVersion());
        metadata.links(LinkHelper.buildCollectionLinks(metadata.getId()))
                .setCatalogType(CatalogType.CATALOG);
        return metadata;
    }

    @Bean
    public Queryables landsat8Queryables() {
        Queryables queryables = new Queryables()
                .setId(Landsat8CollectionMetadata.ID)
                .setDescription(Landsat8CollectionMetadata.DESCRIPTION)
                .setType(Queryables.OBJECT_TYPE)
                .setTitle(Landsat8CollectionMetadata.TITLE)
                .setSchema(JSON_SCHEMA);

        queryables.addEOProperties();
        queryables.addViewProperties();

        queryables.getProperties().put(Landsat8.EXTENSION_PREFIX + ":wrs_path", new QueryableProperty()
                .setTitle("WRS Path")
                .setType(Queryables.INTEGER_TYPE)
                .setMinimum(0)
                .setMaximum(251));

        queryables.getProperties().put(Landsat8.EXTENSION_PREFIX + ":wrs_row", new QueryableProperty()
                .setTitle("WRS Row")
                .setType(Queryables.INTEGER_TYPE)
                .setMinimum(0)
                .setMaximum(119));

        queryables.getProperties().put(Landsat8.EXTENSION_PREFIX + ":image_quality_tirs", new QueryableProperty()
                .setTitle("Image quality TIRS")
                .setType(Queryables.INTEGER_TYPE)
                .setMinimum(0));

        queryables.getProperties().put(Landsat8.EXTENSION_PREFIX + ":image_quality_oli", new QueryableProperty()
                .setTitle("Image quality OLI")
                .setType(Queryables.INTEGER_TYPE)
                .setMinimum(0));

        queryables.getProperties().put(Landsat8.EXTENSION_PREFIX + ":earth_sun_distance", new QueryableProperty()
                .setTitle("Earth-sun distance")
                .setType(Queryables.INTEGER_TYPE)
                .setMinimum(0));

        queryables.getProperties().put(Landsat8.EXTENSION_PREFIX + ":scene_id", new QueryableProperty()
                .setTitle("Image quality TIRS")
                .setType(Queryables.STRING_TYPE)
                .setMinLength(1));

        queryables.getProperties().put(Landsat8.EXTENSION_PREFIX + ":product_id", new QueryableProperty()
                .setTitle("Product ID")
                .setType(Queryables.STRING_TYPE)
                .setMinLength(1));

        queryables.getProperties().put(Landsat8.EXTENSION_PREFIX + ":processing_level", new QueryableProperty()
                .setTitle("Processing level")
                .setType(Queryables.STRING_TYPE)
                .setMinLength(1));

        return queryables;

    }
}
