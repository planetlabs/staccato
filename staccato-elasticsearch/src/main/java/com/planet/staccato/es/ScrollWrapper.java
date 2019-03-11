package com.planet.staccato.es;

import com.planet.staccato.model.Item;
import lombok.Data;
import reactor.core.publisher.Flux;

/**
 * Wrapper to enable passing multiple objects in a reactive map/flatmap.
 *
 * @author joshfix
 * Created on 10/16/18
 */
@Data
public class ScrollWrapper {
    private Flux<Item> itemFlux;
    private String scrollId;

    public ScrollWrapper itemFlux(Flux<Item> itemFlux) {
        this.itemFlux = itemFlux;
        return this;
    }

    public ScrollWrapper scrollId(String scrollId) {
        this.scrollId = scrollId;
        return this;
    }
}
