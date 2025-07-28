package com.backend.consumer.listener;

import com.backend.consumer.model.UserEvent;
import com.backend.consumer.service.EventProcessingService;
import com.backend.consumer.service.UserEventProcessingService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.internals.events.EventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class UserEventListener {
    private static final Logger logger = LoggerFactory.getLogger(UserEventListener.class);

    private final UserEventProcessingService userEventProcessingService;
    private final EventProcessingService eventProcessingService;

    public UserEventListener(UserEventProcessingService userEventProcessingService, EventProcessingService eventProcessingService) {
        this.userEventProcessingService = userEventProcessingService;
        this.eventProcessingService = eventProcessingService;
    }

    // Principal Listener
    @KafkaListener(
            topics = "user-events",
            groupId = "event-processing-group-user-events",
            containerFactory = "userEventKafkaListenerContainerFactory"
    )
    public void handleUserEvent(@Payload UserEvent userEvent,
                                @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                @Header(KafkaHeaders.RECEIVED_PARTITION) String partition,
                                @Header(KafkaHeaders.OFFSET) long offset,
                                ConsumerRecord<String, UserEvent> record,
                                Acknowledgment acknowledgment
                                ) {
        logger.info("Received user event: topic={}, partition={}, offset={}, key={}, event={}", topic, partition, offset, record.key(), userEvent);
        try {
            if (eventProcessingService.isEventAlreadyProcessed(userEvent.getEventId())) {
                logger.info("Event {} already processed, ignoring.", userEvent.getEventId());
                acknowledgment.acknowledge();
                return;
            }

            switch (userEvent.getEventType()) {
                case "LOGIN":
                    userEventProcessingService.processLoginEvent(userEvent);
                    break;
                case "LOGOUT":
                    userEventProcessingService.processLogoutEvent(userEvent);
                    break;
                case "REGISTER":
                    userEventProcessingService.processRegisterEvent(userEvent);
                    break;
                default:
                    userEventProcessingService.processGenericUserEvent(userEvent);
            }

            eventProcessingService.markEventAsProcessed(
                    userEvent.getEventId(),
                    userEvent.getEventType(),
                    topic,
                    partition,
                    offset
            );

            acknowledgment.acknowledge();
            logger.info("User Event {} processed successfully", userEvent.getEventId());
        } catch (Exception e) {
            logger.error("Error while processing user event {}: {}", userEvent.getEventId(), e.getMessage());

            eventProcessingService.markEventAsError(
                    userEvent.getEventId(),
                    userEvent.getEventType(),
                    topic,
                    partition,
                    offset,
                    e.getMessage()
            );

            throw e;
        }
    }

    @KafkaListener(
            topics = "user-events",
            groupId = "login-analytics-group",
            containerFactory = "userEventKafkaListenerContainerFactory"
    )
    public void handleLoginEventForAnalytics(@Payload UserEvent userEvent,
                                             @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                             @Header(KafkaHeaders.RECEIVED_PARTITION) String partition,
                                             @Header(KafkaHeaders.OFFSET) long offset,
                                             Acknowledgment acknowledgment) {

        logger.info("Processing login for analytics: userId={}, timestamp={}", userEvent.getUserId(), userEvent.getTimestamp());
        try {
            processLoginAnalytics(userEvent);
            acknowledgment.acknowledge();
            logger.info("Analytics processed successfully: {}", userEvent.getUserId());
        } catch (Exception e) {
            logger.error("Error while processing login for analytics: {}", e.getMessage(), e);
            acknowledgment.acknowledge();
        }
    }

    private void processLoginAnalytics(UserEvent userEvent) {
        logger.debug("Updating metrics for user login: {}", userEvent.getUserId());
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
