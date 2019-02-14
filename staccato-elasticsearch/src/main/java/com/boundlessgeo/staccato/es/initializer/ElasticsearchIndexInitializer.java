package com.boundlessgeo.staccato.es.initializer;

import com.boundlessgeo.staccato.StacInitializer;
import com.boundlessgeo.staccato.collection.CatalogType;
import com.boundlessgeo.staccato.collection.CollectionMetadata;
import com.boundlessgeo.staccato.elasticsearch.annotation.Mapping;
import com.boundlessgeo.staccato.elasticsearch.annotation.MappingType;
import com.boundlessgeo.staccato.es.config.ElasticsearchConfigProps;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.admin.indices.template.put.PutIndexTemplateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexTemplatesRequest;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.boundlessgeo.staccato.es.config.ElasticsearchConfigProps.Es.Mappings;

/**
 * @author joshfix
 * Created on 2/12/19
 */
@Slf4j
@RequiredArgsConstructor
public class ElasticsearchIndexInitializer implements StacInitializer {

    private final List<CollectionMetadata> collectionMetadataList;
    private final ElasticsearchConfigProps configProps;
    private final RestHighLevelClient client;
    private static String PROPERTIES_PREFIX = "properties.";

    /**
     * Executes after the Spring Boot context has initialized.  This method will check to see if the given Elasticsearch
     * index already exists.  If not, it will obtain a list of field mappings from properties and additionally  scan the
     * classpath to find any mappings defined via annotations.  Once the mappings object has been built it will be
     * committed to Elasticsearch.
     *
     * @throws Exception
     */
    @Override
    public void init() {
        if (null != collectionMetadataList && !collectionMetadataList.isEmpty()) {
            for (CollectionMetadata collection : collectionMetadataList) {

                // for each collection there should be a "collection" catalog type and a "catalog" catalog type that
                // both contain the same metadata.  we only need to process one since they both operate against the
                // same Elasticsearch index.
                if (collection.getCatalogType() == CatalogType.CATALOG) {
                    continue;
                }

                try {
                    initializeIndex(collection);
                } catch (Exception e) {
                    log.error("An error was encountered while attempting to initialize Elasticsearch for collection '" +
                            collection.getId() + "'.  Initialization aborted.", e);
                    return;
                }
            }
        }
    }

    @Override
    public String getName() {
        return "Elasticsearch Index Initializer";
    }

    public void initializeIndex(CollectionMetadata collection) throws Exception {
        //log.info("Attempting to initialize Elasticsearch for collection '" + collection.getId() + "'");

        boolean templateCreated = (!templateExists(collection) && createTemplate(collection)) ? true : false;
        boolean indexCreated = (!indexExists(collection) && createInitialIndex(collection)) ? true : false;

    }

    public boolean templateExists(CollectionMetadata collection) {
        String indexTemplateName = collection.getId() + "-template";
        try {
            GetIndexTemplatesRequest getTemplatesRequest = new GetIndexTemplatesRequest(indexTemplateName);
            client.indices().getTemplate(getTemplatesRequest, RequestOptions.DEFAULT);
            log.info("Found existing Elasticsearch template '" + indexTemplateName +
                    "' for collection '" + collection.getId() + "'");
            return true;
        } catch (Exception e) {
            // template doesn't exist.  don't do anything for now, we'll add it
        }

        log.debug("No template '" + indexTemplateName + "' found for collection '" + collection.getId() + "'");
        return false;
    }

    public boolean indexExists(CollectionMetadata collection) throws Exception {
        String initialIndexName = collection.getId() + "-000001";

        if (client.indices().exists(new GetIndexRequest().indices(initialIndexName), RequestOptions.DEFAULT)) {
            log.info("Found existing index '" + initialIndexName + "' for collection '" + collection.getId() + "'");
            return true;
        }

        log.debug("No index '" + initialIndexName + "' found for collection '" + collection.getId() + "'");
        return false;
    }

    private boolean createTemplate(CollectionMetadata collection) throws Exception {
        String searchAlias = collection.getId() + "-api";
        String indexTemplateName = collection.getId() + "-template";
        String indexTemplatePattern = collection.getId() + "-*";

        log.debug("Attempting to initialize template for collection '" + collection.getId() + "'");

        PutIndexTemplateRequest request = new PutIndexTemplateRequest(indexTemplateName)
                .patterns(Arrays.asList(indexTemplatePattern))
                .alias(new Alias(searchAlias))
                .mapping("_doc", buildMappings(collection));

        client.indices().putTemplate(request, RequestOptions.DEFAULT);

        log.info("Template '" + indexTemplateName
                + "' for collection '" + collection.getId() + "' initialized");
        return true;

    }

    private boolean createInitialIndex(CollectionMetadata collection) throws Exception {
        String initialIndexName = collection.getId() + "-000001";
        String writeAlias = collection.getId();

        CreateIndexRequest request = new CreateIndexRequest(initialIndexName);
        request.alias(new Alias(writeAlias));

        client.indices().create(request, RequestOptions.DEFAULT);
        log.info("Index '" + initialIndexName + "' for collection '"
                + collection.getId() + "' initialized");
        return true;
    }

    // builds the ES JSON representation of mappings
    private Map<String, Object> buildMappings(CollectionMetadata collection) {
        Mappings mappingConfig = configProps.getEs().getMappings();

        Map<String, Object> docPropertiesPropertiesProperties = new HashMap<>();
        Map<String, Object> docPropertiesProperties = new HashMap<>();
        Map<String, Object> docProperties = new HashMap<>();
        Map<String, Object> doc = new HashMap<>();


        // process the core mappings.
        if (!mappingConfig.getCoreMappings().isEmpty()) {
            for (Map.Entry<String, MappingType> entry : mappingConfig.getCoreMappings().entrySet()) {
                processMapping(docProperties, docPropertiesPropertiesProperties, entry.getKey(), entry.getValue());
            }
        }

        // process custom mappings
        if (!mappingConfig.getCustomMappings().isEmpty()) {
            for (Map.Entry<String, MappingType> entry : mappingConfig.getCustomMappings().entrySet()) {
                processMapping(docProperties, docPropertiesPropertiesProperties, entry.getKey(), entry.getValue());
            }
        }

        // process annotated fields
        for (Field field : collection.getProperties().getClass().getDeclaredFields()) {
            Mapping mapping = field.getAnnotation(Mapping.class);
            JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
            if (null != mapping) {
                MappingType mappingType = mapping.type();
                String fieldName = (null != jsonProperty && null != jsonProperty.value() && !jsonProperty.value().isEmpty())
                        ? jsonProperty.value() : field.getName();
                Map<String, Object> type = new HashMap<>(1);
                type.put("type", mappingType);
                docPropertiesPropertiesProperties.put(fieldName, type);
            }
        }

        doc.put("properties", docProperties);
        docProperties.put("properties", docPropertiesProperties);
        docPropertiesProperties.put("properties", docPropertiesPropertiesProperties);

        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("_doc", doc);
        return jsonMap;
    }

    private void processMapping(Map<String, Object> docProperties,
                                Map<String, Object> docPropertiesPropertiesProperties, String key, MappingType value) {
        Map<String, Object> type = new HashMap<>(1);
        type.put("type", value);
        if (key.toLowerCase().startsWith(PROPERTIES_PREFIX)) {
            key = key.substring(key.indexOf(PROPERTIES_PREFIX) + PROPERTIES_PREFIX.length());
            docPropertiesPropertiesProperties.put(key, type);
        } else {
            docProperties.put(key, type);
        }
    }


}
