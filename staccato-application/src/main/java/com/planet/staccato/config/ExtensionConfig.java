package com.planet.staccato.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.planet.staccato.collection.CollectionMetadata;
import com.planet.staccato.model.Item;
import com.planet.staccato.properties.CoreProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Jackson configuration for extensions.  Tells Jackson to inspect the collection field value in item properties to
 * determine which concrete class to deserialize to.
 * TODO: this seems to be a combination of an initializer and a configuration.  Probably a cleaner way to do this.
 *
 * @author joshfix
 * Created on 10/22/18
 */
@Component
@Configuration
@RequiredArgsConstructor
public class ExtensionConfig {

    private final StacConfigProps configProps;
    private final ObjectMapper mapper;
    private final List<CollectionMetadata> collectionMetadataList;

    /**
     * The following code is necessary for Jackson to understand what implementation class to deserialize item
     * properties to.  It creates a mapping inside of Jackson between the properties implementation class and the
     * id of the collection
     */
    @PostConstruct
    public void init() {

        mapper.addMixIn(Item.class, ItemMixin.class);
        collectionMetadataList.forEach(metadata -> {
            metadata.setStacVersion(configProps.getVersion());
            NamedType namedType = new NamedType(metadata.getProperties().getClass(), metadata.getId());
            mapper.registerSubtypes(namedType);
        });

        // TODO -- this isn't being set from the main initializer for some reason???
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private interface ItemMixin<T extends CoreProperties> {

        @JsonTypeInfo(
                use = JsonTypeInfo.Id.NAME,
                include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
                property = "collection"
        )
        public T getProperties();
    }

}
