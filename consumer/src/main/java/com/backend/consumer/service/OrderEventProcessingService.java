package com.backend.consumer.service;

import com.backend.consumer.model.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OrderEventProcessingService {
    private static final Logger logger = LoggerFactory.getLogger(OrderEventProcessingService.class);

    // Auxiliary methods (simulation)
    private void validateOrder(OrderEvent orderEvent) {
       logger.debug("Validating order: {}", orderEvent.getOrderId());
       simulateProcessing(300);
    }

    private void reserveInventory(OrderEvent orderEvent) {
        logger.debug("Reserving inventory for the order: {}", orderEvent.getOrderId());
        simulateProcessing(300);
    }

    private void calculateShipping(OrderEvent orderEvent) {
        logger.debug("Calculating shipping for the order: {}", orderEvent.getOrderId());
        simulateProcessing(150);
    }

    private void sendOrderConfirmation(OrderEvent orderEvent) {
        logger.debug("Sending of the order: {}", orderEvent.getOrderId());
        simulateProcessing(250);
    }

    private void updateOrderStatus(OrderEvent orderEvent) {
        logger.debug("Updating order status for the order: {}", orderEvent.getOrderId());
        simulateProcessing(100);
    }

    private void notifyStatusChange(OrderEvent orderEvent) {
        logger.debug("Notifying status change for the order: {}", orderEvent.getOrderId());
        simulateProcessing(200);
    }

    private void processStatusSpecificActions(OrderEvent orderEvent) {
        switch (orderEvent.getStatus()) {
            case "PAID":
                logger.debug("Processing confirmed payment for the order: {}", orderEvent.getOrderId());
            case "SHIPPED":
                logger.debug("Processing shipping fot the order: {}", orderEvent.getOrderId());
            case "DELIVERED":
                logger.debug("Processing delivered the order: {}", orderEvent.getOrderId());
        }
        simulateProcessing(150);
    }

    private void simulateProcessing(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
