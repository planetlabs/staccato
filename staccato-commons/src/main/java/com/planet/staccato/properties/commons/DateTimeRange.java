package com.planet.staccato.properties.commons;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.planet.staccato.elasticsearch.annotation.Mapping;
import com.planet.staccato.elasticsearch.annotation.MappingType;

/**
 * Commons properties extension
 * https://github.com/radiantearth/stac-spec/tree/master/extensions/commons
 *
 * @author joshfix
 * Created on 1/8/20
 */
public interface DateTimeRange {

    @JsonProperty("start_datetime")
    @Mapping(type = MappingType.DATE)
    String getStartDatetime();
    void setStartDatetime(String startDatetime);

    @JsonProperty("end_datetime")
    @Mapping(type = MappingType.DATE)
    String getEndDatetime();
    void setEndDatetime(String endDatetime);
}
