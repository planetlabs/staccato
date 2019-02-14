package com.boundlessgeo.staccato.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * Model for returning aggregation information.
 *
 * @author joshfix
 * @author tingold
 */

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemStatisticsResponse {

    private String type;
    private List<Double> bounds;
    private Long count;
    private String start;
    private String end;
    private List<String> providers;
    private List<String> licenses;

}
