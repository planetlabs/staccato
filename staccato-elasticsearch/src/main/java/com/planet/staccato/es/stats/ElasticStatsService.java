package com.planet.staccato.es.stats;

import com.planet.staccato.FieldName;
import com.planet.staccato.dto.ItemStatisticsResponse;
import com.planet.staccato.es.QueryBuilderHelper;
import com.planet.staccato.model.Extent;
import com.planet.staccato.service.AggregationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.geobounds.GeoBounds;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.min.Min;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

/**
 * Providers stats data from Elasticsearch.
 *
 * @author joshfix
 * Created on 10/17/18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticStatsService implements AggregationService {

    private final RestHighLevelClient restClient;
    private final Scheduler scheduler;

    /**
     * Creates a non-blocking reactive method for the {@link #getExtent(String, Map) getExtent}  method
     * @param index The Elasticsearch index to api
     * @param query The query that should be executed against the provided index
     * @return The extent of the items matched during the api wrapped in a Mono
     */
    @Override
    public Mono<Extent> getExtentMono(String index, Map<String, String> query) {
        return Mono.fromCallable(() -> getExtent(index, query)).subscribeOn(scheduler).publishOn(Schedulers.single());
    }

    /**
     * Populates an {@link Extent} object with the bounding box for the items matched in the provided query.
     *
     * @param index The Elasticsearch index to api
     * @param query The query that should be executed against the provided index
     * @return The extent of the items matched during the api
     */
    @Override
    public Extent getExtent(String index, Map<String, String> query) {
        StringBuilder sb = new StringBuilder();
        if (null != query) {

            Iterator<Map.Entry<String, String>> it = query.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry entry = it.next();
                sb.append("properties." + entry.getKey() + "=" + entry.getValue());
                if (it.hasNext()) {
                    sb.append(" AND ");
                }
            }
        }

        Optional<QueryBuilder> filter = QueryBuilderHelper.filterBuilder(null, null, sb.toString());
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        if (filter.isPresent()) {
            searchSourceBuilder.query(filter.get());
        }

        searchSourceBuilder
                .aggregation(AggregationBuilders.min("start").field(FieldName.DATETIME_FULL))
                .aggregation(AggregationBuilders.max("end").field(FieldName.DATETIME_FULL))
                .aggregation(AggregationBuilders.geoBounds("bbox").field(FieldName.CENTROID))
                //we don't want records back, just aggregations
                .size(0);

        SearchRequest sr = new SearchRequest().indices(index).source(searchSourceBuilder);

        log.debug("Getting stats on {} with the following request: \n {}", index, sr.source().toString());
        SearchResponse resp;
        try {
            resp = restClient.search(sr);
        } catch (Exception ex) {
            throw new RuntimeException("Error getting aggregations.", ex);
        }

        Min start = resp.getAggregations().get("start");
        Max end = resp.getAggregations().get("end");
        GeoBounds bboxAggregation = resp.getAggregations().get("bbox");

        Extent extent = new Extent();
        List<String> temporalExtent = new ArrayList<>(2);

        Consumer<String> temporalCheck = value -> {
            if (null != value && !value.toLowerCase().contains("infinity")) {
                temporalExtent.add(value);
            } else {
                temporalExtent.add(null);
            }
        };

        temporalCheck.accept(start.getValueAsString());
        temporalCheck.accept(end.getValueAsString());
        extent.getTemporal().getInterval().add(temporalExtent);

        List<Double> bbox = getBboxExtent(bboxAggregation);
        if (bbox != null && (bbox.size() == 4 || bbox.size() == 6)) {
            extent.getSpatial().getBbox().add(bbox);
        }

        return extent;
    }

    protected List<Double> getBboxExtent(GeoBounds bboxAggregation) {

        try {
            List<Double> bbox = new ArrayList<>();
            bbox.add(bboxAggregation.topLeft().getLon());
            bbox.add(bboxAggregation.bottomRight().getLat());
            bbox.add(bboxAggregation.bottomRight().getLon());
            bbox.add(bboxAggregation.topLeft().getLat());
            return bbox;
        } catch (Exception e) {
            // do nothing -- index is empty
        }
        return null;
    }

    /**
     * Uses the {@link #getStats(String) getStats} method to build aggregations for multiple indices
     * @param indices The collection of indices to build aggregations for
     * @return A flux of {@link ItemStatisticsResponse ItemStatisticsResponse} objects
     */
    @Override
    public Flux<ItemStatisticsResponse> getStats(Collection<String> indices) {
        return Flux.fromIterable(indices).flatMap(e -> getStats(e));
    }

    /**
     * Reactive, non-blocking wrapper for the {@link #getStats(String) getStats} method
     * @param index The index to build stats data for
     * @return An {@link ItemStatisticsResponse ItemStatisticsResponse} object wrapped in a Mono
     */
    @Override
    public Mono<ItemStatisticsResponse> getStats(String index) {
        return Mono.fromCallable(() -> getStatsSync(index)).subscribeOn(scheduler).publishOn(Schedulers.single());
    }

    /**
     * Blocking method to fetch aggregations from Elasticsearch.
     *
     * @param index The index to build aggregations for.
     * @return An {@link ItemStatisticsResponse ItemStatisticsResponse} object containing the aggregated data for the index
     */
    private ItemStatisticsResponse getStatsSync(String index) {
        SearchRequest sr = new SearchRequest().indices(index).source(
                new SearchSourceBuilder()
                        .aggregation(AggregationBuilders.min("start").field(FieldName.DATETIME_FULL))
                        .aggregation(AggregationBuilders.max("end").field(FieldName.DATETIME_FULL))
                        //.stats(AggregationBuilders.terms("licenses").field(FieldName.LICENSE_FULL).size(5).order(BucketOrder.count(true)))
                        //.stats(AggregationBuilders.terms("providers").field(FieldName.PROVIDER_FULL).size(5).order(BucketOrder.count(true)))
                        .aggregation(AggregationBuilders.geoBounds("bbox").field(FieldName.CENTROID))
                        //we don't want records back, just aggregations
                        .size(0)
        );
        log.debug("Getting stats on {} with the following request: \n {}", index, sr.source().toString());

        SearchResponse resp;
        try {
            resp = restClient.search(sr, RequestOptions.DEFAULT);
        } catch (IOException ex) {
            return new ItemStatisticsResponse();
        }

        Min start = resp.getAggregations().get("start");
        Max end = resp.getAggregations().get("end");
        //Terms lics = resp.getAggregations().get("licenses");
        //Terms provs = resp.getAggregations().get("providers");
        GeoBounds bboxAggregation = resp.getAggregations().get("bbox");

        ItemStatisticsResponse stats = new ItemStatisticsResponse();
        stats.setType(index);
        stats.setCount(resp.getHits().totalHits);

        String endValue = end.getValueAsString();
        if (null != endValue && !endValue.toLowerCase().contains("infinity")) {
            stats.setEnd(endValue);
        }

        String startValue = start.getValueAsString();
        if (null != startValue && !startValue.toLowerCase().contains("infinity")) {
            stats.setStart(startValue);
        }

        List<Double> bbox = getBboxExtent(bboxAggregation);
        if (bbox != null && (bbox.size() == 4 || bbox.size() == 6)) {
            stats.setBounds(bbox);
        }

        return stats;
    }
}
