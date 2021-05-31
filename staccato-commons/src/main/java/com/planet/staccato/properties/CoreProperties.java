package com.planet.staccato.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.planet.staccato.elasticsearch.annotation.Mapping;
import com.planet.staccato.elasticsearch.annotation.MappingType;
import com.planet.staccato.model.Provider;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import java.util.List;

/**
 * @author joshfix
 * Created on 10/15/18
 */
public interface CoreProperties {

    // Core
    @Mapping(type = MappingType.DATE)
    String getDatetime();
    void setDatetime(String datetime);

    // Metadata
    @Mapping(type = MappingType.DATE)
    String getCreated();
    void setCreated(String created);

    @Mapping(type = MappingType.DATE)
    String getUpdated();
    void setUpdated(String updated);

    // DateTime
    @JsonProperty("start_datetime")
    @Mapping(type = MappingType.DATE)
    String getStartDatetime();
    void setStartDatetime(String startDatetime);

    @JsonProperty("end_datetime")
    @Mapping(type = MappingType.DATE)
    String getEndDatetime();
    void setEndDatetime(String endDatetime);

    // Instrument
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

    // Licensing
    @Mapping(type = MappingType.KEYWORD)
    String getLicense();
    void setLicense(String license);

    // Provider
    List<Provider> getProviders();
    void setProviders(List<Provider> providers);
}

