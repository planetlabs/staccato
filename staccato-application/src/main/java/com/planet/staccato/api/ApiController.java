package com.planet.staccato.api;

import com.planet.staccato.dto.api.SearchRequest;
import com.planet.staccato.dto.api.extensions.FieldsExtension;
import com.planet.staccato.dto.api.extensions.SortExtension;
import com.planet.staccato.model.Item;
import com.planet.staccato.model.ItemCollection;
import com.planet.staccato.service.ApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
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
public class ApiController implements ApiApi {

    private SortbyPropertyEditor sortbyPropertyEditor = new SortbyPropertyEditor();
    private FieldsPropertyEditor fieldsPropertyEditor = new FieldsPropertyEditor();
    private final ApiService service;

    /**
     * Registers a property editor to convert sortby parameter value for GET requests to SortbyExtension object.
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(SortExtension.class, "sortby", sortbyPropertyEditor);
        binder.registerCustomEditor(FieldsExtension.class, "fields", fieldsPropertyEditor);
    }

    @Override
    public Mono<Item> getItem(@PathVariable("id") String id) {
        return service.getItem(id, null).name("getItem");
    }

    @Override
    public Mono<ItemCollection> getItems(SearchRequest searchRequest,
                                         @RequestParam(value = "filter-lang", required = false) String filterLang,
                                         @RequestParam(value = "filter-crs", required = false) String filterCrs) {
        searchRequest
                .method(HttpMethod.GET.toString())
                .filterLang(filterLang)
                .filterCrs(filterCrs);
        return service.getItemCollection(searchRequest).name("getItems");
    }

    @Override
    public Flux<Item> getItemsStream(SearchRequest searchRequest,
                                     @RequestParam(value = "filter-lang", required = false) String filterLang,
                                     @RequestParam(value = "filter-crs", required = false) String filterCrs) {
        searchRequest
                .method(HttpMethod.GET.toString())
                .filterLang(filterLang)
                .filterCrs(filterCrs);
        return service.getItemsFlux(searchRequest);
    }

    @Override
    public Mono<ItemCollection> getItemsPost(@RequestBody SearchRequest searchRequest) {
        searchRequest.setMethod(HttpMethod.POST.toString());
        return service.getItemCollection(searchRequest).name("getItemsPost");
    }

    @Override
    public Flux<Item> getItemsPostStream(SearchRequest searchRequest) {
        searchRequest.setMethod(HttpMethod.POST.toString());
        return service.getItemsFlux(searchRequest);
    }

    @Override
    public Mono<ItemCollection> getItemsFormPost(@ModelAttribute SearchRequest searchRequest) {
        searchRequest.setMethod(HttpMethod.POST.toString());
        return service.getItemCollection(searchRequest).name("getItemsFormPost");
    }
}
