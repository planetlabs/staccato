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
     * Generates the base string for links
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
     * @param id           The ID of the item to retrieve
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
     * @return A flux of items
     */
    @Override
    public Flux<Item> getItemsFlux(SearchRequest request) {
        return getItemsFlux(request.getBbox(), request.getTime(), request.getQuery(), request.getLimit(),
                request.getPage(), request.getIds(), request.getCollections(), request.getPropertyname());
    }

    /**
     * Fetches multiple items that match the provided api request parameters.
     *
     * @param bbox         The bbox parameter provided in the api request
     * @param time         The time parameter provided in the api request
     * @param query        The query parameter provided in the api request
     * @param limit        The limit parameter provided in the api request
     * @param page         The page parameter provided in the api request
     * @param ids          A list of IDs to match
     * @param collections  A list of collections to match
     * @param propertyname The propertyname parameter provided in the api request
     * @return A flux of items
     */
    @Override
    public Flux<Item> getItemsFlux(double[] bbox, String time, String query, Integer limit, Integer page,
                                   String[] ids, String[] collections, String[] propertyname) {
        Set<String> includeFields = (null != propertyname && propertyname.length > 0) ?
                new HashSet(Arrays.asList(propertyname)) : null;

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        limit = queryBuilderService.getLimit(limit);

        Optional<QueryBuilder> bboxBuilder = queryBuilderService.bboxBuilder(bbox);
        if (bboxBuilder.isPresent()) {
            boolQueryBuilder.must(bboxBuilder.get());
        }

        Optional<QueryBuilder> timeBuilder = queryBuilderService.timeBuilder(time);
        if (timeBuilder.isPresent()) {
            boolQueryBuilder.must(timeBuilder.get());
        }

        Optional<QueryBuilder> queryBuilder = queryBuilderService.queryBuilder(query);
        if (queryBuilder.isPresent()) {
            boolQueryBuilder.must(queryBuilder.get());
        }

        Optional<QueryBuilder> idsBuilder = queryBuilderService.idsBuilder(ids);
        if (idsBuilder.isPresent()) {
            boolQueryBuilder.must(idsBuilder.get());
        }

        Set<String> indices = new HashSet<>();
        if (null != collections) {
            for (String collection : collections) {
                indices.add(aliasLookup.getReadAlias(collection));
            }
        } else {
            indices.addAll(aliasLookup.getReadAliases());
        }

        Flux<Item> itemFlux = repository.searchItemFlux(indices, configProps.getEs().getType(),
                boolQueryBuilder, limit, page, includeFields);
        return processor.searchItemFlux(
                itemFlux, SearchRequestUtils.generateSearchRequest(bbox, time, query, limit, page, propertyname));
    }

    /**
     * Fetches multiple items that match the provided api request parameters.
     *
     * @param bbox         The bbox parameter provided in the api request
     * @param time         The time parameter provided in the api request
     * @param query        The query parameter provided in the api request
     * @param limit        The limit parameter provided in the api request
     * @param page         The page parameter provided in the api request
     * @param propertyname The propertyname parameter provided in the api request
     * @return A collection of items wrapped in a mono
     */
    @Override
    public Mono<ItemCollection> getItems(double[] bbox, String time, String query, Integer limit, Integer page,
                                         String[] ids, String[] collections, String[] propertyname) {
        final int nextPage = (null == page) ? 1 : page + 1;
        int finalLimit = queryBuilderService.getLimit(limit);

        return getItemsFlux(bbox, time, query, limit, page, ids, collections, propertyname)
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
                    link += query == null ? Strings.EMPTY : "&query=" + query;
                    link += ids == null ? Strings.EMPTY : "&ids=" + String.join(",", ids);
                    link += collections == null ? Strings.EMPTY : "&collections=" + String.join(",", collections);
                    link += propertyname == null ? Strings.EMPTY : "&propertyname=" + String.join(",", propertyname);

                    itemCollection.addLink(new Link()
                            .href(link)
                            .rel("self"));

                    // if the number of api in the collection is less than or equal to the limit, do not provide a page page token
                    if (itemCollection.getFeatures().size() >= finalLimit) {
                        itemCollection.addLink(new Link()
                                .href(link + "&page=" + nextPage)
                                .rel("page"));
                    }


                    return itemCollection;
                });
    }

}

