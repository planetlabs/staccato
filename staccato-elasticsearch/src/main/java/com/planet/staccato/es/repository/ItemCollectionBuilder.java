package com.planet.staccato.es.repository;

import com.planet.staccato.config.LinksConfigProps;
import com.planet.staccato.dto.api.SearchRequest;
import com.planet.staccato.es.QueryBuilderHelper;
import com.planet.staccato.model.Item;
import com.planet.staccato.model.ItemCollection;
import com.planet.staccato.model.Link;
import com.planet.staccato.model.SearchMetadata;
import joptsimple.internal.Strings;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author joshfix
 * Created on 2019-09-17
 */
public class ItemCollectionBuilder {

    public static List<SearchHit> buildMeta(SearchMetadata searchMetadata, SearchResponse response, SearchRequest searchRequest) {
        searchMetadata.matched(response.getHits().getTotalHits())
                .returned(response.getHits().getHits().length)
                .limit(searchRequest.getLimit())
                .matched(response.getHits().getTotalHits());

        // only build next token if needed
        if ((searchMetadata.getReturned() == searchMetadata.getLimit()) && (searchMetadata.getMatched() >= searchMetadata.getReturned())) {
            SearchHit lastHit = response.getHits().getHits()[response.getHits().getHits().length - 1];
            String nextToken = NextTokenHelper.serialize(lastHit.getSortValues());
            searchMetadata.next(nextToken);
        }

        return Arrays.asList(response.getHits().getHits());
    }

    public static ItemCollection buildItemCollection(SearchMetadata searchMetadata, List<Item> itemList, SearchRequest searchRequest) {
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
                .rel("self"));

        if (searchMetadata.getNext() != null) {
            itemCollection.addLink(new Link()
                    .href(link + "&next=" + searchMetadata.getNext())
                    .rel("next"));
        }

        return itemCollection;
    }
}
