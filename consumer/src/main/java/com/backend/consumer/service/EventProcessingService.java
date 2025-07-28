package com.backend.consumer.service;

import com.backend.consumer.entity.ProcessedEvent;
import com.backend.consumer.repository.ProcessedEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class EventProcessingService {
    private final ProcessedEventRepository repository;
    private final Logger logger = LoggerFactory.getLogger(EventProcessingService.class);

    public EventProcessingService(ProcessedEventRepository repository) {
        this.repository = repository;
    }

    public boolean isEventAlreadyProcessed(String eventId) {
        boolean exists = repository.existsById(UUID.fromString(eventId));
        if (exists) {
            logger.warn("Event {} is already processed.",  eventId);
        }
        return exists;
    }

    public void markEventAsProcessed(String eventId, String eventType, String topic, String partitionId, Long offset) {
        try {
            ProcessedEvent event = new ProcessedEvent(eventId, partitionId, eventType, topic, offset);
            event.setProcessingStatus("SUCCESS");
            repository.save(event);
            logger.debug("Event {} marked as processed.",  eventId);
        } catch (Exception e) {
            logger.error("Error while marking event {} as processed: {}",  eventId, e.getMessage(), e);
        }
    }

    public void markEventAsError(String eventId, String eventType, String topic, String partitionId, Long offset, String errorMessage) {
        try {
            ProcessedEvent event = new ProcessedEvent(eventId, partitionId, eventType, topic, offset);
            event.setProcessingStatus("ERROR");
            event.setErrorMessage(errorMessage);
            repository.save(event);
            logger.error("Event {} marked as error: {}",  eventId, errorMessage);
        } catch (Exception e) {
            logger.error("Error while marking event {} as error: {}",  eventId, e.getMessage(), e);
        }
    }
}
