package com.backend.consumer.listener;

import com.backend.consumer.model.OrderEvent;
import com.backend.consumer.service.EventProcessingService;
import com.backend.consumer.service.OrderEventProcessingService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {
    private static final Logger logger = LoggerFactory.getLogger(UserEventListener.class);

    private final OrderEventProcessingService orderEventProcessingService;
    private final EventProcessingService eventProcessingService;

    public OrderEventListener(OrderEventProcessingService orderEventProcessingService, EventProcessingService eventProcessingService) {
        this.orderEventProcessingService = orderEventProcessingService;
        this.eventProcessingService = eventProcessingService;
    }

    @KafkaListener(
            topics = "order-events",
            groupId = "event-processing-group-order-events",
            containerFactory = "orderEventKafkaListenerContainerFactory"
    )
    public void handleOrderEvent(@Payload OrderEvent orderEvent,
                                 @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                 @Header(KafkaHeaders.RECEIVED_PARTITION) String partition,
                                 @Header(KafkaHeaders.OFFSET) long offset,
                                 ConsumerRecord<String, OrderEvent> record,
                                 Acknowledgment acknowledgment) {
        logger.info("Received order event: topic={}, partition={}, offset={}, key={}, event={}", topic, partition, offset, record.key(), orderEvent);
        try {
            if (eventProcessingService.isEventAlreadyProcessed(orderEvent.getEventId())) {
                logger.info("Event {} already processed, ignoring", orderEvent.getEventId());
                acknowledgment.acknowledge();
                return;
            }

            switch (orderEvent.getEventType()) {
                case "CREATED":
                    orderEventProcessingService.processOrderCreatedEvent(orderEvent);
                    break;
                case "UPDATED":
                    orderEventProcessingService.processOrderUpdatedEvent(orderEvent);
                    break;
                case "CANCELLED":
                    orderEventProcessingService.processOrderCancelledEvent(orderEvent);
                case "COMPLETED":
                    orderEventProcessingService.processOrderCompletedEvent(orderEvent);
                default:
                    logger.warn("Unknown event type: {}", orderEvent.getEventType());
            }

            eventProcessingService.markEventAsProcessed(
                    orderEvent.getEventId(),
                    orderEvent.getEventType(),
                    topic,
                    partition,
                    offset
            );

            acknowledgment.acknowledge();

            logger.info("Order Event {} processed successfully", orderEvent.getEventId());
        } catch (Exception e) {
            logger.error("Error processing order event {}: {}", orderEvent.getEventId(), e.getMessage(), e);

            eventProcessingService.markEventAsError(
                    orderEvent.getEventId(),
                    orderEvent.getEventType(),
                    topic,
                    partition,
                    offset,
                    e.getMessage()
            );

            // Without acknowledge
            throw e;

        }
    }
}
