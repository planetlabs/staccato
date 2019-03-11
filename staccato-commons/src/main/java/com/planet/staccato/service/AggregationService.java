package com.planet.staccato.service;

import com.planet.staccato.dto.ItemStatisticsResponse;
import com.planet.staccato.model.Extent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Map;

/**
 * Defines the API that must be implemented to provide an AggregationService implementation.
 *
 * @author joshfix
 * Created on 10/17/18
 */
public interface AggregationService {
    Mono<Extent> getExtentMono(String index, Map<String, String> query);

    Extent getExtent(String index, Map<String, String> query);

    Flux<ItemStatisticsResponse> getStats(Collection<String> indices);

    Mono<ItemStatisticsResponse> getStats(String index);
}
