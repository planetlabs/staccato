package com.planet.staccato.catalog;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.planet.staccato.collection.CatalogType;
import com.planet.staccato.collection.CollectionMetadata;
import com.planet.staccato.collection.Subcatalog;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Responsible for discovering all properties fieldsExtension in each collection that are annotated with {@link Subcatalog}
 * and providing the ability to determine which subcatalogable fieldsExtension are still available given a request path.
 *
 * @author joshfix
 * Created on 10/24/18
 */
@Service
@RequiredArgsConstructor
public class SubcatalogPropertiesService {

    private final List<CollectionMetadata> collectionMetadataList;
    private Map<String, Set<PropertyField>> collectionsProperties = new HashMap<>();
    private Map<String, Map<String, String>> javaToJsonNames = new HashMap<>();
    private Map<String, Map<String, String>> jsonToJavaNames = new HashMap<>();

    /**
     * This method is responsible for finding all property field names annotated with @Subcatalog for which subcatalog
     * links should be auto generated.
     */
    @PostConstruct
    public void init() {
        collectionMetadataList.forEach(collection -> {
            // only generating subcatalogs for subcatalogs, not collections
            if (collection.getCatalogType() == CatalogType.COLLECTION) {
                return;
            }
            String collectionId = collection.getId();
            collectionsProperties.put(collectionId, new HashSet<>());
            javaToJsonNames.put(collectionId, new HashMap<>());
            jsonToJavaNames.put(collectionId, new HashMap<>());

            // use reflection to examine all of the annotated interface methods
            Class[] interfaces = collection.getProperties().getClass().getInterfaces();
            for (Class interfase : interfaces) {
                List<Method> methods = Arrays.asList(interfase.getDeclaredMethods());
                for (Method method : methods) {
                    if (method.isAnnotationPresent(JsonProperty.class) && method.isAnnotationPresent(Subcatalog.class)) {
                        String javaName = BeanUtils.findPropertyForMethod(method).getName();
                        String jsonName = method.isAnnotationPresent(JsonProperty.class) ? method.getAnnotation(JsonProperty.class).value() : javaName;
                        collectionsProperties.get(collectionId).add(new PropertyField().jsonName(jsonName).javaName(javaName));
                        javaToJsonNames.get(collectionId).put(javaName, jsonName);
                        jsonToJavaNames.get(collectionId).put(jsonName, javaName);
                    }
                }
            }
        });
    }

    public String javaToJsonName(String collectionId, String javaPropertyName) {
        return javaToJsonNames.get(collectionId).get(javaPropertyName);
    }

    public String jsonToJavaName(String collectionId, String jsonPropertyName) {
        return jsonToJavaNames.get(collectionId).get(jsonPropertyName);
    }

    /**
     * Determines which remaining fieldsExtension are eligible to be subcataloged given the current request path.
     *
     * @param collectionId The id of the collection
     * @param path         The request path
     * @return A list of remaining eligible property fieldsExtension
     */
    public List<PropertyField> getRemainingProperties(String collectionId, String path) {
        if (!collectionsProperties.containsKey(collectionId)) {
            return Collections.EMPTY_LIST;
        }
        List<PropertyField> fieldNamesFinal = new ArrayList<>();

        List<PropertyField> fieldNames = new ArrayList<>(collectionsProperties.get(collectionId));
        List<String> currentFields = parsePath(path);
        fieldNames.forEach(fn -> {
            if (!currentFields.contains(fn.getJsonName())) {
                fieldNamesFinal.add(fn);
            }
        });

        return fieldNamesFinal;
    }

    /**
     * Splits the request path into an array, accounting for any unwanted trailing or leading slashes.
     *
     * @param path The request path
     * @return An array of path segments
     */
    public List<String> parsePath(String path) {
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        return Arrays.asList(path.split("/"));
    }

    /**
     * Builds a map of subcataloged property fieldsExtension to the unique value of that field.  Eg, if the user accesses
     * /stac/my_collection/path/10, the map will be populated with `path -> 10`.
     *
     * @param collectionId The id of the collection
     * @param path         The request path
     * @return A map of subcataloged property fieldsExtension to field value
     */
    public Map<String, String> createSubcatalogPathParamMap(String collectionId, String path) {
        List<String> pathList = new ArrayList<>(parsePath(path));
        pathList.remove("stac");
        pathList.remove(collectionId);
        pathList.remove("items");

        String[] pathArray = pathList.toArray(new String[]{});
        Map<String, String> subcatalogPathParamMap = new HashMap<>(pathArray.length / 2);
        for (int i = 0; i < pathArray.length; i += 2) {
            // this means the last value of the url is a property and not a property/value combo, so don't include it
            if (i + 2 > pathArray.length) {
                continue;
            }
            subcatalogPathParamMap.put(pathArray[i], pathArray[i + 1]);
        }
        /*  TODO - need to figure out why StacControllerAdvice doesn't handle these exceptions
        if (!collectionsProperties.get(collectionId).containsAll(subcatalogPathParamMap.keySet())) {
            throw new RuntimeException("Path contains invalid field names");
        }
        */
        return subcatalogPathParamMap;
    }


}