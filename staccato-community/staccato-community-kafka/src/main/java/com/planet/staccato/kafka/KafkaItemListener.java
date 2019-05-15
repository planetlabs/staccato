package com.planet.staccato.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.planet.staccato.SerializationUtils;
import com.planet.staccato.StacInitializer;
import com.planet.staccato.exception.SerializationException;
import com.planet.staccato.kafka.config.KafkaConfigProps;
import com.planet.staccato.model.Item;
import com.planet.staccato.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOffset;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates a Kafka listener that expects messages to contain {@link Item items} to be inserted via the transaction
 * service.
 *
 * @author joshfix
 * Created on 1/2/18
 */
@Slf4j
@RequiredArgsConstructor
public class KafkaItemListener implements StacInitializer {

    private final TransactionService transactionService;
    private final ObjectMapper mapper;
    private final KafkaConfigProps configProps;

    @Override
    public String getName() {
        return "KafkaInitializer";
    }

    /**
     * Initializes the listener and calls the transaction service when new messages arrives.
     */
    @Override
    public void init() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS z dd MMM yyyy");

        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, configProps.getBootstrapServers());
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, configProps.getGroupIdConfig());
        consumerProps.put(ConsumerConfig.CLIENT_ID_CONFIG, configProps.getClientIdConfig());
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, configProps.getAutoOffsetResetConfig());
        ReceiverOptions<Integer, String> receiverOptions = ReceiverOptions.create(consumerProps);

        ReceiverOptions<Integer, String> options =
                receiverOptions.subscription(Collections.singleton(configProps.getTopic()))
                .addAssignListener(partitions -> log.debug("onPartitionsAssigned {}", partitions))
                .addRevokeListener(partitions -> log.debug("onPartitionsRevoked {}", partitions));

        Flux<ReceiverRecord<Integer, String>> kafkaFlux = KafkaReceiver.create(options).receive();

        kafkaFlux.subscribe(record -> {
            ReceiverOffset offset = record.receiverOffset();
            offset.acknowledge();

            log.debug(String.format("Received message: topic-partition=%s offset=%d timestamp=%s key=%d value=%s\n",
                    offset.topicPartition(),
                    offset.offset(),
                    dateFormat.format(new Date(record.timestamp())),
                    record.key(),
                    record.value()));

            try {
                Item item = SerializationUtils.deserializeItemFromString(record.value(), mapper);
                transactionService.putItems(Flux.just(item), item.getCollection()).subscribe();
            } catch (Exception e) {
                log.error("Error deserializing Item received from Kafka. ", e);
                throw new SerializationException("Error deserializing Item received from Kafka.");
            }

        });

    }

}
