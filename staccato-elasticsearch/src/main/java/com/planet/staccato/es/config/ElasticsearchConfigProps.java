package com.planet.staccato.es.config;

import com.planet.staccato.elasticsearch.annotation.MappingType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author joshfix
 * Created on 12/6/17
 */
@Data
@Component
@ConfigurationProperties(prefix = "staccato.es")
public class ElasticsearchConfigProps {

    private String scheme = "http";
    private String host = "localhost";
    private int port = 9200;
    private String user;
    private String password;
    private String type = "_doc";  // this will be deprecated and unnecessary in ES7
    private int numberOfShards = 5;
    private int numberOfReplicas = 1;
    private int maxReconnectionAttempts = 10;
    private int restClientMaxConnectionsTotal = 200;
    private int restClientMaxConnectionsPerRoute = 200;
    private int restClientMaxRetryTimeoutMillis = 60000;

    private Mappings mappings = new Mappings();
    private AsyncBridgeThreadPool asyncBridgeThreadPool = new AsyncBridgeThreadPool();

    @Data
    public static class Mappings {

        private Map<String, MappingType> coreMappings = new HashMap<>();
        private Map<String, MappingType> customMappings = new HashMap<>();

        public Mappings() {
            coreMappings.put("id", MappingType.KEYWORD);
            coreMappings.put("geometry", MappingType.GEO_SHAPE);
            coreMappings.put("assets", MappingType.OBJECT);
            coreMappings.put("properties.collection", MappingType.KEYWORD);
            coreMappings.put("properties.datetime", MappingType.DATE);
            coreMappings.put("properties.start_datetime", MappingType.DATE);
            coreMappings.put("properties.end_datetime", MappingType.DATE);
            coreMappings.put("properties.created", MappingType.DATE);
            coreMappings.put("properties.updated", MappingType.DATE);
            coreMappings.put("properties.mission", MappingType.KEYWORD);
            coreMappings.put("properties.constellation", MappingType.KEYWORD);
            coreMappings.put("properties.instruments", MappingType.KEYWORD);
            coreMappings.put("properties.platform", MappingType.KEYWORD);
            coreMappings.put("properties.providers", MappingType.KEYWORD);
            coreMappings.put("properties.license", MappingType.KEYWORD);
            coreMappings.put("centroid", MappingType.GEO_POINT);
        }
    }

    @Data
    public static class AsyncBridgeThreadPool {
        private boolean daemon = true;
        private int maxThreads = 200;
    }
}

