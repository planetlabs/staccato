package com.planet.staccato;

import com.planet.staccato.dto.api.SearchRequest;
import com.planet.staccato.dto.api.extensions.FieldsExtension;
import com.planet.staccato.dto.api.extensions.SortExtension;

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
     * @param bbox   The bbox api request parameter
     * @param time   The datetime api request parameter
     * @param filter  The filter api request parameter
     * @param limit  The limit api request parameter
     * @param next   The next api request parameter
     * @param fields The fields extension api request parameter
     * @return The {@link SearchRequest SearchRequest} object
     */
    public static SearchRequest generateSearchRequest(
            double[] bbox, String time, String filter, Integer limit, String next, FieldsExtension fields,
            String[] ids, String[] collections, Object intersects, SortExtension sort) {
        SearchRequest request = new SearchRequest();
        if (null != bbox && (bbox.length == 4 || bbox.length == 6)) {
            request.setBbox(bbox);
        }
        if (null != time && !time.isBlank()) {
            request.setDatetime(time);
        }
        if (null != filter && !filter.isBlank()) {
            request.setFilter(filter);
        }
        if (null != limit) {
            request.setLimit(limit);
        }
        if (null != next) {
            request.setNext(next);
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
        if (null != sort) {
            request.setSortby(sort);
        }
        return request;
    }

}
