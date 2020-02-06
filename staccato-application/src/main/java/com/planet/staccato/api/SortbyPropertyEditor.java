package com.planet.staccato.api;

import com.planet.staccato.dto.api.extensions.SortExtension;
import com.planet.staccato.dto.api.extensions.SortExtension.SortTerm;

import java.beans.PropertyEditorSupport;

/**
 * The sortby extension supports different formats depending on whether the request is GET or POST.  The POST format
 * uses the SortExtension object, whereas the GET format is simply an array of Strings and must be parsed.  This
 * property editor will convert the GET format to a SortExtension object so that the SearchRequest object only need
 * to define the sortby field as a SortExtension.
 *
 * @author joshfix
 * Created on 1/15/20
 */
public class SortbyPropertyEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) {
        if (text == null) {
            return;
        }

        String[] sortby = text.split(",");
        SortExtension sortExtension = new SortExtension();

        for (String field : sortby) {
            if (field.startsWith("-")) {
                // if the field name starts with an dash, it is to be excluded
                sortExtension.add(new SortTerm(field.substring(1), SortTerm.SortDirection.DESC));
            } else if (field.startsWith("+")) {
                sortExtension.add(new SortTerm(field.substring(1), SortTerm.SortDirection.ASC));
            } else {
                sortExtension.add(new SortTerm(field, SortTerm.SortDirection.ASC));
            }
        }

        setValue(sortExtension);
    }
}
