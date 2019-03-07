package com.boundlessgeo.staccato.planet;

import com.boundlessgeo.staccato.collection.CollectionMetadataAdapter;
import com.boundlessgeo.staccato.model.Provider;
import lombok.Data;

/**
 * @author joshfix
 * Created on 10/23/18
 */
@Data
public class PlanetCollectionMetadata extends CollectionMetadataAdapter<PlanetItemProperties> {

    public static final String STAC_VERSION = "0.6.2";
    public static final String ID = "planet";
    public static final String TITLE = "Planet";
    public static final String DESCRIPTION = "Planet data";
    public static final String VERSION = "0.0.0";
    public static final String LICENSE = "CC-BY-SA";
    public static final String KEYWORDS = "planet";

    public PlanetCollectionMetadata() {
        super();

        setStacVersion(STAC_VERSION);
        setId(ID);
        setTitle(TITLE);
        setDescription(DESCRIPTION);
        setVersion(VERSION);
        setLicense(LICENSE);
        keywords.add(KEYWORDS);

        properties = new PlanetItemProperties();

        buildProperties();
        buildProviders();
    }

    private void buildProviders() {
        providers.add(Provider.build()
                .name("Planet")
                .addRole(Provider.Role.PRODUCER)
                .url("https://planet.com"));

        providers.add(Provider.build()
                .name("Planet Labs")
                .addRole(Provider.Role.PROCESSOR)
                .url("https://planet.com"));

        providers.add(Provider.build()
                .name("Planet Labs")
                .addRole(Provider.Role.HOST)
                .url("https://planet.com/"));
    }

    private void buildProperties() {
        //properties.setPlatform("landsat-8");
        //properties.setInstrument("OLI_TIRS");
        //properties.setConstellation("landsat");
        //properties.setOffNadir(0);
        //properties.setBands(buildBands());
    }

}
