package com.boundlessgeo.staccato.config;

import com.boundlessgeo.staccato.collection.CollectionMetadata;
import com.boundlessgeo.staccato.extension.CommonsCollection;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
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

    private final ObjectMapper mapper;
    private final List<CollectionMetadata> collectionMetadataList;

    /**
     * The following code is necessary for Jackson to understand what implementation class to deserialize item
     * properties to.  It creates a mapping inside of Jackson between the properties implementation class and the
     * id of the collection
     */
    @PostConstruct
    public void init() {
        collectionMetadataList.forEach(metadata -> {
                NamedType namedType = new NamedType(metadata.getProperties().getClass(), metadata.getProperties().getCollection());
                mapper.registerSubtypes(namedType);
                mapper.addMixIn(CommonsCollection.class, ItemPropertiesMixin.class);
        });
        // TODO -- this isn't being set from the main initializer for some reason???
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.EXISTING_PROPERTY,
            property = "collection"
    )
    private abstract class ItemPropertiesMixin {}
}
