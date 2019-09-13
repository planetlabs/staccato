package com.planet.staccato.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author joshfix
 * Created on 10/15/18
 */
@Data
public class Extent {

    private Spatial spatial = new Spatial();
    private Temporal temporal = new Temporal();

    @Data
    public static class Spatial {
        List<List<Double>> bbox = new ArrayList<>();
    }

    @Data
    public static class Temporal {
        List<List<String>> interval = new ArrayList<>();
    }
}
