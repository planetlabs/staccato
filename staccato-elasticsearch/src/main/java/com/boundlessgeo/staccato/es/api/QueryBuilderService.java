package com.boundlessgeo.staccato.es.api;

import org.elasticsearch.index.query.QueryBuilder;

import java.util.Optional;

/**
 * Defines the interface for providing a QueryBuilderService
 *
 * @author joshfix
 * Created on 12/6/17
 */
public interface QueryBuilderService {

    Optional<QueryBuilder> bboxBuilder(double[] bbox);

    Optional<QueryBuilder> timeBuilder(String time);

    Optional<QueryBuilder> searchBuilder(String filter);

    int getOffset(String next);

    int getLimit(Integer limit);

}
