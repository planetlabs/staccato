package com.planet.staccato.es;

import com.planet.staccato.FieldName;
import com.planet.staccato.SearchRequestUtils;
import com.planet.staccato.dto.api.SearchRequest;
import com.planet.staccato.dto.api.extensions.FieldsExtension;
import com.planet.staccato.es.api.PropertiesVisitor;
import com.planet.staccato.es.exception.FilterException;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.geo.ShapeRelation;
import org.elasticsearch.common.geo.builders.*;
import org.elasticsearch.index.query.*;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.stereotype.Service;
import org.xbib.cql.CQLParser;
import org.xbib.cql.SortedQuery;
import org.xbib.cql.elasticsearch.ElasticsearchQueryGenerator;

import java.time.Instant;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This service builds Elasticsearch queries from the api parameters passed to the STAC API.
 *
 * @author joshfix
 * Created on 12/6/17
 */
@Slf4j
@Service
public class QueryBuilderHelper {//implements QueryBuilder {

    private static final int WEST = 0;
    private static final int SOUTH = 1;
    private static final int EAST = 2;
    private static final int NORTH = 3;
    private static final int DEFAULT_LIMIT = 10;
    // https://github.com/radiantearth/stac-spec/blob/v0.8.0/api-spec/STAC-extensions.yaml#L1144
    private static final String OPEN_INTERVAL_SYMBOL = "..";
    private static final List<String> SUPPORTED_CQL_LANGS = Arrays.asList(SearchRequest.FilterLangEnum.CQL_TEXT.getValue());

    public static BoolQueryBuilder buildQuery(double[] bbox, String time, String query, Integer limit, String next,
                                              String[] ids, String[] collections, FieldsExtension fields,
                                              Object intersects) {
        SearchRequest searchRequest = SearchRequestUtils.generateSearchRequest(bbox, time, query, limit, next,
                fields, ids, collections, intersects, null);
        return buildQuery(searchRequest);
    }

    public static BoolQueryBuilder buildQuery(SearchRequest searchRequest) {
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        Optional<QueryBuilder> bboxBuilder = QueryBuilderHelper.bboxBuilder(searchRequest.getBbox());
        if (bboxBuilder.isPresent()) {
            boolQueryBuilder.must(bboxBuilder.get());
        }

        Optional<QueryBuilder> timeBuilder = QueryBuilderHelper.timeBuilder(searchRequest.getDatetime());
        if (timeBuilder.isPresent()) {
            boolQueryBuilder.must(timeBuilder.get());
        }

        String filterLang = searchRequest.getFilterLang() == null ? null : searchRequest.getFilterLang();
        Optional<QueryBuilder> filterBuilder = QueryBuilderHelper.filterBuilder(searchRequest.getFilterCrs(),
                filterLang, searchRequest.getFilter());
        if (filterBuilder.isPresent()) {
            boolQueryBuilder.must(filterBuilder.get());
        }

        Optional<QueryBuilder> idsBuilder = QueryBuilderHelper.idsBuilder(searchRequest.getIds());
        if (idsBuilder.isPresent()) {
            boolQueryBuilder.must(idsBuilder.get());
        }

        Optional<QueryBuilder> intersectsBuilder = QueryBuilderHelper.intersectsBuilder(searchRequest.getIntersects());
        if (intersectsBuilder.isPresent()) {
            boolQueryBuilder.must(intersectsBuilder.get());
        }

        return boolQueryBuilder;
    }

    /**
     * Builds an Elasticsearch bbox query
     *
     * @param bbox The bbox values passed in the api request
     * @return The Elasticsearch query builder
     */

    public static Optional<QueryBuilder> bboxBuilder(double[] bbox) {
        if (null == bbox || (!(bbox.length == 4 || bbox.length == 6))) {
            return Optional.empty();
        }
        Coordinate c1 = new Coordinate(bbox[WEST], bbox[NORTH]);
        Coordinate c2 = new Coordinate(bbox[EAST], bbox[SOUTH]);
        EnvelopeBuilder envelopeBuilder = new EnvelopeBuilder(c1, c2);

        return Optional.of(
                new GeoShapeQueryBuilder(FieldName.GEOMETRY, envelopeBuilder).relation(ShapeRelation.INTERSECTS));
    }

    /**
     * Builds an Elasticsearch temporal query
     *
     * @param time The datetime values passed in the api request
     * @return The Elasticsearch query builder
     */

    public static Optional<QueryBuilder> timeBuilder(String time) {
        if (null == time || time.isBlank()) {
            return Optional.empty();
        }
        String startTimeProperty;
        String endTimeProperty;

        if (time.indexOf("/") > 0) {
            String[] timeArray = time.split("/");
            startTimeProperty = dateStringOrOpenInterval(timeArray[0]);
            endTimeProperty = dateStringOrOpenInterval(timeArray[1]);
            if (endTimeProperty != null) {
                // check if end time is not null;
                // otherwise, NullPointerException will be thrown during parse
                try {
                    // see if the value can be parsed into a period, eg P1Y2M3W4D
                    Period period = Period.parse(endTimeProperty);

                    Instant start = Instant.parse(startTimeProperty);
                    Instant end = start.plus(period);

                    start.plus(period.normalized());

                    startTimeProperty = start.toString();
                    endTimeProperty = end.toString();
                } catch (DateTimeParseException e) {
                    // not a period
                }
            }
        } else {
            startTimeProperty = time;
            endTimeProperty = time;
        }
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders
                .rangeQuery(FieldName.DATETIME_FULL)
                .from(startTimeProperty)
                .to(endTimeProperty);

        return Optional.of(rangeQueryBuilder);
    }

