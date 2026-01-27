package com.denisarruda.kafka.wikimedia;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.MessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WikimediaChangeHandler implements EventHandler {

    private static final Logger log = LoggerFactory.getLogger(WikimediaChangeHandler.class);

    private final KafkaProducer<String, String> producer;
    private final String topic;

    public WikimediaChangeHandler(KafkaProducer<String, String> producer, String topic) {
        this.producer = producer;
        this.topic = topic;
    }

    @Override
    public void onOpen() throws Exception {
        // nothing to do
    }

    @Override
    public void onClosed() throws Exception {
        producer.close();
    }

    @Override
    public void onMessage(String event, MessageEvent message) throws Exception {
        log.info("Event received: " + message.getData());
        producer.send(new ProducerRecord<>(topic, message.getData()));
    }

    @Override
    public void onComment(String comment) throws Exception {
        // nothing to do
    }

    @Override
    public void onError(Throwable t) {
        log.error("Error in WikimediaChangeHandler", t);
    }
}