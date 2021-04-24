package com.planet.staccato.config;

import com.planet.staccato.collection.CollectionMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author joshfix
 * Created on 2/12/19
 */
@Component
@Configuration
@RequiredArgsConstructor
public class StacConfiguration {

    private final List<CollectionMetadata> collectionMetadataList;

    @Bean
    public Map<String, CollectionMetadata> collectionMetadataMap() {
        Map<String, CollectionMetadata> metadataMap = new HashMap<>(collectionMetadataList.size());
        collectionMetadataList.forEach(cm -> metadataMap.put(cm.getId(), cm));
        return metadataMap;
    }

}
