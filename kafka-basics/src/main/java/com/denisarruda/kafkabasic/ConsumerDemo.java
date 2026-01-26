package com.denisarruda.kafkabasic;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerDemo {

    private static final Logger log = LoggerFactory.getLogger(ConsumerDemo.class);
    public static void main(String[] args) {
        log.info("Starting Kafka Consumer");

        String groupId = "my-java-group";

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("group.id", groupId);
        properties.setProperty("auto.offset.reset", "earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(java.util.Collections.singletonList("first_topic"));

        // Poll for new data
        while (true) {
            log.info("Polling");
            ConsumerRecords<String, String> records = consumer.poll(java.time.Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : records) {
                log.info("Key: {} | Value: {} | Partition: {} | Offset: {}", record.key(), record.value(), record.partition(), record.offset());
            }
        }
        // consumer.close(); // Not reached in infinite loop, but should be called on shutdown in real apps
    }
}
