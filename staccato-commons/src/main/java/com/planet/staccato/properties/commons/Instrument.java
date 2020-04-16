package com.planet.staccato.properties.commons;

import com.planet.staccato.elasticsearch.annotation.Mapping;
import com.planet.staccato.elasticsearch.annotation.MappingType;

import java.util.List;

/**
 * Commons properties extension
 * https://github.com/radiantearth/stac-spec/tree/master/extensions/commons
 *
 * @author joshfix
 * Created on 1/8/20
 */
public interface Instrument {

    @Mapping(type = MappingType.KEYWORD)
    String getPlatform();
    void setPlatform(String platform);

    @Mapping(type = MappingType.KEYWORD)
    List<String> getInstruments();
    void setInstruments(List<String> instruments);

    @Mapping(type = MappingType.KEYWORD)
    String getConstellation();
    void setConstellation(String constellation);

    @Mapping(type = MappingType.KEYWORD)
    String getMission();
    void setMission(String mission);
}
