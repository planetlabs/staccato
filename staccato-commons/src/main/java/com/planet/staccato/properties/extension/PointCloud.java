package com.planet.staccato.properties.extension;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.planet.staccato.elasticsearch.annotation.Mapping;
import com.planet.staccato.elasticsearch.annotation.MappingType;
import lombok.Data;

import java.util.List;

/**
 * @author joshfix
 * Created on 2019-05-15
 */
public interface PointCloud {

    String EXTENSION_PREFIX = "pc";

    @Mapping(type = MappingType.INTEGER)
    @JsonProperty(EXTENSION_PREFIX + ":count")
    Integer getCount();
    void setCount(Integer count);

    @Mapping(type = MappingType.KEYWORD)
    @JsonProperty(EXTENSION_PREFIX + ":type")
    String getType();
    void setType(String type);

    @Mapping(type = MappingType.KEYWORD)
    @JsonProperty(EXTENSION_PREFIX + ":encoding")
    String getEncoding();
    void setEncoding(String encoding);

    @Mapping(type = MappingType.DOUBLE)
    @JsonProperty(EXTENSION_PREFIX + ":density")
    String getDensity();
    void setDensity(String density);

    @JsonProperty(EXTENSION_PREFIX + ":schemas")
    List<Schema> getSchemas();
    void setSchemas(List<Schema> schemas);

    @JsonProperty(EXTENSION_PREFIX + ":statistics")
    Statistics getStatistics();
    void setStatistics(Statistics statistics);

    @Data
    class Schema {
        private String name;
        private Integer size;
        private String type;
    }

    @Data
    class Statistics {
        private Double average;
        private Integer count;
        private Double maximum;
        private Double minimum;
        private String name;
        private Integer position;
        private Double stddev;
        private Double variance;
    }

}
