package com.boundlessgeo.staccato.extension;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Collection;

/**
 * Defines the fields and Jackson property values for fields in the scientific extension.
 * @see <a href="https://github.com/radiantearth/stac-spec/tree/master/extensions/scientific">Scientific Extension</a>
 * @author joshfix
 * Created on 2/11/19
 */
public interface Scientific {

    @JsonProperty("sci:doi")
    String getDoi();
    void setDoi(String doi);

    @JsonProperty("sci:citation")
    String getCitation();
    void setCitation(String citation);

    @JsonProperty("sci:publications")
    Collection<Publication> getPublications();
    void setPublications(Collection<Publication> publications);

    @Data
    class Publication {
        private String doi;
        private String citation;
    }
}
