package com.planet.staccato.es.api;

import com.planet.staccato.config.LinksConfigProps;
import com.planet.staccato.dto.SearchRequest;
import com.planet.staccato.es.IndexAliasLookup;
import com.planet.staccato.es.SearchRequestUtils;
import com.planet.staccato.es.config.ElasticsearchConfigProps;
import com.planet.staccato.es.repository.ElasticsearchRepository;
import com.planet.staccato.filter.ItemsFilterProcessor;
import com.planet.staccato.model.Item;
import com.planet.staccato.model.ItemCollection;
import com.planet.staccato.model.Link;
import com.planet.staccato.service.ApiService;
import joptsimple.internal.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Service class implementing logic required to support the STAC api API.
 *
 * @author joshfix
 * Created on 10/17/18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticsearchApiService implements ApiService {

    private final ElasticsearchConfigProps configProps;
    private final ElasticsearchRepository repository;
    private final QueryBuilderService queryBuilderService;
    private final ItemsFilterProcessor processor;
    private final IndexAliasLookup aliasLookup;
    private final LinksConfigProps linksConfigProperties;
    private static String LINK_BASE;

    /**
     *  Generates the base string for links
     */
    @PostConstruct
    public void init() {
        LinksConfigProps.Self self = linksConfigProperties.getSelf();
        LINK_BASE = self.getScheme() + "://" + self.getHost();
        if (self.getPort() != 80) {
            LINK_BASE += ":" + self.getPort();
        }
        LINK_BASE += "/stac/api"; // generate the base string for links
    }

    /**
     * Fetches a single item.
     *
     * @param id The ID of the item to retrieve
     * @param collectionId The collection of the item to retrieve
     * @return The item wrapped in a mono
     */
    @Override
    public Mono<Item> getItem(String id, String collectionId) {
        List<String> indices = (collectionId == null) ? aliasLookup.getReadAliases() : Arrays.asList(collectionId);
        Mono<Item> itemMono = repository.searchById(indices, configProps.getEs().getType(), id);
        return processor.searchItemMono(itemMono, new SearchRequest());
    }

    /**
     * Fetches multiple items that match the provided api request.
     *
     * @param request The {@link SearchRequest SearchRequest} object containing all api parameters
     * @param collectionId The collection of the item to retrieve
     * @return A flux of items
     */
    @Override
    public Flux<Item> getItemsFlux(SearchRequest request, String collectionId) {
        return getItemsFlux(request.getBbox(), request.getTime(), request.getQuery(), request.getLimit(),
                request.getNext(), request.getPropertyname(), collectionId);
    }

    /**
     * Fetches multiple items that match the provided api request parameters.
     *
     * @param bbox The bbox parameter provided in the api request
     * @param time The time parameter provided in the api request
     * @param filter The query parameter provided in the api request
     * @param limit The limit parameter provided in the api request
     * @param next The next parameter provided in the api request
     * @param propertyname The propertyname parameter provided in the api request
     * @param collectionId The ID of the collection to api.  If null, all collections will be searched.
     * @return A flux of items
     */
    @Override
    public Flux<Item> getItemsFlux(double[] bbox, String time, String filter, Integer limit, String next,
                                   String[] propertyname, String collectionId) {
        Set<String> includeFields = (null != propertyname && propertyname.length > 0) ?
                new HashSet(Arrays.asList(propertyname)) : null;

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        int offset = queryBuilderService.getOffset(next);
        limit = queryBuilderService.getLimit(limit);


        /**
         * Get QueryBuilder for bbox
         */
        if (null != bbox && bbox.length == 4) {
            Optional<QueryBuilder> queryBuilder = queryBuilderService.bboxBuilder(bbox);
            if (queryBuilder.isPresent()) {
                boolQueryBuilder.must(queryBuilder.get());
            }
        }

        /**
         * Get QueryBuilder for time
         */
        if (null != time) {
            Optional<QueryBuilder> queryBuilder = queryBuilderService.timeBuilder(time);
            if (queryBuilder.isPresent()) {
                boolQueryBuilder.must(queryBuilder.get());
            }
        }

        /**
         * Get QueryBuilder for query
         */
        if (null != filter && !filter.isEmpty()) {
            Optional<QueryBuilder> queryBuilder = queryBuilderService.searchBuilder(filter);
            if (queryBuilder.isPresent()) {
                boolQueryBuilder.must(queryBuilder.get());
            }
        }

        List<String> indices = (collectionId == null) ? aliasLookup.getReadAliases() : Arrays.asList(collectionId);

        Flux<Item> itemFlux = repository.searchItemFlux(indices, configProps.getEs().getType(),
                boolQueryBuilder, limit, offset, includeFields);
        return processor.searchItemFlux(
                itemFlux, SearchRequestUtils.generateSearchRequest(bbox, time, filter, limit, next, propertyname));
    }

    /**
     * Fetches multiple items that match the provided api request parameters.
     *
     * @param bbox The bbox parameter provided in the api request
     * @param time The time parameter provided in the api request
     * @param filter The query parameter provided in the api request
     * @param limit The limit parameter provided in the api request
     * @param next The next parameter provided in the api request
     * @param propertyname The propertyname parameter provided in the api request
     * @param collectionId The ID of the collection to api.  If null, all collections will be searched.
     * @return A collection of items wrapped in a mono
     */
    @Override
    public Mono<ItemCollection> getItems(double[] bbox, String time, String filter, Integer limit, String next,
                                         String[] propertyname, String collectionId) {
        int offset = queryBuilderService.getOffset(next);
        final String nextOffset = (null == next) ? "1" : String.valueOf(offset + 1);
        int finalLimit = queryBuilderService.getLimit(limit);

        return getItemsFlux(bbox, time, filter, limit, next, propertyname, collectionId)
                .collectList()
                // take the api list build an item collection from it
                .map(itemList -> {
                    ItemCollection itemCollection = new ItemCollection()
                            .features(itemList)
                            .type(ItemCollection.TypeEnum.FEATURECOLLECTION);

                    // rebuild the original request link

                    String link = LINK_BASE + "?limit=" + finalLimit;
                    if (null != bbox && bbox.length == 4) {

                        link += bbox == null ? Strings.EMPTY : "&bbox=" + bbox[0] + "," + bbox[1] + "," + bbox[2] + "," + bbox[3];
                    }

                    link += time == null ? Strings.EMPTY : "&time=" + time;
                    link += filter == null ? Strings.EMPTY : "&query=" + filter;
                    link += propertyname == null ? Strings.EMPTY : "&propertyname=" + propertyname;

                    itemCollection.addLink(new Link()
                            .href(link)
                            .rel("self"));

                    // if the number of api in the collection is less than or equal to the limit, do not provide a next page token
                    if (itemCollection.getFeatures().size() >= finalLimit) {
                        itemCollection.addLink(new Link()
                                .href(link + "&next=" + nextOffset)
                                .rel("next"));
                    }



                    return itemCollection;
                });
    }

}
