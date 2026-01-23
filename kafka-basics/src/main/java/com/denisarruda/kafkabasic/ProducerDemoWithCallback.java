package com.denisarruda.kafkabasic;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerDemoWithCallback {

    private static final Logger log = LoggerFactory.getLogger(ProducerDemoWithCallback.class);
    public static void main(String[] args) {
        log.info("Starting Kafka Producer");

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        properties.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        properties.setProperty("batch.size", "400");

        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        // Outer loop for 10 iterations
        for (int j = 0; j < 10; j++) {
            // Send 30 records in a loop
            for (int i = 0; i < 30; i++) {
                //Create Producer Record
                ProducerRecord<String, String> record = new org.apache.kafka.clients.producer.ProducerRecord<>("second_topic", "Hello World from Java callback " + j + ":" + i);

                //Send Data - asynchronous
                producer.send(record, new Callback() {
                    @Override
                    public void onCompletion(org.apache.kafka.clients.producer.RecordMetadata metadata, Exception exception) {
                        //executes every time a record is successfully sent or an exception is thrown
                        if (exception == null) {
                            //the record was successfully sent
                            log.info("Received new metadata. \n" +
                                    "Topic: " + metadata.topic() + "\n" +
                                    "Partition: " + metadata.partition() + "\n" +
                                    "Offset: " + metadata.offset() + "\n" +
                                    "Timestamp: " + metadata.timestamp());
                        } else {
                            log.error("Error while producing", exception);
                        }
                    }
                });
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        //Flush data
        producer.flush();

        //Close Producer
        producer.close();
    
    }
}
