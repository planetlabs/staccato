package com.planet.staccato.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * Model for returning stats information.
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

}
