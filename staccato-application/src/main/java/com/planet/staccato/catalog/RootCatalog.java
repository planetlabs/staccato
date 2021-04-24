package com.planet.staccato.catalog;

import com.planet.staccato.model.Catalog;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class RootCatalog extends Catalog {

    protected Object conformsTo;

    public Catalog conformsTo(Object conformsTo) {
        this.conformsTo = conformsTo;
        return this;
    }
}
