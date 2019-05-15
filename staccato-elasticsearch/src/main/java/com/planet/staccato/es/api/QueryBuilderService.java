package com.planet.staccato.es.api;

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

    Optional<QueryBuilder> queryBuilder(String filter);

    Optional<QueryBuilder> idsBuilder(String[] ids);

    int getLimit(Integer limit);

}