    private static String dateStringOrOpenInterval(String value) {
        if (value.equals(OPEN_INTERVAL_SYMBOL)) {
            return null;
        }
        return value;
    }

    public static Optional<QueryBuilder> intersectsBuilder(Object intersects) {
        if (intersects == null) {
            return Optional.empty();
        }
        Map<String, Object> intersectsMap = (Map<String, Object>) intersects;
        String type = (String) intersectsMap.get("type");
        if (type == null || type.isBlank()) {
            throw new FilterException("Unable to determine geometry type from intersects parameter");
        }

        List coords = (List) intersectsMap.get("coordinates");
        if (coords == null) {
            throw new FilterException("Unable to determine coordinates from intersects parameter");
        }
        if (coords.isEmpty()) {
            return Optional.empty();
        }

        ShapeBuilder shapeBuilder = null;
        switch (type) {
            case "Point":
                shapeBuilder = new PointBuilder((double) coords.get(0), (double) coords.get(1));
                break;
            case "Polygon":
                CoordinatesBuilder polygonCoordsBuilder = new CoordinatesBuilder();
                // TODO this needs to be more robust
                for (Object o : (List) coords.get(0)) {
                    List innerCoords = (List) o;
                    polygonCoordsBuilder.coordinate((double) innerCoords.get(0), (double) innerCoords.get(1));
                }
                shapeBuilder = new PolygonBuilder(polygonCoordsBuilder);
                break;
            case "LineString":
                CoordinatesBuilder lineStringCoordsBuilder = new CoordinatesBuilder();
                for (Object o : coords) {
                    List innerCoords = (List) o;
                    lineStringCoordsBuilder.coordinate((double) innerCoords.get(0), (double) innerCoords.get(1));
                }
                shapeBuilder = new LineStringBuilder(lineStringCoordsBuilder);
                break;
            // TODO implement the rest
            case "MultiPoint":
            case "MultiLineString":
            case "MultiPolygon":
        }

        if (null == shapeBuilder) {
            return Optional.empty();
        }
        return Optional.of(
                new GeoShapeQueryBuilder(FieldName.GEOMETRY, shapeBuilder).relation(ShapeRelation.INTERSECTS));
    }

    /**
     * Builds an Elasticsearch query from the OGC CQL filter spec
     *
     * @param filterCrs  The CRS of the filter as specified by the API paramter "filter-crs"
     * @param filterLang The language of the filter as specified by the API paramter "filter-lang"
     * @param filter     The query filter passed in the api request
     * @return The Elasticsearch query builder
     */
    public static Optional<QueryBuilder> filterBuilder(String filterCrs, String filterLang, String filter) {
        // Until we have an CQL-Elasticsearch library that supports the OGC CQL spec, it is unclear if filterCrs and
        // filterLang will be needed/used in this method.

        if (filter == null || filter.isBlank()) {
            return Optional.empty();
        }

        if (filterLang != null && !filterLang.isBlank() && !SUPPORTED_CQL_LANGS.contains(filterLang.toLowerCase())) {
            throw new FilterException(String.format("provided filter-lang value '%s' is not supported. supported filter-langs are: '%s'",
                    filterLang,
                    String.join("\', \'", SUPPORTED_CQL_LANGS)));
        } else if (filterLang == null) {
            filterLang = SearchRequest.FilterLangEnum.CQL_TEXT.getValue();
        }

        try {
            CQLParser parser = new CQLParser(filter);
            parser.parse();
            ElasticsearchQueryGenerator generator = new ElasticsearchQueryGenerator();
            SortedQuery sq = parser.getCQLQuery();
            sq.getQuery().getScopedClause().accept(new PropertiesVisitor());
            sq.accept(generator);
            QueryBuilder builder = QueryBuilders.wrapperQuery(generator.getQueryResult());
            return Optional.of(builder);
        } catch (Exception e) {
            throw new FilterException("Error parsing query.");
        }
    }

    public static Optional<QueryBuilder> idsBuilder(String[] ids) {
        if (null == ids || ids.length == 0) {
            return Optional.empty();
        }
        return Optional.of(QueryBuilders.termsQuery("id", ids));
    }

    /**
     * Sets a default item limit if none was set or if the set limit is invalid.
     *
     * @param limit The maximum number of items passed in the request
     * @return The final maximum number of items that will be returned in the api response
     */
    public static int getLimit(Integer limit) {
        return (null == limit || limit <= 0) ? DEFAULT_LIMIT : limit;
    }

}
