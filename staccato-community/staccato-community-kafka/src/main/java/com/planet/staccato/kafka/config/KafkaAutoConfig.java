package com.planet.staccato.kafka.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.planet.staccato.kafka.KafkaItemListener;
import com.planet.staccato.kafka.TestProducer;
import com.planet.staccato.service.TransactionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for Kafka plugin.  Creates the Spring beans necessary to receive Items from a Kafka topic.
 * @author joshfix
 * Created on 1/2/18
 */
@Slf4j
@Configuration
@AllArgsConstructor
@ConditionalOnProperty(prefix = "staccato.kafka", value = "enabled", havingValue = "true")
public class KafkaAutoConfig {

    private final TransactionService transactionService;
    private final ObjectMapper mapper;

    @Bean
    public KafkaConfigProps kafkaConfigProps() {
        return new KafkaConfigProps();
    }

    @Bean
    public KafkaItemListener kafkaItemListener() {
        return new KafkaItemListener(transactionService, mapper, kafkaConfigProps());
    }

    @Bean
    public TestProducer testProducer() {
        return new TestProducer();
    }

}
