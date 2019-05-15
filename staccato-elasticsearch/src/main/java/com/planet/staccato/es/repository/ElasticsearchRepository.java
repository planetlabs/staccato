package com.planet.staccato.es.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.planet.staccato.FieldName;
import com.planet.staccato.dto.StacTransactionResponse;
import com.planet.staccato.es.ScrollWrapper;
import com.planet.staccato.es.exception.ItemException;
import com.planet.staccato.model.Item;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.*;

/**
 * This class provides the logic for methods that would be expected to be found in a repository service for performing
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

    private static final StacTransactionResponse ITEM_NOT_FOUND;

    static {
        ITEM_NOT_FOUND = new StacTransactionResponse();
        ITEM_NOT_FOUND.setSuccess(false);
        ITEM_NOT_FOUND.setReason("Item not found.");
    }

    /**
     * Retries a specific item by index, providers and Id.
     *
     * @param indices Elasticsearch index.
     * @param type    Search providers query.
     * @param id      Id for the desired entity.
     * @return A Map representation of the Entity.
     */
    public Mono<Item> searchById(List<String> indices, String type, String id) {
        QueryBuilder queryBuilder = QueryBuilders.termQuery(FieldName.ID, id);
        return searchItemFlux(indices, type, queryBuilder, 1, 0, null)
                .switchIfEmpty(Mono.error(new RuntimeException("Item with ID '" + id + "' not found.")))
                .single();
    }

    /**
     * Helper method to construct SearchSourceBuilder objects.
     *
     * @param queryBuilder The builder object for the query
     * @param limit The max number of items to return in the result
     * @param offset The item offset to retrieve results from
     * @return The builder for the api source
     */
    private SearchSourceBuilder buildSearchSourceBuilder(QueryBuilder queryBuilder, Integer limit, Integer offset) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(queryBuilder)
                .size(limit)
                .sort(new FieldSortBuilder("properties.datetime").order(SortOrder.DESC));

        return (null == limit || null == offset) ? searchSourceBuilder : searchSourceBuilder.from(offset * limit);
    }

    /**
     * Helper method for constructing api requests.
     *
     * @param builder The builder for the seach source
     * @param indices The list of indices to api
     * @param type The Elasticsearch type
     * @return The constructed api request
     */
    private SearchRequest buildSearchRequest(SearchSourceBuilder builder, Collection<String> indices, String type) {
        return new SearchRequest()
                .types(type)
                .source(builder)
                .indices(indices.toArray(new String[indices.size()]));
    }

    /**
     * Code to build the ES api request, reactively submit it to ES and deserialize the results.
     *
     * @param indices The indices to api
     * @param type The Elasticsearch type
     * @param queryBuilder The builder for the query
     * @param limit The max number of items to return in the result
     * @param offset The item offset to retrieve results from
     * @return A Flux of items
     */
    public Flux<Item> searchItemFlux(
            Collection<String> indices, String type, QueryBuilder queryBuilder, Integer limit, Integer offset,
            Set<String> includeFields) {

        SearchSourceBuilder searchSourceBuilder = buildSearchSourceBuilder(queryBuilder, limit, offset);

        // if include fields were provided, make sure properties.providers is present because it's needed for Jackson
        // deserialization to the proper ItemProperties subtype.  then set the includeFields on the api.
        if (null != includeFields && !includeFields.isEmpty()) {
            includeFields.add("properties.collection");
            searchSourceBuilder.fetchSource(includeFields.toArray(new String[includeFields.size()]), null);
        }

        SearchRequest searchRequest = buildSearchRequest(searchSourceBuilder, indices, type);
        String searchString = searchRequest.source().toString();

        log.debug("Searching ES indices '" + indices.toString() + "' with the following request: \n" + searchString);

        return client.searchNoScroll(searchString, indices)
                // build a flux from the hits
                .flatMapIterable(response -> Arrays.asList(response.getHits().getHits()))
                // process all the hits in parallel -- will use all CPU cores by default
                .parallel().runOn(Schedulers.parallel())
                // map each hit to it's source bytes
                .map(hit -> deserializeItem(hit.getSourceRef().toBytesRef().bytes))
                // revert to sequential processing
                .sequential();
    }

    /**
     * Retrieves the first page of a set of paginated item results
     *
     * @param collectionId The ID of the collection to query
     * @param limit The maximum number of items that should be returned in the query
     * @return A {@link ScrollWrapper wrapper} object containing a flux of items and scroll ID
     */
    public ScrollWrapper initialScroll(String collectionId, int limit) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder
                .size(limit)
                .sort(new FieldSortBuilder("properties.datetime").order(SortOrder.DESC));

        SearchRequest searchRequest = new SearchRequest()
                .types("item")
                .source(searchSourceBuilder)
                //.scroll(TimeValue.timeValueMillis(300000))
                .indices(collectionId);

        String searchString = searchRequest.source().toString();

        log.debug("Searching ES indices '" + collectionId + "' with the following request: \n" + searchString);

        final ScrollWrapper wrapper = new ScrollWrapper();
        Flux<Item> itemFlux = client.search(searchString, Arrays.asList(collectionId))
                .map(response -> {
                    if (response.getHits().totalHits > limit) {
                        wrapper.scrollId(response.getScrollId());
                    }
                    return response;
                })
                // build a flux from the hits
                .flatMapIterable(response -> Arrays.asList(response.getHits().getHits()))
                // process all the hits in parallel -- will use all CPU cores by default
                .parallel().runOn(Schedulers.parallel())
                // map each hit to it's source bytes
                .map(hit -> deserializeItem(hit.getSourceRef().toBytesRef().bytes))
                // revert to sequential processing
                .sequential();
        //.doOnNext(item -> log.debug(item.toString()));
        return wrapper.itemFlux(itemFlux);
    }

    /**
     * Uses the ES scroll API to retrieve a flux of items
     *
     * @param page The scroll ID
     * @return A flux of items
     */
    public Flux<Item> scroll(Integer page) {
        SearchScrollRequest scrollRequest = new SearchScrollRequest(String.valueOf(page));
        scrollRequest.scroll(TimeValue.timeValueMillis(300000));
        String searchString = "{\"scroll\":\"5m\",\"scroll_id\": \"" + page + "\"}";
        //final ScrollWrapper wrapper = new ScrollWrapper();
        return client.searchScroll(searchString)
                // build a flux from the hits
                .flatMapIterable(response -> Arrays.asList(response.getHits().getHits()))
                // process all the hits in parallel -- will use all CPU cores by default
                .parallel().runOn(Schedulers.parallel())
                // map each hit to it's source bytes
                .map(hit -> deserializeItem(hit.getSourceRef().toBytesRef().bytes))
                // revert to sequential processing
                .sequential()
                .doOnError(e -> log.error("error in scroll request", e));
        //.doOnNext(item -> log.debug(item.toString()));
    }

    /**
     * Adds a new Item to the provided Elasticsearch index.
     *
     * @param index The Elasticsearch index to add the item to
     * @param type The Elasticsearch type
     * @param item The item to be added
     * @return The {@link StacTransactionResponse response} of the operation wrapped in a mono
     */
    public Mono<StacTransactionResponse> createItem(String index, String type, Item item) {
        Optional<String> optionalJson = serializeItem(item);
        if (!optionalJson.isPresent()) {
            StacTransactionResponse stacTransactionResponse = new StacTransactionResponse();
            stacTransactionResponse.setSuccess(false);
            stacTransactionResponse.setReason("Unable to serialize item.  Does the data conform to the schema? Item: " + item);
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
     * @param itemMono The item to be updated, wrapped in a mono
     * @param type The Elasticsearch type
     * @param body The updated item body sent in the request.  This should just be the item JSON.
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
     * @param itemMono The item to be updated, wrapped in a mono
     * @param type The Elasticsearch type
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
    private Mono<StacTransactionResponse> buildErrorResponse(Throwable e) {
        StacTransactionResponse r = new StacTransactionResponse();
        r.setId(r.getId());
        r.setSuccess(false);
        r.setReason(e.getLocalizedMessage());
        return Mono.just(r);
    }

    /**
     * Helper method to serialize an item to a JSON string.
     *
     * @param item The item to be serialized
     * @return The resultant JSON string
     */
    private Optional<String> serializeItem(Item item) {
        try {
            return Optional.of(mapper.writeValueAsString(item));
        } catch (JsonProcessingException e) {
            log.error("Error serializing Item to JSON string. " + item.toString(), e);
        }
        return Optional.empty();
    }

    /**
     * Helper method to deserialize an item from a byte array.
     *
     * @param bytes The item's byte array
     * @return The resultant {@link Item item}
     */
    private Item deserializeItem(byte[] bytes) {
        try {
            return mapper.readValue(bytes, Item.class);
        } catch (Exception e) {
            log.error("Error deserializing hit for item: \n" + new String(bytes), e);
            throw new ItemException("Error processing results.  Please contact an administrator with the details of your api parameters.");
        }
    }

}
