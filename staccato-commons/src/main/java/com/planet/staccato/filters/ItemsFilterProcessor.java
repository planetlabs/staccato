package com.planet.staccato.filters;

import com.planet.staccato.dto.api.SearchRequest;
import com.planet.staccato.model.Item;
import com.planet.staccato.model.ItemCollection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * This service will handle any filters that implement the {@link ItemIndexFilter}, {@link ItemSearchFilter}, and
 * {@link ItemUpdateFilter} and are registered in Spring's context.  Each query is executed at a different point in the
 * request chain.  ItemIndexFilters are executed before items are inserted into the database.  ItemSearchFilters are
 * executed just before the api results are returned to the client.  ItemUpdateFilters are similar to
 * ItemIndexFilters, but only execute for update operations.
 *
 * @author joshfix
 * Created on 1/30/18
 */
@Slf4j
@Service
public class ItemsFilterProcessor {

    @Autowired(required = false)
    private List<ItemIndexFilter> indexFilters = new ArrayList<>();

    @Autowired(required = false)
    private List<ItemUpdateFilter> updateFilters = new ArrayList<>();

    @Autowired(required = false)
    private List<ItemSearchFilter> searchFilters = new ArrayList<>();

    private Function<Item, Item> indexItemFunction = (item) ->  {
        String collection = item.getCollection();
        for (ItemIndexFilter filter : indexFilters) {
            if (filter.types().contains(collection) || filter.types().contains("*")) {
                filter.doFilter(item);
            }
        }
        return item;
    };

    private Function<Item, Item> updateItemFunction = (item) ->  {
        String collection = item.getCollection();
        for (ItemUpdateFilter filter : updateFilters) {
            if (filter.types().contains(collection) || filter.types().contains("*")) {
                filter.doFilter(item);
            }
        }
        return item;
    };

    private BiFunction<Item, SearchRequest, Item> searchItemFunction = (item, request) ->  {
        String collection = item.getCollection();
        for (ItemSearchFilter filter : searchFilters) {
            if (filter.types().contains(collection) || filter.types().contains("*")) {
                filter.doFilter(item, request);
            }
        }
        return item;
    };

    private BiFunction<ItemCollection, SearchRequest, ItemCollection> searchItemCollectionFunction =
            (itemCollection, request) -> {
        for (Item item : itemCollection.getFeatures()) {
            searchItemFunction.apply(item, request);
        }
        return itemCollection;
    };

    public Flux<Item> indexItemFlux(Flux<Item> itemFlux) {
        return (null == indexFilters) ? itemFlux : itemFlux.map(indexItemFunction);
    }

    public Flux<Item> updateItemFlux(Flux<Item> itemFlux) {
        return (null == updateFilters) ? itemFlux : itemFlux.map(updateItemFunction);
    }

    public Flux<Item> searchItemFlux(Flux<Item> itemFlux, SearchRequest request) {
        return (null == searchFilters) ? itemFlux : itemFlux.map(item -> searchItemFunction.apply(item, request));
    }

    public Mono<Item> searchItemMono(Mono<Item> itemMono, SearchRequest request) {
        return (null == searchFilters) ? itemMono : itemMono.map(item -> searchItemFunction.apply(item, request));
    }

    public Mono<ItemCollection> searchItemCollectionMono(Mono<ItemCollection> itemCollectionMono, SearchRequest request) {
        return (null == searchFilters) ? itemCollectionMono : itemCollectionMono.map(ic ->
                searchItemCollectionFunction.apply(ic, request));
    }

}
