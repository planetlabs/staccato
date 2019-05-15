package com.planet.staccato.es.api;

import lombok.extern.slf4j.Slf4j;
import org.xbib.cql.*;

import java.lang.reflect.Field;

/**
 * STAC query strings should only be applied to properties, however the CQL to Elasticsearch query code operates against
 * the entire document.  Therefore we must insert the prefix "properties." in front of every clause to ensure the
 * query is only executed against properties.
 *
 * @author joshfix
 * Created on 2019-05-14
 */
@Slf4j
public class PropertiesVisitor implements Visitor {

    public static final String PROPERTIES_PREFIX = "properties.";

    @Override
    public void visit(ScopedClause node) {
        if (node.getScopedClause() != null) {
            visit(node.getScopedClause());
        }

        Index index = node.getSearchClause().getIndex();

        // if the clause already starts with "properties", just continue
        if (index.getContext() != null && index.getContext().equalsIgnoreCase("properties")) {
            return;
        }

        try {
            Field nameField = Index.class.getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(index, PROPERTIES_PREFIX + index.getName());
        } catch (Exception e) {
            log.debug("Unable to set properties prefix on CQL clause: " + node.getSearchClause().toString());
        }
    }

    @Override
    public void visit(SortedQuery node) {}

    @Override
    public void visit(Query node) {}

    @Override
    public void visit(PrefixAssignment node) {}

    @Override
    public void visit(BooleanGroup node) {}

    @Override
    public void visit(SearchClause node) {}

    @Override
    public void visit(Relation node) {}

    @Override
    public void visit(Modifier node) {}

    @Override
    public void visit(ModifierList node) {}

    @Override
    public void visit(Term node) {}

    @Override
    public void visit(Identifier node) {}

    @Override
    public void visit(Index node) {}

    @Override
    public void visit(SimpleName node) {}

    @Override
    public void visit(SortSpec node) {}

    @Override
    public void visit(SingleSpec node) {}
}
