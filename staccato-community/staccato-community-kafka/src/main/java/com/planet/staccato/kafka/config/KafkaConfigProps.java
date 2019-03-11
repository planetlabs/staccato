package com.planet.staccato.kafka.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for kafka.
 *
 * @author joshfix
 * Created on 1/4/18
 */
@Data
@ConfigurationProperties(prefix = "staccato.kafka")
public class KafkaConfigProps {

    private String bootstrapServers = "localhost:9092";
    private String groupIdConfig = "staccato-group";
    private String clientIdConfig = "staccato-consumer";
    private String autoOffsetResetConfig = "earliest";
    private String topic = "staccato";

}
