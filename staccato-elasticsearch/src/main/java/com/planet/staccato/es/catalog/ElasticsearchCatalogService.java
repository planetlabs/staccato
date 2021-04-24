package com.planet.staccato.es.catalog;

import com.planet.staccato.collection.CollectionMetadata;
import com.planet.staccato.es.IndexAliasLookup;
import com.planet.staccato.es.api.ElasticsearchApiService;
import com.planet.staccato.model.Item;
import com.planet.staccato.model.ItemCollection;
import com.planet.staccato.service.CatalogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.support.ValueType;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;

/**
 * This service provides functionality required by the catalog API to retrieve items and find sets of unique values
 * for a given item property field.
 *
 * @author joshfix
 * Created on 10/24/18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticsearchCatalogService implements CatalogService {

    private final RestHighLevelClient client;
    private final IndexAliasLookup indexAliasLookup;
    private final ElasticsearchApiService searchService;

    /**
     * Fetches a single item.
     *
     * @param itemId The id of the item to retrieve
     * @param collectionId The collection the item belongs to
     * @return The item matching the given itemId and collectionId, wrapped in a Mono
     */
    @Override
    public Mono<Item> getItem(String itemId, String collectionId) {
        return searchService.getItem(itemId, collectionId);
    }

    /**
     * Produces a list of all unique values for a given field in a collection.
     *
     * @param collection The collection that will be queried.
     * @param path The request path containing property fieldsExtension and values for which to find unique values
     * @return A list of unique values
     */
    @Override
    public List<String> getValuesForField(CollectionMetadata collection, List<String> path) {
        String fieldName = path.get(path.size() - 1);

        List<String> values = new LinkedList<>();
        fieldName = "properties." + fieldName;

        // build the term aggregation from the last subcatalog property in the url path
        TermsAggregationBuilder aggregationBuilder = new TermsAggregationBuilder(fieldName + "_Agg", ValueType.STRING).size(10000);
        aggregationBuilder.field(fieldName);

        // the query based on all of the unique subcatalog value previously selected
        QueryBuilder query = QueryBuilders.boolQuery();
        for (int i = 2; i * 2 <= path.size(); i = i + 2) {
            String property = "properties." + path.get(i);
            String value = path.get(i + 1);
            QueryBuilder pathQuery = QueryBuilders.termQuery(property, value);

            ((BoolQueryBuilder) query).must(pathQuery);
        }

        SearchRequest request = new SearchRequest().indices(indexAliasLookup.getReadAlias(collection.getId()))
                .searchType(SearchType.DFS_QUERY_THEN_FETCH)
                .source(new SearchSourceBuilder().query(query).aggregation(aggregationBuilder).size(0));

        SearchResponse response;
        try {
            response = client.search(request, RequestOptions.DEFAULT);
        } catch (Exception ex) {
            log.error("Error getting aggregations.", ex);
            throw new RuntimeException("Error getting aggregations.", ex);
        }

        if (response == null) return Collections.EMPTY_LIST;

        ParsedTerms terms = response.getAggregations().get(fieldName + "_Agg");
        if (terms != null) {
            List<ParsedTerms.ParsedBucket> buckets = (List<ParsedTerms.ParsedBucket>) terms.getBuckets();
            if (buckets != null && buckets.size() > 0) {
                for (ParsedTerms.ParsedBucket bucket : buckets) {
                    values.add(bucket.getKeyAsString());
                }
            }
            return values;
        }
        return Collections.EMPTY_LIST;
    }

    /**
     * Searches for a collection of items given a api query built from path variables that were traversed in a
     * subcatalog.
     * @param collectionId The id of the collection
     * @param pathVariables A map of fieldnames and their corresponding values that should be used to build the api
     *                      query
     * @return The collection of item results wrapped in a Mono
     */
    @Override
    public Mono<ItemCollection> getItems(String collectionId, Map<String, String> pathVariables) {
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String, String>> it = pathVariables.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry entry = it.next();
            sb.append("properties." + entry.getKey() + "=" + entry.getValue());
            if (it.hasNext()) {
                sb.append(" AND ");
            }
        }
        com.planet.staccato.dto.api.SearchRequest searchRequest = new com.planet.staccato.dto.api.SearchRequest();
        String filter = sb.toString().isBlank() ? null : sb.toString();
        searchRequest.setFilter(filter);
        searchRequest.setLimit(10);
        searchRequest.setCollections(new String[]{collectionId});
        return searchService.getItemCollection(searchRequest);
    }

}
