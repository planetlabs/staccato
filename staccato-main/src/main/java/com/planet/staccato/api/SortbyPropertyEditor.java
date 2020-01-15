package com.planet.staccato.api;

import com.planet.staccato.dto.api.extensions.SortExtension;

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
        String[] sortby = text.split(",");
        SortExtension sortExtension = new SortExtension();
        for (String sortElement : sortby) {
            if (sortElement.contains("|")) {
                String[] sortElementArray = sortElement.split("\\|");
                if (sortElementArray.length == 2) {
                    sortExtension.add(new SortExtension.SortTerm()
                            .field(sortElementArray[0])
                            .direction(sortElementArray[1]));
                }
            }
        }
        setValue(sortExtension);
    }
}
