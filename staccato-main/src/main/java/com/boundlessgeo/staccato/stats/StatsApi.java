package com.boundlessgeo.staccato.stats;

import com.boundlessgeo.staccato.dto.ItemStatisticsResponse;
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

    @GetMapping("/stac/stats")
    Flux<ItemStatisticsResponse> getStats();

    @GetMapping("/stac/stats/{type}")
    Mono<ItemStatisticsResponse> getStats(@PathVariable("type") String type);

}
