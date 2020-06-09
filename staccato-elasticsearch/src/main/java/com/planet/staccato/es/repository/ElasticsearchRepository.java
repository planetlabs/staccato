package com.planet.staccato.es.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.planet.staccato.FieldName;
import com.planet.staccato.SerializationUtils;
import com.planet.staccato.dto.StacTransactionResponse;
import com.planet.staccato.dto.api.extensions.SortExtension;
import com.planet.staccato.es.QueryBuilderHelper;
import com.planet.staccato.es.config.ElasticsearchConfigProps;
import com.planet.staccato.exception.StaccatoRuntimeException;
import com.planet.staccato.model.Context;
import com.planet.staccato.model.Item;
import com.planet.staccato.model.ItemCollection;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * This class provides the logic for methods that would be expected to be matched in a repository service for performing
 * CRUD operations against Elasticsearch.
 *
 * @author joshfix
 * Created on 11/29/17
 */
@Slf4j
@Service
@AllArgsConstructor
public class ElasticsearchRepository {

    private final RestHighLevelClient restClient;
    private final ObjectMapper mapper;
    private final Scheduler scheduler;
    private final ElasticsearchWebClient client;
    private final ElasticsearchConfigProps configProps;
    private final ItemCollectionBuilder itemCollectionBuilder;

    private static final StacTransactionResponse ITEM_NOT_FOUND;
    private static String TYPE;

    static {
        ITEM_NOT_FOUND = new StacTransactionResponse();
        ITEM_NOT_FOUND.setSuccess(false);
        ITEM_NOT_FOUND.setReason("Item not matched.");
    }

    @PostConstruct
    public void init() {
        TYPE = configProps.getType();
    }

    /**
     * Retries a specific item by index, providers and Id.
     *
     * @param indices Elasticsearch index.
     * @param id      Id for the desired entity.
     * @return A Map representation of the Entity.
     */
    public Mono<Item> searchById(List<String> indices, String id) {
        QueryBuilder queryBuilder = QueryBuilders.termQuery(FieldName.ID, id);
        com.planet.staccato.dto.api.SearchRequest searchRequest = new com.planet.staccato.dto.api.SearchRequest()
                .limit(1)
                //.next("0")
                ;
        return searchItemFlux(indices, queryBuilder, searchRequest)
                .switchIfEmpty(Mono.error(new StaccatoRuntimeException("Item with ID '" + id + "' not matched.", 404)))
                .single()
                .map(r -> {
                    log.debug("is the result null? " + (r == null));
                    return r;
                });
    }

    /**
     * Helper method to construct SearchSourceBuilder objects.
     *
     * @param queryBuilder The builder object for the query
     * @param limit        The max number of items to return in the result
     * @param next         The item offset to retrieve results from
     * @return The builder for the api source
     */
    protected SearchSourceBuilder buildSearchSourceBuilder(QueryBuilder queryBuilder, Integer limit, String next) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(queryBuilder)
                .size(limit);

