package com.denisarruda.kafkabasic;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerDemoKeys {

    private static final Logger log = LoggerFactory.getLogger(ProducerDemoKeys.class);
    public static void main(String[] args) {
        log.info("Starting Kafka Producer");

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        properties.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < 10; i++) {
                String topic = "third_topic";
                String key = "key_" + i;
                String value = "Hello World from Java callback " + j + ":" + i;

                //Create Producer Record
                ProducerRecord<String, String> record = new org.apache.kafka.clients.producer.ProducerRecord<>(topic, key, value);

                //Send Data - asynchronous
                producer.send(record, new Callback() {
                    @Override
                    public void onCompletion(org.apache.kafka.clients.producer.RecordMetadata metadata, Exception exception) {
                        //executes every time a record is successfully sent or an exception is thrown
                        if (exception == null) {
                            //the record was successfully sent
                            log.info("key: " + key + " | " +
                                    "Partition: " + metadata.partition() + "\n" +
                                    "Partition: " + metadata.partition());
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
