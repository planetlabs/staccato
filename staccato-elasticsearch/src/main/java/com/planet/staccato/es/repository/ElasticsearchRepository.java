package com.planet.staccato.es.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.planet.staccato.FieldName;
import com.planet.staccato.config.LinksConfigProps;
import com.planet.staccato.dto.StacTransactionResponse;
import com.planet.staccato.dto.api.extensions.SortExtension;
import com.planet.staccato.es.QueryBuilderHelper;
import com.planet.staccato.es.ScrollWrapper;
import com.planet.staccato.es.config.ElasticsearchConfigProps;
import com.planet.staccato.es.exception.ItemException;
import com.planet.staccato.model.Item;
import com.planet.staccato.model.ItemCollection;
import com.planet.staccato.model.Link;
import com.planet.staccato.model.Meta;
import joptsimple.internal.Strings;
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

import javax.annotation.PostConstruct;
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
    private final ElasticsearchConfigProps configProps;

    private static final StacTransactionResponse ITEM_NOT_FOUND;
    private static String TYPE;

    static {
        ITEM_NOT_FOUND = new StacTransactionResponse();
        ITEM_NOT_FOUND.setSuccess(false);
        ITEM_NOT_FOUND.setReason("Item not found.");
    }

    @PostConstruct
    public void init() {
        TYPE = configProps.getEs().getType();
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
                .page(0);
        return searchItemFlux(indices, queryBuilder, searchRequest)
                .switchIfEmpty(Mono.error(new RuntimeException("Item with ID '" + id + "' not found.")))
                .single();
    }

    /**
     * Helper method to construct SearchSourceBuilder objects.
     *
     * @param queryBuilder The builder object for the query
     * @param limit        The max number of items to return in the result
     * @param offset       The item offset to retrieve results from
     * @return The builder for the api source
     */
    private SearchSourceBuilder buildSearchSourceBuilder(QueryBuilder queryBuilder, Integer limit, Integer offset) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(queryBuilder)
                .size(limit);
                //.sort(new FieldSortBuilder("properties.datetime").order(SortOrder.DESC));

        return (null == limit || offset <= 1) ? searchSourceBuilder : searchSourceBuilder.from((offset - 1) * limit);
    }

    /**
     * Helper method for constructing api requests.
     *
     * @param builder The builder for the seach source
     * @param indices The list of indices to api
     * @return The constructed api request
     */
    private SearchRequest buildSearchRequest(SearchSourceBuilder builder, Collection<String> indices) {
        return new SearchRequest()
                .types(TYPE)
                .source(builder)
                .indices(indices.toArray(new String[indices.size()]));
    }


    public Mono<ItemCollection> searchItemCollection(Collection<String> indices, QueryBuilder queryBuilder,
                                                     final com.planet.staccato.dto.api.SearchRequest searchRequest) {
        String searchString = buildEsSearchString(indices, queryBuilder, searchRequest);
        final Meta meta = new Meta();
        return client.searchNoScroll(searchString, indices)
                // build a flux from the hits
                .flatMapIterable(response -> {
                    meta.found(response.getHits().getTotalHits())
                            .returned(response.getHits().getHits().length)
                            .limit(searchRequest.getLimit())
                            .page((null == searchRequest.getPage()) ? 1 : searchRequest.getPage());
                    return Arrays.asList(response.getHits().getHits());
                })
                // process all the hits in parallel -- will use all CPU cores by default
                .parallel().runOn(Schedulers.parallel())
                // map each hit to it's source bytes
                .map(hit -> deserializeItem(hit.getSourceRef().toBytesRef().bytes))
                // revert to sequential processing
                .sequential()
                .collectList()
                // take the api list build an item collection from it
                .map(itemList -> {
                    ItemCollection itemCollection = new ItemCollection()
                            .features(itemList)
                            .type(ItemCollection.TypeEnum.FEATURECOLLECTION)
                            .meta(meta);

                    final int nextPage = (null == searchRequest.getPage()) ? 1 : searchRequest.getPage() + 1;
                    int finalLimit = QueryBuilderHelper.getLimit(searchRequest.getLimit());

                    // rebuild the original request link
                    double[] bbox = searchRequest.getBbox();
                    String link = LinksConfigProps.LINK_BASE + "?limit=" + finalLimit;
                    if (null != bbox && bbox.length == 4) {
                        link += bbox == null ? Strings.EMPTY : "&bbox=" + bbox[0] + "," + bbox[1] + "," + bbox[2] + "," + bbox[3];
                    }
                    link += searchRequest.getTime() == null ? Strings.EMPTY : "&time=" + searchRequest.getTime();
                    link += searchRequest.getQuery() == null ? Strings.EMPTY : "&query=" + searchRequest.getQuery();
                    link += searchRequest.getIds() == null ? Strings.EMPTY :
                            "&ids=" + String.join(",", searchRequest.getIds());
                    link += searchRequest.getCollections() == null ? Strings.EMPTY :
                            "&collections=" + String.join(",", searchRequest.getCollections());
                    if (null != searchRequest.getFields()) {

                        link += searchRequest.getFields().getInclude() == null ? Strings.EMPTY :
                                "&fields.include=" + String.join(",", searchRequest.getFields().getInclude());
                        link += searchRequest.getFields().getExclude() == null ? Strings.EMPTY :
                                "&fields.exclude=" + String.join(",", searchRequest.getFields().getExclude());
                    }
                    itemCollection.addLink(new Link()
                            .href(link)
                            .rel("self"));

                    // if the number of api in the collection is less than or equal to the limit, do not provide a next token
                    if (itemCollection.getFeatures().size() >= finalLimit) {
                        itemCollection.addLink(new Link()
                                .href(link + "&page=" + nextPage)
                                .rel("next"));
                    }


                    return itemCollection;
                });
    }

    private void setIncludeExcludeFields(SearchSourceBuilder searchSourceBuilder, com.planet.staccato.dto.api.SearchRequest searchRequest) {
        // if include fieldsExtension were provided, make sure `collection` is present because it's needed for Jackson
        // deserialization to the proper ItemProperties subtype.  then set the includeFields on the api.
        if (null != searchRequest.getFields()) {

            Set<String> includeSet = null;
            if (searchRequest.getFields().getInclude() != null) {
                // don't want to alter the original set as it can be used in filters before the response is returned
                includeSet = new HashSet<>(searchRequest.getFields().getInclude());
            }
            Set<String> excludeSet = searchRequest.getFields().getExclude();
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
    }

    private String buildEsSearchString(Collection<String> indices, QueryBuilder queryBuilder,
                                       com.planet.staccato.dto.api.SearchRequest searchRequest) {
        int limit = QueryBuilderHelper.getLimit(searchRequest.getLimit());
        SearchSourceBuilder searchSourceBuilder = buildSearchSourceBuilder(queryBuilder, limit, searchRequest.getPage());
        configureSort(searchSourceBuilder, searchRequest.getSort());
        setIncludeExcludeFields(searchSourceBuilder, searchRequest);

        SearchRequest esSearchRequest = buildSearchRequest(searchSourceBuilder, indices);
        String searchString = esSearchRequest.source().toString();

        log.debug("Searching ES indices '" + indices.toString() + "' with the following request: \n" + searchString);
        return searchString;
    }

    private void configureSort(SearchSourceBuilder searchSourceBuilder, SortExtension sort) {
        if (sort == null || sort.isEmpty()) {
            searchSourceBuilder.sort(new FieldSortBuilder("properties.datetime").order(SortOrder.DESC));
            return;
        }

        for (SortExtension.SortTerm term : sort) {
            SortOrder sortOrder = (term.getDirection() == SortExtension.SortTerm.SortDirection.DESC) ?
                    SortOrder.DESC : SortOrder.ASC;
            searchSourceBuilder.sort(new FieldSortBuilder(term.getField()).order(sortOrder));
        }
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
     * @param collectionId  The ID of the collection to query
     * @param queryBuilder  The elasticsearch query builder
     * @param searchRequest The original search request
     * @return A {@link ScrollWrapper wrapper} object containing a flux of items and scroll ID
     */
    public ScrollWrapper initialScroll(String collectionId, QueryBuilder queryBuilder,
                                       com.planet.staccato.dto.api.SearchRequest searchRequest) {
        int limit = QueryBuilderHelper.getLimit(searchRequest.getLimit());
        SearchSourceBuilder searchSourceBuilder = buildSearchSourceBuilder(queryBuilder, limit, null);
        configureSort(searchSourceBuilder, searchRequest.getSort());

        SearchRequest esSearchRequest = new SearchRequest()
                .types(TYPE)
                .source(searchSourceBuilder)
                //.scroll(TimeValue.timeValueMillis(300000))
                .indices(collectionId);

        String searchString = esSearchRequest.source().toString();

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
     * @param type  The Elasticsearch type
     * @param item  The item to be added
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
