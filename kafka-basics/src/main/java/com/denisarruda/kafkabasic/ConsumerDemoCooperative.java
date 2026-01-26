package com.denisarruda.kafkabasic;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerDemoCooperative {

    private static final Logger log = LoggerFactory.getLogger(ConsumerDemoCooperative.class);
    public static void main(String[] args) {
        log.info("Starting Kafka Consumer");

        String groupId = "my-java-group";

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("group.id", groupId);
        properties.setProperty("auto.offset.reset", "earliest");
        properties.setProperty("partition.assignment.strategy", "org.apache.kafka.clients.consumer.CooperativeStickyAssignor");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        // Shutdown hook
        final Thread mainThread = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                log.info("Detected a shutdown, let's exit by calling consumer.wakeup()...");
                consumer.wakeup();

                try {
                    mainThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        consumer.subscribe(java.util.Collections.singletonList("third_topic"));

        // Poll for new data
        try {
            while (true) {
                log.info("Polling");
                ConsumerRecords<String, String> records = consumer.poll(java.time.Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> record : records) {
                    log.info("Key: {} | Value: {} | Partition: {} | Offset: {}", record.key(), record.value(), record.partition(), record.offset());
                }
            }
        } catch (org.apache.kafka.common.errors.WakeupException e) {
            log.info("WakeupException caught, shutting down consumer...");
        } catch (Exception e) {
            log.error("Unexpected exception in consumer loop", e);
        } finally {
            consumer.close();
            log.info("Consumer closed gracefully");
        }
    }
}
