package com.planet.staccato.config;

import com.planet.staccato.collection.CollectionMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

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

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(List.of("*"));
        corsConfig.setMaxAge(8000L);
        corsConfig.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }

}
