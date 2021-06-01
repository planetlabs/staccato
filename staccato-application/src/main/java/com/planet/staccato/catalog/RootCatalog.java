package com.planet.staccato.catalog;

import com.planet.staccato.model.Catalog;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class RootCatalog extends Catalog {

    protected String[] conformsTo;

    public Catalog conformsTo(String[] conformsTo) {
        setConformsTo(conformsTo);
        return this;
    }
}
