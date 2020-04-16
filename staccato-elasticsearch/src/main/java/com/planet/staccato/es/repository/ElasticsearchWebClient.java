package com.planet.staccato.es.repository;

import com.planet.staccato.es.config.ElasticsearchConfigProps;
import com.planet.staccato.es.exception.ItemException;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.xcontent.NamedXContentRegistry;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * Provides a list of methods that use Spring's reactive {@link WebClient WebClient} to execute requests against
 * Elasticsearch's REST API.
 *
 * @author joshfix
 * Created on 1/5/18
 */
@Slf4j
@Component
public class ElasticsearchWebClient {

    private final WebClient client;
    public static final int MIB = 1_048_576;
    public static final String SEARCH_EXCEPTION = "Elasticsearch encountered an error executing the api.";
    public static final String DELETE_EXCEPTION = "Elasticsearch encountered an error deleting the item.";
    public static final String INDEX_EXCEPTION = "Elasticsearch encountered an error adding the item.";

    /**
     * Initializes the reactive {@link WebClient WebClient}.
     *
     * @param configProps Configuration properties for Elasticsearch
     */
    public ElasticsearchWebClient(ElasticsearchConfigProps configProps) {
        String esEndpoint = new StringBuilder()
                .append(configProps.getScheme())
                .append("://")
                .append(configProps.getHost())
                .append(":")
                .append(configProps.getPort())
                .toString();
        log.debug("Connecting to Elasticsearch at " + esEndpoint);

        // https://github.com/spring-projects/spring-framework/issues/23961
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(MIB * 500)).build();

        WebClient.Builder builder = WebClient
                .builder()
                .baseUrl(esEndpoint)
                .exchangeStrategies(exchangeStrategies);

        // if a password exists, use it
        if (null != configProps.getUser() && !configProps.getUser().isEmpty()) {
            builder.filter(
                    ExchangeFilterFunctions.basicAuthentication(configProps.getUser(), configProps.getPassword()));
        }

        client = builder.build();
    }

    /**
     * Executes a standard api in Elasticsearch against one ore more indices.
     *
     * @param body The JSON Search request
     * @param indices The indices to api
     * @return The api response object
     */
    public Mono<SearchResponse> search(String body, Collection<String> indices) {
        return searchInternal(body, "/_search?index=" + String.join(",", indices));
    }

    /**
     * Executes a standard api in Elasticsearch against one ore more indices.
     *
     * @param body The JSON Search request
     * @param indices The indices to api
     * @return The api response object
     */
    public Mono<SearchResponse> search(String body, String indices) {
        return searchInternal(body, "/_search?index=" + indices);
    }

    /**
     * Simple Elasticsearch api method.
     * @param body The JSON Search request
     * @param uri The Elasticsearch REST API path
     * @return The api response object
     */
    public Mono<SearchResponse> searchInternal(String body, String uri) {
        return client
                .post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(body), String.class)
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(),
                        response -> {
                            response.toEntity(String.class).subscribe(
                                    entity -> log.error("Elasticsearch client reported status " + response.statusCode() +
                                            ". " + entity.getBody())
                            );
                            return Mono.error(new RuntimeException(SEARCH_EXCEPTION));
                        })
                .bodyToMono(ByteArrayResource.class)
                .map(bar -> deserializeResponse(bar.getByteArray(), SearchResponse.class));
    }

    /**
     * Deletes an item from Elasticsearch.
     *
     * @param index The index of the item
     * @param type The document type of the item
     * @param id The id of theItem
     * @return The delete response object
     */
    public Mono<DeleteResponse> delete(String index, String type, String id) {
        return client
                .delete()
                .uri("/{index}/{type}/{id}", index, type, id)
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(),
                        response -> {
                            response.toEntity(String.class).subscribe(
                                    entity -> log.error("Elasticsearch client reported status " + response.statusCode() +
                                            ". " + entity.getBody())
                            );
                            return Mono.error(new RuntimeException(DELETE_EXCEPTION));
                        })
                .bodyToMono(ByteArrayResource.class)
                .map(bar -> deserializeResponse(bar.getByteArray(), DeleteResponse.class));
    }

    /**
     * Creates a new item in Elasticsearch.
     *
     * @param index The index the item should be created in
     * @param type The document type of the item
     * @param id The id of the item
     * @param item A String containing the JSON representation of the item
     * @return The index response object
     */
    public Mono<IndexResponse> create(String index, String type, String id, String item) {
        return client
                .put()
                .uri("/{index}/{type}/{id}", index, type, id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), String.class)
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(),
                        response -> {
                            response.toEntity(String.class).subscribe(
                                    entity -> log.error("Elasticsearch client reported status " + response.statusCode() +
                                            ". " + entity.getBody())
                            );
                            return Mono.error(new RuntimeException(INDEX_EXCEPTION));
                        })
                .bodyToMono(ByteArrayResource.class)
                //.map(b -> {
                //    log.debug("Index response body: " + new String(b.getByteArray()));
                //    return b;
                //})
                .map(bar -> deserializeResponse(bar.getByteArray(), IndexResponse.class));
    }

    /**
     * Updates an item in Elasticsearch.
     *
     * @param index The index the item should be created in
     * @param type The document type of the item
     * @param id The id of the item
     * @param item A String containing the JSON representation of the item
     * @return The update response object
     */
    public Mono<UpdateResponse> update(String index, String type, String id, String item) {
        return client
                .post()
                .uri("/{index}/{type}/{id}/_update", index, type, id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), String.class)
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(),
                        response -> {
                            response.toEntity(String.class).subscribe(
                                    entity -> log.error("Elasticsearch client reported status " + response.statusCode() +
                                            ". " + entity.getBody())
                            );
                            return Mono.error(new RuntimeException(INDEX_EXCEPTION));
                        })
                .bodyToMono(ByteArrayResource.class)
                .map(b -> {
                    log.error("RESPONSE BODY: " + new String(b.getByteArray()));
                    return b;
                })
                .map(bar -> deserializeResponse(bar.getByteArray(), UpdateResponse.class));
    }

    /**
     * Utility method to deserialize responses from Elasticsearch into the response objects provided by Elasticsearch's
     * Java library.
     *
     * @param bytes The byte array response from the Elasticsearch request
     * @param clazz Class class for the Elasticsearch response class that the method should attempt to deserialize to
     * @param <T> The Elasticsearch response class that the method should attempt to deserialize to
     * @return The deserialized response
     */
    private <T extends ActionResponse> T deserializeResponse(byte[] bytes, Class<T> clazz) {
        try {

            Method fromXContent = clazz.getMethod("fromXContent", XContentParser.class);
            XContentParser parser = XContentFactory
                    .xContent(XContentType.JSON)
                    .createParser(NamedXContentRegistry.EMPTY, null, bytes);
            return (T)fromXContent.invoke(null, parser);
        } catch (Exception e) {
            log.error("Error deserializing Elasticsearch SearchResponse.", e);
            throw new ItemException("An error occurred processing the result from the database.  Please contact an administrator.");
        }
    }

}
