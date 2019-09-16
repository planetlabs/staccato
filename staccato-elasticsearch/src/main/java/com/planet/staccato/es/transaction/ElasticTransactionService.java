package com.planet.staccato.es.transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.planet.staccato.SerializationUtils;
import com.planet.staccato.dto.StacTransactionResponse;
import com.planet.staccato.es.IndexAliasLookup;
import com.planet.staccato.es.api.ElasticsearchApiService;
import com.planet.staccato.es.config.ElasticsearchConfigProps;
import com.planet.staccato.es.repository.ElasticsearchRepository;
import com.planet.staccato.filters.ItemsFilterProcessor;
import com.planet.staccato.model.Item;
import com.planet.staccato.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Service class implementing logic required to support the transaction extension.
 *
 * @author joshfix
 * Created on 11/29/17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticTransactionService implements TransactionService {

    private final ElasticsearchConfigProps configProps;
    private final ElasticsearchRepository repository;
    private final ObjectMapper mapper;
    private final ItemsFilterProcessor processor;
    private final IndexAliasLookup indexAliasLookup;
    private final ElasticsearchApiService searchService;

    /**
     * Indexes a flux of items into Elasticsearch.
     *
     * @param items The flux of items
     * @return The transaction response object
     */
    @Override
    public Mono<StacTransactionResponse> putItems(Flux<Item> items) {
        return putItems(items, null);
    }

    /**
     * Indexes a flux of items into Elasticsearch.
     *
     * @param items The flux of items
     * @param collectionId The collection ID used to determine which index the items should be inserted into
     * @return The transaction response object
     */
    @Override
    public Mono<StacTransactionResponse> putItems(Flux<Item> items, String collectionId) {
        Flux<Item> itemFlux = processor.indexItemFlux(items);
        return itemFlux
        //return items
                .parallel().runOn(Schedulers.parallel())
                .flatMap(item -> {
                    String index = indexAliasLookup.getWriteAlias(collectionId);
                    log.debug("Found item providers '" + index + "'.");
                    return repository.createItem(index, configProps.getType(), item);
                })
                .sequential()
                .collectList()
                .map(responses -> {
                    StacTransactionResponse stacTransactionResponse = new StacTransactionResponse();
                    stacTransactionResponse.setSuccess(true);
                    stacTransactionResponse.setResult(responses);
                    return stacTransactionResponse;
                })
                .doOnError(e -> {
                    throw new RuntimeException("An error was encountered while attempting to add item. " + e.getMessage());
                });
    }

    /**
     * Deletes an item in Elasticsearch.
     *
     * @param id The ID of the item to be deleted
     * @return The transaction response object
     */
    @Override
    public Mono<StacTransactionResponse> deleteItem(String id) {
        return deleteItem(id, null);
    }

    /**
     * Deletes an item in Elasticsearch.
     *
     * @param id The ID of the item to be deleted
     * @param collectionId The collection ID of the item to be deleted
     * @return The transaction response object
     */
    @Override
    public Mono<StacTransactionResponse> deleteItem(String id, String collectionId) {
        return repository.deleteItem(searchService.getItem(id, collectionId), configProps.getType(), collectionId);
    }

    /**
     * Updates an item in Elasticsearch.
     *
     * @param id The ID of the item to update
     * @param body The JSON representation of the item
     * @return The transaction response object
     */
    @Override
    public Mono<StacTransactionResponse> updateItem(String id, String body) {
        return updateItem(id, body, null);
    }

    /**
     * Updates an item in Elasticsearch.
     *
     * @param id The ID of the item to update
     * @param body The JSON representation of the item
     * @param collectionId The collection ID used to determine which index the items should be inserted into
     * @return The transaction response object
     */
    @Override
    public Mono<StacTransactionResponse> updateItem(String id, String body, String collectionId) {
        body = "{\"doc\":" + body + "}";
        return repository.updateItem(searchService.getItem(id, collectionId), configProps.getType(), body, collectionId);
    }

    /**
     * Updates a flux of items in Elasticsearch using a bulk request.
     *
     * @param items The flux of items.
     * @return The transaction response object
     */
    @Override
    public Mono<StacTransactionResponse> updateItems(Flux<Item> items) {
        return updateItems(items, null);
    }

    /**
     * Updates a flux of items in Elasticsearch using a bulk request.
     *
     * @param items The flux of items.
     * @param collectionId The collection ID used to determine which index the items should be inserted into
     * @return The transaction response object
     */
    @Override
    public Mono<StacTransactionResponse> updateItems(Flux<Item> items, String collectionId) {
        return processor.updateItemFlux(items)
                .collectMultimap(item -> collectionId, item -> {
                    // get all api into a map by providers
                    UpdateRequest updateRequest = new UpdateRequest();
                    updateRequest
                            .doc(SerializationUtils.serializeItem(item, mapper), XContentType.JSON)
                            .index(collectionId)
                            .type(configProps.getType())
                            .id(item.getId());
                    return updateRequest;
                })
                .flatMap(map -> {
                    // add all requests to bulk update and call the repo
                    BulkRequest bulkRequest = new BulkRequest();
                    for (Collection<UpdateRequest> updateRequests : map.values()) {
                        for (UpdateRequest updateRequest : updateRequests) {
                            bulkRequest.add(updateRequest);
                        }
                    }
                    return repository.bulkUpdate(bulkRequest);
                })
                .map(response -> {
                    // build responses for all api updated
                    StacTransactionResponse parentResponse = new StacTransactionResponse();
                    List<StacTransactionResponse> responses = new ArrayList<>();
                    parentResponse.setResult(responses);

                    if (response.hasFailures()) {
                        parentResponse.setSuccess(false);
                        parentResponse.setReason(response.buildFailureMessage());
                    } else {
                        parentResponse.setSuccess(true);
                    }

                    for (BulkItemResponse bulkResponse : response) {
                        StacTransactionResponse r = new StacTransactionResponse();
                        if (null != bulkResponse.getResponse() &&
                                (bulkResponse.getResponse().getResult() == DocWriteResponse.Result.UPDATED /*|| bulkResponse.getResponse().getResult() == DocWriteResponse.Result.NOOP*/)) {
                            r.setSuccess(true);
                            r.setId(bulkResponse.getResponse().getId());
                            r.setResult(bulkResponse.getResponse().getResult());
                        } else {
                            r.setSuccess(false);
                            r.setReason(bulkResponse.getFailureMessage());
                            r.setId(bulkResponse.getFailure().getId());
                        }
                        responses.add(r);
                    }
                    return parentResponse;
                })
                .doOnError(e -> Mono.error(new RuntimeException("No api were able to be updated.  Do the partial "
                        + "json api include both an 'id' and 'properties.providers'?")));
    }

}
