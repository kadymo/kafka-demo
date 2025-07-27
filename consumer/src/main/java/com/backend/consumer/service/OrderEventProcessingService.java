package com.backend.consumer.service;

import com.backend.consumer.model.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OrderEventProcessingService {
    private static final Logger logger = LoggerFactory.getLogger(OrderEventProcessingService.class);

    public void processOrderCreatedEvent(OrderEvent orderEvent) {
        logger.info("Processing creation of the order: {} for the user: {}", orderEvent.getOrderId(), orderEvent.getUserId());
        try {
            validateOrder(orderEvent);
            reserveInventory(orderEvent);
            calculateShipping(orderEvent);
            sendOrderConfirmation(orderEvent);
            logger.info("Order processed successfully: {}", orderEvent.getOrderId());
        } catch (Exception e) {
            logger.error("Error processing order {}: {}", orderEvent.getOrderId(), e.getMessage());
            throw e;
        }
    }

    public void processOrderUpdatedEvent(OrderEvent orderEvent) {
        logger.info("Processing update of the order: {} - Status: {}", orderEvent.getOrderId(), orderEvent.getStatus());
        try {
            updateOrderStatus(orderEvent);
            notifyStatusChange(orderEvent);
            processStatusSpecificActions(orderEvent);
            logger.info("Order processed successfully: {}", orderEvent.getOrderId());
        } catch (Exception e) {
            logger.error("Error processing order {}: {}", orderEvent.getOrderId(), e.getMessage());
            throw e;
        }
    }

    public void processOrderCancelledEvent(OrderEvent orderEvent) {
        logger.info("Processing cancellation of the order: {}", orderEvent.getOrderId());
        try {
            releaseInventory(orderEvent);
            processRefund(orderEvent);
            sendCancellationNotification(orderEvent);
            updateCancellationMetrics(orderEvent);
            logger.info("Order cancelling processed successfully: {}", orderEvent.getOrderId());
        } catch (Exception e) {
            logger.error("Error cancelling the order {}: {}", orderEvent.getOrderId(), e.getMessage());
        }
    }

    public void processOrderCompletedEvent(OrderEvent orderEvent) {
        logger.info("Processing complete of the order: {}", orderEvent.getOrderId());
        try {
            finalizeTransaction(orderEvent);
            generateInvoice(orderEvent);
            requestReview(orderEvent);
            updateSalesMetrics(orderEvent);
            logger.info("Order completed processed successfully: {}", orderEvent.getOrderId());
        } catch (Exception e) {
            logger.error("Error completing the order {}: {}", orderEvent.getOrderId(), e.getMessage());
            throw e;
        }
    }

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

    private void releaseInventory(OrderEvent orderEvent) {
        logger.debug("Releasing inventory for the order: {}", orderEvent.getOrderId());
        simulateProcessing(200);
    }

    private void processRefund(OrderEvent orderEvent) {
        logger.debug("Processing refund for the order: {}", orderEvent.getOrderId());
        simulateProcessing(400);
    }

    private void sendCancellationNotification(OrderEvent orderEvent) {
        logger.debug("Sending a cancellation notification for the order: {}", orderEvent.getOrderId());
        simulateProcessing(200);
    }

    private void updateCancellationMetrics(OrderEvent orderEvent) {
        logger.debug("Updating cancellation metrics for the order: {}", orderEvent.getOrderId());
        simulateProcessing(100);
    }

    private void finalizeTransaction(OrderEvent orderEvent) {
        logger.debug("Finalizing transaction for the order: {}", orderEvent.getOrderId());
        simulateProcessing(250);
    }

    private void generateInvoice(OrderEvent orderEvent) {
        logger.debug("Generating invoice for the order: {}", orderEvent.getOrderId());
        simulateProcessing(300);
    }

    private void requestReview(OrderEvent orderEvent) {
        logger.debug("Requesting review for the order: {}", orderEvent.getOrderId());
        simulateProcessing(150);
    }

    private void updateSalesMetrics(OrderEvent orderEvent) {
        logger.debug("Updating sales metrics for the order: {}", orderEvent.getOrderId());
        simulateProcessing(100);
    }

    private void simulateProcessing(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