        if (next != null && !next.isBlank()) {
            Object[] deserializedSortValues = NextTokenHelper.deserialize(next);
            if (null != deserializedSortValues) {
                searchSourceBuilder.searchAfter(deserializedSortValues);
            }
        }
        return searchSourceBuilder;
    }

    /**
     * Helper method for constructing api requests.
     *
     * @param builder The builder for the seach source
     * @param indices The list of indices to api
     * @return The constructed api request
     */
    protected SearchRequest buildSearchRequest(SearchSourceBuilder builder, Collection<String> indices) {
        return new SearchRequest()
                .types(TYPE)
                .source(builder)
                .indices(indices.toArray(new String[indices.size()]));
    }

    public Mono<ItemCollection> searchItemCollection(Collection<String> indices, QueryBuilder queryBuilder,
                                                     final com.planet.staccato.dto.api.SearchRequest searchRequest) {
        String searchString = buildEsSearchString(indices, queryBuilder, searchRequest);
        final Context context = new Context();
        final StringBuilder nextTokenBuilder = new StringBuilder();
        return client.search(searchString, indices)
                // build the meta object and return the search hits
                .flatMapIterable(response -> {
                    List<SearchHit> searchHits = itemCollectionBuilder.buildMeta(context, response, searchRequest);
                    nextTokenBuilder.append(itemCollectionBuilder.buildNextToken(context, response));
                    return searchHits;
                })
                // process all the hits in parallel -- will use all CPU cores by default
                .parallel().runOn(Schedulers.parallel())
                // map each hit to it's source bytes
                .map(hit -> SerializationUtils.deserializeItem(hit.getSourceRef().toBytesRef().bytes, mapper))
                // revert to sequential processing
                .sequential()
                .collectList()
                // take the api list build an item collection from it
                .map(itemList -> itemCollectionBuilder
                        .buildItemCollection(context, itemList, searchRequest, nextTokenBuilder.toString()));
    }

    protected void setIncludeExcludeFields(SearchSourceBuilder searchSourceBuilder,
                                           com.planet.staccato.dto.api.SearchRequest searchRequest) {
        // If include fieldsExtension were provided, make sure `collection` is present because it's needed for Jackson
        // deserialization to the proper ItemProperties subtype.  If the `collection` field was not requested in the
        // `fields` parameter, it will be removed before the response is returned.  Finally, set the includeFields on
        // the api.

        if (searchRequest.getFields() == null) {
            return;
        }

        Set<String> includeSet = null;
        Set<String> excludeSet = null;

        if (searchRequest.getFields() != null && searchRequest.getFields().getInclude() != null) {
            // don't want to alter the original set as it can be used in filters before the response is returned
            includeSet = new HashSet<>(searchRequest.getFields().getInclude());
        }

        if (searchRequest.getFields() != null && searchRequest.getFields().getExclude() != null) {
            excludeSet = searchRequest.getFields().getExclude();
        }

        if ((includeSet != null && !includeSet.isEmpty()) || (excludeSet != null && !excludeSet.isEmpty())) {
            // include fields takes preference, so remove any fields from the exclude set that are also in include
            if (excludeSet != null && includeSet != null) {
                excludeSet = new HashSet<>(excludeSet);
                excludeSet.removeAll(includeSet);
            }
            String[] include = null;
            String[] exclude = null;

            if (includeSet != null && !includeSet.isEmpty()) {
                includeSet.add("collection");
                include = includeSet.toArray(new String[includeSet.size()]);
            }
            if (excludeSet != null && !excludeSet.isEmpty()) {
                exclude = excludeSet.toArray(new String[excludeSet.size()]);
            }
            searchSourceBuilder.fetchSource(include, exclude);
        }

    }

    protected String buildEsSearchString(Collection<String> indices, QueryBuilder queryBuilder,
                                       com.planet.staccato.dto.api.SearchRequest searchRequest) {
        int limit = QueryBuilderHelper.getLimit(searchRequest.getLimit());
        SearchSourceBuilder searchSourceBuilder =
                buildSearchSourceBuilder(queryBuilder, limit, searchRequest.getNext());
        configureSort(searchSourceBuilder, searchRequest.getSortby());
        setIncludeExcludeFields(searchSourceBuilder, searchRequest);

        SearchRequest esSearchRequest = buildSearchRequest(searchSourceBuilder, indices);
        String searchString = esSearchRequest.source().toString();

        log.debug("Searching ES indices '" + indices.toString() + "' with the following request: \n" + searchString);
        return searchString;
    }

    protected void configureSort(SearchSourceBuilder searchSourceBuilder, SortExtension sort) {
        if (sort == null || sort.isEmpty()) {
            searchSourceBuilder
                    .sort(new FieldSortBuilder("properties.datetime").order(SortOrder.DESC))
                    .sort(new FieldSortBuilder("id").order(SortOrder.ASC));
            return;
        }

        for (SortExtension.SortTerm term : sort) {
            SortOrder sortOrder = (term.getDirection() == SortExtension.SortTerm.SortDirection.DESC) ?
                    SortOrder.DESC : SortOrder.ASC;
            searchSourceBuilder.sort(new FieldSortBuilder(term.getField()).order(sortOrder));
        }
        searchSourceBuilder.sort(new FieldSortBuilder("id").order(SortOrder.DESC));
    }

    /**
     * Code to build the ES api request, reactively submit it to ES and deserialize the results.
     *
     * @param indices       The indices to api
     * @param queryBuilder  The builder for the query
     * @param searchRequest The original search request
     * @return A Flux of items
     */
    public Flux<Item> searchItemFlux(Collection<String> indices, QueryBuilder queryBuilder,
                                     com.planet.staccato.dto.api.SearchRequest searchRequest) {
        String searchString = buildEsSearchString(indices, queryBuilder, searchRequest);
        return client.search(searchString, indices)
                // build a flux from the hits
                .flatMapIterable(response -> Arrays.asList(response.getHits().getHits()))
                // process all the hits in parallel -- will use all CPU cores by default
                .parallel().runOn(Schedulers.parallel())
                // map each hit to it's source bytes
                .map(hit -> SerializationUtils.deserializeItem(hit.getSourceRef().toBytesRef().bytes, mapper))
                // revert to sequential processing
                .sequential();
    }

    /**
     * Adds a new Item to the provided Elasticsearch index.
     *
     * @param index The Elasticsearch index to add the item to
     * @param type  The Elasticsearch type
     * @param item  The item to be added
     * @return The {@link StacTransactionResponse response} of the operation wrapped in a mono
     */
    public Mono<StacTransactionResponse> createItem(String index, String type, Item item) {
        Optional<String> optionalJson = SerializationUtils.serializeItemToString(item, mapper);
        if (!optionalJson.isPresent()) {
            StacTransactionResponse stacTransactionResponse = new StacTransactionResponse();
            stacTransactionResponse.setSuccess(false);
            stacTransactionResponse.setReason("Unable to serialize item.  Does the data conform to the schema? Item: "
                    + item);
            return Mono.just(stacTransactionResponse);
        }

        return client
                .create(index, type, item.getId(), optionalJson.get())
                .map(response -> {
                    StacTransactionResponse stacTransactionResponse = new StacTransactionResponse();

                    boolean success = response.getResult() == DocWriteResponse.Result.CREATED ||
                            response.getResult() == DocWriteResponse.Result.UPDATED;
                    stacTransactionResponse.setSuccess(success);
                    stacTransactionResponse.setResult(response.getResult().toString());
                    stacTransactionResponse.setId(item.getId());
                    return stacTransactionResponse;
                })
                .doOnError(error -> {
                    StacTransactionResponse stacTransactionResponse = new StacTransactionResponse();
                    stacTransactionResponse.setId(item.getId());
                    stacTransactionResponse.setSuccess(false);
                    stacTransactionResponse.setReason(error.getLocalizedMessage());
                });
    }

    /**
     * Updates an existing item in Elasticsearch.
     *
     * @param itemMono     The item to be updated, wrapped in a mono
     * @param type         The Elasticsearch type
     * @param body         The updated item body sent in the request.  This should just be the item JSON.
     * @param collectionId The collection ID the item belongs to
     * @return The {@link StacTransactionResponse response} of the operation wrapped in a mono
     */
    public Mono<StacTransactionResponse> updateItem(Mono<Item> itemMono, String type, String body, String collectionId) {

        return itemMono
                .flatMap(item -> client.update(collectionId, type, item.getId(), body)
                        .map(response -> {
                            StacTransactionResponse stacDeleteResponse = new StacTransactionResponse();
                            stacDeleteResponse.setSuccess(response.getResult() == DocWriteResponse.Result.UPDATED);
                            stacDeleteResponse.setResult(response.getResult().toString());
                            return stacDeleteResponse;
                        })
                        .onErrorResume(this::buildErrorResponse))
                .defaultIfEmpty(ITEM_NOT_FOUND);
    }

    /**
     * Deletes an item in Elasticsearch.
     *
     * @param itemMono     The item to be updated, wrapped in a mono
     * @param type         The Elasticsearch type
     * @param collectionId The collection ID the item belongs to
     * @return The {@link StacTransactionResponse response} of the operation wrapped in a mono
     */
    public Mono<StacTransactionResponse> deleteItem(Mono<Item> itemMono, String type, String collectionId) {
        return itemMono
                .flatMap(item -> client.delete(collectionId, type, item.getId())
                        .map(response -> {
                            StacTransactionResponse stacDeleteResponse = new StacTransactionResponse();
                            stacDeleteResponse.setSuccess(response.getResult() == DocWriteResponse.Result.DELETED);
                            stacDeleteResponse.setResult(response.getResult().toString());
                            return stacDeleteResponse;
                        })
                        .onErrorResume(this::buildErrorResponse))
                .defaultIfEmpty(ITEM_NOT_FOUND);
    }

    /**
     * Executes a bulk update in Elasticsearch for multiple items.
     *
     * @param bulkRequest The Elasticsearch bulk request
     * @return The bulk reponse object
     */
    public Mono<BulkResponse> bulkUpdate(BulkRequest bulkRequest) {
        return Mono.fromCallable(() -> restClient.bulk(bulkRequest))
                .subscribeOn(scheduler)
                .publishOn(Schedulers.single());
    }

    /**
     * Helper method to construct error responses.
     *
     * @param e The exception that was thrown
     * @return The {@link StacTransactionResponse response} of the operation wrapped in a mono
     */
    protected Mono<StacTransactionResponse> buildErrorResponse(Throwable e) {
        StacTransactionResponse r = new StacTransactionResponse();
        r.setId(r.getId());
        r.setSuccess(false);
        r.setReason(e.getLocalizedMessage());
        return Mono.just(r);
    }

}
