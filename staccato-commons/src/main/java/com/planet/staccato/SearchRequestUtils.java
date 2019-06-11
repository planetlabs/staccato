package com.planet.staccato;

import com.planet.staccato.dto.api.SearchRequest;

/**
 * Utility class for creating {@link SearchRequest SearchRequest} objects.
 *
 * @author joshfix
 * Created on 10/17/18
 */
public class SearchRequestUtils {

    /**
     * Creates a {@link SearchRequest SearchRequest} object from a list of api request parameters.
     *
     * @param bbox The bbox api request parameter
     * @param time The time api request parameter
     * @param query The query api request parameter
     * @param limit The limit api request parameter
     * @param page The page api request parameter
     * @param fields The fields api request parameter
     * @return The {@link SearchRequest SearchRequest} object
     */
    public static SearchRequest generateSearchRequest(
            double[] bbox, String time, String query, Integer limit, Integer page, String[] fields, String[] ids, String[] collections, Object intersects) {
        SearchRequest request = new SearchRequest();
        if (null != bbox && bbox.length == 4) {
            request.setBbox(bbox);
        }
        if (null != time && !time.isBlank()) {
            request.setTime(time);
        }
        if (null != query && !query.isBlank()) {
            request.setQuery(query);
        }
        if (null != limit) {
            request.setLimit(limit);
        }
        if (null != page) {
            request.setPage(page);
        }
        if (null != fields) {
            request.setFields(fields);
        }
        if (null != ids && ids.length > 0) {
            request.setIds(ids);
        }
        if (null != collections && collections.length > 0) {
            request.setCollections(collections);
        }
        if (null != intersects) {
            request.setIntersects(intersects);
        }
        return request;
    }

}
