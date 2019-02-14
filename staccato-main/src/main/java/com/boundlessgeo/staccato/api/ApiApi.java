package com.boundlessgeo.staccato.api;

import com.boundlessgeo.staccato.model.Item;
import com.boundlessgeo.staccato.model.ItemCollection;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    Mono<ItemCollection> getItems(@RequestParam(value = "bbox", required = false) double[] bbox,
                                  @RequestParam(value = "time", required = false) String time,
                                  @RequestParam(value = "query", required = false) String query,
                                  @RequestParam(value = "limit", required = false) Integer limit,
                                  @RequestParam(value = "next", required = false) String next,
                                  @RequestParam(value = "propertyname", required = false) String[] propertyname);

    @GetMapping(path = "/search", produces = {MediaType.TEXT_EVENT_STREAM_VALUE, MediaType.APPLICATION_STREAM_JSON_VALUE})
    Flux<Item> getItemsStream(double[] bbox, String time, String query, Integer limit, String next, String[] propertyname);

}
