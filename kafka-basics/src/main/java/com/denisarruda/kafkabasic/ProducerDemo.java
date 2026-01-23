package com.denisarruda.kafkabasic;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerDemo {

    private static final Logger log = LoggerFactory.getLogger(ProducerDemo.class);
    public static void main(String[] args) {
        log.info("Starting Kafka Producer");

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        properties.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        //Create Producer Record
        ProducerRecord<String, String> record = new org.apache.kafka.clients.producer.ProducerRecord<>("first_topic", "Hello World from Java");

        //Send Data - asynchronous
        producer.send(record);

        //Flush data
        producer.flush();

        //Close Producer
        producer.close();
    
    }
}
