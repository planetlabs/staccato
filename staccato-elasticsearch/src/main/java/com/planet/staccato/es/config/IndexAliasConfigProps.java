package com.planet.staccato.es.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration properties for Elasticsearch index aliases.  The alias mappings should be placed in properties.yml
 * under staccato.es.index.aliases
 *
 * @author joshfix
 * Created on 9/4/18
 */
@Data
@Component
@ConfigurationProperties(prefix = "staccato.es.index", ignoreInvalidFields = true)
public class IndexAliasConfigProps {

    private Map<String, String> aliases = new HashMap<>();
}
