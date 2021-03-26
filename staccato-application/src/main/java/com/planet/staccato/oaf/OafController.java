package com.planet.staccato.oaf;

import com.planet.staccato.model.Conformance;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

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
    public Mono<Conformance> getConformance() {
        return service.getConformanceMono();
    }
}
