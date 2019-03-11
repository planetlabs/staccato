package com.planet.staccato.stats;

import com.planet.staccato.dto.ItemStatisticsResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controller interface for returning aggregations on data.
 *
 * @author joshfix, tingold
 * Created on 11/29/17
 */
public interface StatsApi {

    @GetMapping(path = "/stac/stats", produces = MediaType.APPLICATION_JSON_VALUE)
    Flux<ItemStatisticsResponse> getStats();

    @GetMapping(path = "/stac/stats/{collection}", produces = MediaType.APPLICATION_JSON_VALUE)
    Mono<ItemStatisticsResponse> getStats(@PathVariable("collection") String collection);

}
