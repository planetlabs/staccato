package com.planet.staccato.wfs;

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
public class WfsController implements WfsApi {

    private final DefaultWfsService service;

    @Override
    public Mono<Object> getApi() {
        return service.getApi();
    }

    @Override
    public Mono<Conformance> getConformance() {
        return service.getConformanceMono();
    }
}
