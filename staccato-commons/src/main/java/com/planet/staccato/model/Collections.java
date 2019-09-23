package com.planet.staccato.model;

import com.planet.staccato.collection.CollectionMetadata;
import lombok.Data;

import java.util.List;

/**
 * @author joshfix
 * Created on 2019-09-23
 */
@Data
public class Collections {

    private List<Link> links;
    private List<CollectionMetadata> collections;

    public Collections links(List<Link> links) {
        setLinks(links);
        return this;
    }

    public Collections collections(List<CollectionMetadata> collections) {
        setCollections(collections);
        return this;
    }
}
