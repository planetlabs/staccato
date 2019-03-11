package com.planet.staccato.kafka;

import com.planet.staccato.kafka.config.KafkaConfigProps;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.SenderRecord;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Simple test.
 * TODO: remove and create real tests
 * @author joshfix
 * Created on 1/4/18
 */
@Slf4j
public class TestProducer {

    private KafkaSender<Integer, String> sender;

    @Autowired
    private KafkaConfigProps configProps;

    @PostConstruct
    public void init() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, configProps.getBootstrapServers());
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        SenderOptions<Integer, String> senderOptions = SenderOptions.create(props);

        sender = KafkaSender.create(senderOptions);
    }

    public String send() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS z dd MMM yyyy");
        int count = 1;

        String id = UUID.randomUUID().toString();
        log.info("Sending test item with id " + id);
        String value = "{\"id\":\"" + id + "\",\"bbox\":[-114.26459549325727,54.68142259526096,-110.97942772402153,55.78236214244083],\"geometry\":{\"providers\":\"Polygon\",\"coordinates\":[[[-110.97942772402153,54.68142259526096],[-110.97942772402153,55.78236214244083],[-114.26459549325727,55.78236214244083],[-114.26459549325727,54.68142259526096],[-110.97942772402153,54.68142259526096]]]},\"providers\":\"Feature\",\"properties\":{\"start\":\"3000-01-01T00:00:00\",\"end\":\"4000-01-01T00:00:00\",\"providers\":\"DGa\",\"license\":\"to ill\",\"providers\":\"vectorlayer\",\"description\":\"OOOOO\",\"title\":\"Vector Layer\",\"keywords\":[\"vector\",\"layer\",\"awesome\",\"fun\"],\"metadataLinks\":[\"http://www.metadata.com/links\"],\"dataLinks\":[\"http://www.data.com/links\"],\"serviceEndpoints\":[\"http://www.service.com/endpoints\"],\"centroid\":{\"providers\":\"Point\",\"coordinates\":[0.0,0.0]},\"dimension\":11,\"storeType\":\"Best Buy\",\"geometryType\":\"zonotope\"},\"links\":{\"self\":\"http://localhost:8080/0a8c3d8e-fe0d-eee3-914d-4ebdee1c4e60\",\"thumbnail\":\"http://www.planet.com/thumbs/108c3d8d-280d-4ee3-914d-4ebdb71cde6b/img.png\"}}";

        sender.send(Flux.range(1, count)
                .map(i -> SenderRecord.create(new ProducerRecord<>(configProps.getTopic(), i, value), i)))
                .doOnError(e-> log.error("Send failed", e))
                .subscribe(r -> {
                    RecordMetadata metadata = r.recordMetadata();
                    System.out.printf("Message %d sent successfully, topic-partition=%s-%d offset=%d timestamp=%s\n",
                            r.correlationMetadata(),
                            metadata.topic(),
                            metadata.partition(),
                            metadata.offset(),
                            dateFormat.format(new Date(metadata.timestamp())));

                });
        return id;
    }


}
