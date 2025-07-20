package com.backend.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {
    @KafkaListener(topics = "topicName", groupId = "foo")
    public void listenerGroupFoo(String message) {
        System.out.println("Received message in group foo: " + message);
    }
}

