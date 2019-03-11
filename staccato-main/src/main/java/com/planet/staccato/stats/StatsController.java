package com.planet.staccato.stats;

import com.planet.staccato.dto.ItemStatisticsResponse;
import com.planet.staccato.es.IndexAliasLookup;
import com.planet.staccato.service.AggregationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controller implementation for returning aggregations on data.
 *
 * @author joshfix
 * Created on 11/29/17
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class StatsController implements StatsApi {

    private final AggregationService service;
    private final IndexAliasLookup indexAliasLookup;

    @Override
    public Flux<ItemStatisticsResponse> getStats() {
        return service.getStats(indexAliasLookup.getWriteAliases()).name("getStats");
    }

    @Override
    public Mono<ItemStatisticsResponse> getStats(@PathVariable("collection") String collection) {
        return service.getStats(collection).name("getStats");
    }

}
