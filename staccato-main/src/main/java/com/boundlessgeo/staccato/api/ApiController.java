package com.boundlessgeo.staccato.api;

import com.boundlessgeo.staccato.model.Item;
import com.boundlessgeo.staccato.model.ItemCollection;
import com.boundlessgeo.staccato.service.ApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *  Defines the controller interface for the STAC API specification
 *  @see <a href="https://github.com/radiantearth/stac-spec/tree/master/api-spec">api-spec</a>
 *
 * @author joshfix
 * Created on 11/29/17
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/stac")
public class ApiController implements ApiApi {

    private final ApiService service;

    @Override
    public Mono<Item> getItem(@PathVariable("id") String id) {
        return service.getItem(id, null).name("getItem");
    }


    @Override
    public Mono<ItemCollection> getItems(double[] bbox, String time, String query, Integer limit, String next, String[] propertyname) {
        return service.getItems(bbox, time, query, limit, next, propertyname, null).name("getItems");
    }

    @Override
    public Flux<Item> getItemsStream(double[] bbox, String time, String query, Integer limit, String next, String[] propertyname) {
        return service.getItemsFlux(bbox, time, query, limit, next, propertyname, null);
    }

}
