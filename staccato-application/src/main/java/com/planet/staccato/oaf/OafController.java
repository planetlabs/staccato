package com.planet.staccato.oaf;

import com.planet.staccato.model.Conformance;
import com.planet.staccato.queryables.Queryables;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author joshfix
 * Created on 2019-09-23
 */
@RestController
@RequiredArgsConstructor
public class OafController implements OafApi {

    private final DefaultOafService service;

    @Override
    public Mono<Object> getApi() {
        return service.getApi();
    }

    @Override
    public Mono<List<String>> getConformance() {
        return service.getConformanceMono();
    }

    @Override
    public Flux<Queryables> getQueryables() {
        return service.getQueryables();
    }

}
