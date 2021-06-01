package com.planet.staccato.planet;

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
public class PlanetAutoConfig {

    // autowiring this in ensures the static LINK_PREFIX value is built
    private final LinksConfigProps linksConfigProps;
    private final StacConfigProps stacConfigProps;

    @Bean
    public CollectionMetadata planetCollection() {
        PlanetCollectionMetadata metadata = new PlanetCollectionMetadata();
        metadata.setStacVersion(stacConfigProps.getVersion());
        metadata.links(LinkHelper.buildCollectionLinks(metadata.getId()))
                .catalogType(CatalogType.COLLECTION);
        return metadata;
    }

    @Bean
    public CollectionMetadata planetCatalog() {
        PlanetCollectionMetadata metadata = new PlanetCollectionMetadata();
        metadata.setStacVersion(stacConfigProps.getVersion());
        metadata.links(LinkHelper.buildCollectionLinks(metadata.getId()))
                .setCatalogType(CatalogType.CATALOG);
        return metadata;
    }

    @Bean
    public Queryables planetQueryables() {
        Queryables queryables = new Queryables()
                .setId(PlanetCollectionMetadata.ID)
                .setDescription(PlanetCollectionMetadata.DESCRIPTION)
                .setType(Queryables.OBJECT_TYPE)
                .setTitle(PlanetCollectionMetadata.TITLE)
                .setSchema(JSON_SCHEMA);

        queryables.addEOProperties();
        queryables.addViewProperties();

        queryables.getProperties().put(Planet.EXTENSION_PREFIX + ":ground_control", new QueryableProperty()
                .setTitle("Ground control")
                .setType(Queryables.BOOLEAN_TYPE));

        queryables.getProperties().put(Planet.EXTENSION_PREFIX + ":item_type", new QueryableProperty()
                .setTitle("Item type")
                .setType(Queryables.STRING_TYPE)
                .setMinLength(1));

        queryables.getProperties().put(Planet.EXTENSION_PREFIX + ":provider", new QueryableProperty()
                .setTitle("Provider")
                .setType(Queryables.STRING_TYPE)
                .setMinLength(1));

        queryables.getProperties().put(Planet.EXTENSION_PREFIX + ":published", new QueryableProperty()
                .setTitle("Published")
                .setType(Queryables.STRING_TYPE)
                .setFormat(QueryableProperty.TemporalFormatEnum.DATE_TIME));

        queryables.getProperties().put(Planet.EXTENSION_PREFIX + ":quality_category", new QueryableProperty()
                .setTitle("Quality category")
                .setType(Queryables.STRING_TYPE)
                .setMinLength(1));

        queryables.getProperties().put(Planet.EXTENSION_PREFIX + ":satellite_azimuth", new QueryableProperty()
                .setTitle("Satellite azimuth")
                .setType(Queryables.FLOAT_TYPE)
                .setMinimum(0)
                .setMaximum(360));

        queryables.getProperties().put(Planet.EXTENSION_PREFIX + ":satellite_id", new QueryableProperty()
                .setTitle("Satellite ID")
                .setType(Queryables.STRING_TYPE)
                .setMinLength(1));

        queryables.getProperties().put(Planet.EXTENSION_PREFIX + ":strip_id", new QueryableProperty()
                .setTitle("Strip ID")
                .setType(Queryables.STRING_TYPE)
                .setMinLength(1));

        queryables.getProperties().put(Planet.EXTENSION_PREFIX + ":updated", new QueryableProperty()
                .setTitle("Updated")
                .setType(Queryables.STRING_TYPE)
                .setFormat(QueryableProperty.TemporalFormatEnum.DATE_TIME));

        return queryables;

    }
}
