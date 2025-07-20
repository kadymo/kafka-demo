package com.backend.producer.service;

import com.backend.producer.config.KafkaTopics;
import com.backend.producer.model.OrderEvent;
import com.backend.producer.model.UserEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class EventProducerService {
    private static final Logger logger = LoggerFactory.getLogger(EventProducerService.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public EventProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public CompletableFuture<SendResult<String, Object>> sendUserEvent(UserEvent userEvent) {
        logger.info("Sending user event: ", userEvent);

        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(
                KafkaTopics.USER_EVENTS,
                userEvent.getUserId(),
                userEvent
        );

        future.whenComplete((result, ex) -> {
           if (ex == null) {
               logger.info("User event sent successfully: topic={}, partition={}, offset={}, key={}",
                       result.getRecordMetadata().topic(),
                       result.getRecordMetadata().partition(),
                       result.getRecordMetadata().offset(),
                       userEvent.getUserId());
           } else {
               logger.error("User event sent failed: ", ex.getMessage(), ex);
           }
        });

        return future;
    }

    public CompletableFuture<SendResult<String, Object>> sendOrderEvent(OrderEvent orderEvent) {
        logger.info("Sending order event: ", orderEvent);
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(
                KafkaTopics.ORDER_EVENTS,
                orderEvent.getOrderId(),
                orderEvent
        );

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                logger.info("Order event sent successfully: topic={}, partition={}, offset={}, key={}",
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset(),
                        orderEvent.getOrderId());
            } else {
                logger.error("Order event sent failed: ", ex.getMessage(), ex);
            }
        });

        return future;
    }
}
