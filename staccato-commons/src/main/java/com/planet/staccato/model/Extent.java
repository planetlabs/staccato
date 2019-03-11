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

    private List<Double> spatial = new ArrayList<>(4);
    private List<String> temporal = new ArrayList<>(2);

}
