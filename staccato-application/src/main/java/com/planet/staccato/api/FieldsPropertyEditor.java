package com.planet.staccato.api;

import com.planet.staccato.dto.api.extensions.FieldsExtension;

import java.beans.PropertyEditorSupport;

/**
 * The fields extension supports different formats depending on whether the request is GET or POST.  The POST format
 * uses the FieldsExtension object, whereas the GET format is simply an array of Strings and must be parsed.  This
 * property editor will convert the GET format to a FieldsExtension object so that the SearchRequest object only need
 * to define the fields field as a FieldsExtension.
 *
 * @author joshfix
 * Created on 1/15/20
 */
public class FieldsPropertyEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) {
        if (text == null) {
            return;
        }

        String[] fields = text.split(",");
        FieldsExtension fieldsExtension = new FieldsExtension();
        for (String field : fields) {

            if (field.startsWith("-")) {
                // if the field name starts with an dash, it is to be excluded
                fieldsExtension.exclude(field.substring(1));
            } else {
                fieldsExtension.include(field);
            }
        }

        setValue(fieldsExtension);
    }
}
