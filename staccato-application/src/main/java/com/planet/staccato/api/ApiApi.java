package com.planet.staccato.api;

import com.planet.staccato.dto.api.SearchRequest;
import com.planet.staccato.model.Item;
import com.planet.staccato.model.ItemCollection;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * Defines the controller interface for the STAC API specification
 * @see <a href="https://github.com/radiantearth/stac-spec/tree/master/api-spec">api-spec</a>
 * @author joshfix, tingold
 * Created on 11/29/17
 */
public interface ApiApi {

    @GetMapping(path = "/search/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    Mono<Item> getItem(@PathVariable("id") String id);

    @GetMapping(path = "/search")
    Mono<ItemCollection> getItems(@Valid SearchRequest searchRequest,
                                  @RequestParam(value = "filter-lang", required = false) String filterLang,
                                  @RequestParam(value = "filter-crs", required = false) String filterCrs);

    @GetMapping(path = "/search", produces = {MediaType.TEXT_EVENT_STREAM_VALUE,
            MediaType.APPLICATION_NDJSON_VALUE})
    Flux<Item> getItemsStream(@Valid SearchRequest searchRequest,
                              @RequestParam(value = "filter-lang", required = false) String filterLang,
                              @RequestParam(value = "filter-crs", required = false) String filterCrs);

    @PostMapping(value = "/search", consumes = MediaType.APPLICATION_JSON_VALUE)
    Mono<ItemCollection> getItemsPost(@Valid @RequestBody SearchRequest searchRequest);

    @PostMapping(value = "/search", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = {MediaType.TEXT_EVENT_STREAM_VALUE, MediaType.APPLICATION_NDJSON_VALUE})
    Flux<Item> getItemsPostStream(@Valid @RequestBody SearchRequest searchRequest);

    @PostMapping(value = "/search", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_NDJSON_VALUE)
    Mono<ItemCollection> getItemsFormPost(@Valid @ModelAttribute SearchRequest searchRequest);

}
