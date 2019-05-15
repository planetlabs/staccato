package com.planet.staccato.extension;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.planet.staccato.elasticsearch.annotation.Mapping;
import com.planet.staccato.elasticsearch.annotation.MappingType;

/**
 * Defines the fields and Jackson property values for fields in the datetime range extension.
 * @see <a href="https://github.com/radiantearth/stac-spec/tree/master/extensions/datetime-range">Datetime Range Extension</a>
 * @author joshfix
 * Created on 2/11/19
 */
public interface DatetimeRange {

    @Mapping(type = MappingType.DATE)
    @JsonProperty("dtr:start_datetime")
    String getStartDatetime();
    void setStartDatetime(String startDatetime);

    @JsonProperty("dtr:end_datetime")
    String getEndDatetime();
    void setEndDatetime(String endDatetime);

}
