package com.planet.staccato.properties.extension;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Defines the fieldsExtension and Jackson property values for fieldsExtension in the scientific extension.
 * @see <a href="https://github.com/stac-extensions/processing">Processing Extension</a>
 * @author joshfix
 * Created on 2/11/19
 */
public interface File {

    String EXTENSION_PREFIX = "file";

    @JsonProperty(EXTENSION_PREFIX + ":bits_per_sample")
    Integer getBitsPerSample();
    void setBitsPerSample(Integer bitsPerSample);

    @JsonProperty(EXTENSION_PREFIX + ":byte_order")
    ByteOrderEnum getByteOrder();
    void setByteOrder(ByteOrderEnum byteOrder);

    @JsonProperty(EXTENSION_PREFIX + ":checksum")
    String getChecksum();
    void setChecksum(String checksum);

    @JsonProperty(EXTENSION_PREFIX + ":data_type")
    Map<String, String> getSoftware();
    void setSoftware(Map<String, String> software);

    @JsonProperty(EXTENSION_PREFIX + ":header_size")
    Integer getHeaderSize();
    void setHeaderSize(Integer headerSize);

    @JsonProperty(EXTENSION_PREFIX + ":nodata")
    Object getNoData();
    void setNoData(Object noData);

    @JsonProperty(EXTENSION_PREFIX + ":size")
    Integer getSize();
    void setSize(Integer size);

    @JsonProperty(EXTENSION_PREFIX + ":unit")
    String getUnit();
    void setUnit(String unit);

    @JsonProperty(EXTENSION_PREFIX + ":values")
    MappingObject getValues();
    void SetValues(MappingObject mappingObject);

    @Data
    class MappingObject {
        protected List<Object> values;
        protected String summary;
    }

    enum ByteOrderEnum {
        LITTLE_ENDIAN("little_endian"),
        BIG_ENDIAN("big_endian"),;

        private String value;

        ByteOrderEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static ByteOrderEnum fromValue(String text) {
            for (ByteOrderEnum b : ByteOrderEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }

    }

}

