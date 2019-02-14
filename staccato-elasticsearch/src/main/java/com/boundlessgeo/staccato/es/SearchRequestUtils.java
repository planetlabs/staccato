package com.boundlessgeo.staccato.es;

import com.boundlessgeo.staccato.dto.SearchRequest;

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
     * @param filter The query api request parameter
     * @param limit The limit api request parameter
     * @param next The next api request parameter
     * @param propertyname The propertyname api request parameter
     * @return The {@link SearchRequest SearchRequest} object
     */
    public static SearchRequest generateSearchRequest(
            double[] bbox, String time, String filter, Integer limit, String next, String[] propertyname) {
        SearchRequest request = new SearchRequest();
        request.setBbox(bbox);
        request.setTime(time);
        request.setQuery(filter);
        request.setLimit(limit);
        request.setNext(next);
        request.setPropertyname(propertyname);
        return request;
    }

}
