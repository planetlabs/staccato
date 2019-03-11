package com.planet.staccato.es;

import com.planet.staccato.collection.CollectionMetadata;
import com.planet.staccato.es.config.IndexAliasConfigProps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides a service to determine which read/write aliases should be used based on the collection ID.  It is
 * recommended to never use an Elasticsearch index directly and to always use aliases so the actual index can be
 * changed, reindexed, rolled over, etc.
 *
 * @author joshfix
 * Created on 9/4/18
 */
@Slf4j
@Service
public class IndexAliasLookup {

    private final Map<String, String> writeAliasMap = new HashMap<>();
    private final Map<String, String> readAliasMap = new HashMap<>();
    private final List<String> writeAliases;
    private final List<String> readAliases;
    public static final String READ_ALIAS_SUFFIX = "-search";

    public IndexAliasLookup(@Autowired(required = false) List<CollectionMetadata> collectionMetadata,
                            IndexAliasConfigProps configProps) {

        for (Map.Entry<String, String> entry : configProps.getAliases().entrySet()) {
            writeAliasMap.put(entry.getValue(), entry.getKey());
            readAliasMap.put(entry.getValue(), entry.getKey() + READ_ALIAS_SUFFIX);
        }

        for (CollectionMetadata metadata : collectionMetadata) {
            if (!writeAliasMap.containsKey(metadata.getId())) {
                writeAliasMap.put(metadata.getId(), metadata.getId());
            }

            if (!readAliasMap.containsKey(metadata.getId())) {
                readAliasMap.put(metadata.getId(), metadata.getId() + READ_ALIAS_SUFFIX);
            }
        }

        writeAliases = new ArrayList<>(writeAliasMap.values());
        readAliases = new ArrayList<>(readAliasMap.values());
    }

    public String getWriteAlias(String type) {
        return (writeAliasMap.containsKey(type)) ? writeAliasMap.get(type) : type;
    }

    public String getReadAlias(String type) {
        return (readAliasMap.containsKey(type)) ? readAliasMap.get(type) : type;
    }

    public List<String> getWriteAliases() {
        return writeAliases;
    }

    public List<String> getReadAliases() {
        return readAliases;
    }


}
