package com.planet.staccato.es.repository;

import com.planet.staccato.config.LinksConfigProps;
import com.planet.staccato.config.StacConfigProps;
import com.planet.staccato.config.StaccatoMediaType;
import com.planet.staccato.dto.api.SearchRequest;
import com.planet.staccato.es.QueryBuilderHelper;
import com.planet.staccato.model.Item;
import com.planet.staccato.model.ItemCollection;
import com.planet.staccato.model.Link;
import com.planet.staccato.model.SearchMetadata;
import joptsimple.internal.Strings;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author joshfix
 * Created on 2019-09-17
 */
@Service
@RequiredArgsConstructor
public class ItemCollectionBuilder {

    private Map<Class, Set<String>> cache = new HashMap<>();
    private final StacConfigProps configProps;

    public String buildNextToken(SearchMetadata searchMetadata, SearchResponse response) {
        // only build next token if needed
        if ((searchMetadata.getReturned() == searchMetadata.getLimit())
                && (searchMetadata.getMatched() >= searchMetadata.getReturned())) {
            SearchHit lastHit = response.getHits().getHits()[response.getHits().getHits().length - 1];
            return NextTokenHelper.serialize(lastHit.getSortValues());
        }
        return null;
    }

    public List<SearchHit> buildMeta(SearchMetadata searchMetadata, SearchResponse response, SearchRequest searchRequest) {
        searchMetadata.matched(response.getHits().getTotalHits())
                .returned(response.getHits().getHits().length)
                .limit(searchRequest.getLimit())
                .matched(response.getHits().getTotalHits());
        return Arrays.asList(response.getHits().getHits());
    }

    public ItemCollection buildItemCollection(SearchMetadata searchMetadata, List<Item> itemList,
                                              SearchRequest searchRequest, String nextToken) {
        ItemCollection itemCollection = new ItemCollection()
                .features(itemList)
                .type(ItemCollection.TypeEnum.FEATURECOLLECTION)
                .metadata(searchMetadata)
                .numberMatched(searchMetadata.getMatched())
                .numberReturned(searchMetadata.getReturned());

        int finalLimit = QueryBuilderHelper.getLimit(searchRequest.getLimit());

        // rebuild the original request link
        double[] bbox = searchRequest.getBbox();
        String link = LinksConfigProps.LINK_BASE + "?limit=" + finalLimit;
        if (null != bbox && (bbox.length == 4 || bbox.length == 6)) {
            link += bbox == null ? Strings.EMPTY : "&bbox=" + bbox[0] + "," + bbox[1] + "," + bbox[2] + "," + bbox[3];
        }
        link += searchRequest.getDatetime() == null ? Strings.EMPTY : "&datetime=" + searchRequest.getDatetime();
        link += searchRequest.getQuery() == null ? Strings.EMPTY : "&query=" + searchRequest.getQuery();
        link += searchRequest.getIds() == null ? Strings.EMPTY :
                "&ids=" + String.join(",", searchRequest.getIds());
        link += searchRequest.getCollections() == null ? Strings.EMPTY :
                "&collections=" + String.join(",", searchRequest.getCollections());

        String fieldsValue = "";
        if (null != searchRequest.getFields()) {
            // add include fields
            fieldsValue += searchRequest.getFields().getInclude() == null ? Strings.EMPTY :
                    String.join(",", searchRequest.getFields().getInclude());

            // add exclude fields
            Set<String> excludeFields = searchRequest.getFields().getExclude();
            if (null != excludeFields) {
                // need to add the "-" prefix for the get parameter
                List<String> prefixedExcludeFields = new ArrayList<>();
                excludeFields.forEach(field -> prefixedExcludeFields.add("-" + field));
                String excludeFieldsString = String.join(",", prefixedExcludeFields);
                fieldsValue += fieldsValue.isBlank() ? excludeFieldsString : "," + excludeFieldsString;
            }
        }

        if (fieldsValue != null && !fieldsValue.isBlank()) {
            link += "&fields=" + fieldsValue;
        }

        String selfLink = searchRequest.getNext() == null ? link : link + "&next=" + searchRequest.getNext();
        itemCollection.addLink(new Link()
                .href(selfLink)
                .type(StaccatoMediaType.APPLICATION_GEO_JSON_VALUE)
                .rel("self"));

        if (nextToken != null) {
            itemCollection.addLink(new Link()
                    .href(link + "&next=" + nextToken)
                    .type(StaccatoMediaType.APPLICATION_GEO_JSON_VALUE)
                    .rel("next"));
        }

        itemCollection.setStacVersion(configProps.getVersion());

        for (Item item : itemList) {
            item.setStacVersion(configProps.getVersion());
            itemCollection.addStacExtensions(addExtensions(item).getStacExtensions());
        }

        return itemCollection;
    }

    /**
     * Adds extension entries into the item's stac_extensions array.
     *
     * @param item
     * @return
     */
    protected Item addExtensions(Item item) {
        Class propertiesClass = item.getProperties().getClass();
        if (cache.containsKey(propertiesClass)) {
            return item.stacExtensions(cache.get(propertiesClass));
        }

        Set<String> extensions = new HashSet<>();
        Class[] interfaces = propertiesClass.getInterfaces();
        for (Class clazz : interfaces) {
            try {
                Field prefixField = clazz.getDeclaredField("EXTENSION_PREFIX");
                if (prefixField != null) {
                    String prefix = (String)prefixField.get(item.getProperties());
                    extensions.add(prefix);
                }
            } catch (Exception e) {
                // field doesn't exist, do nothing
            }
        }
        cache.put(propertiesClass, extensions);
        item.setStacExtensions(extensions);
        return item;
    }
}
