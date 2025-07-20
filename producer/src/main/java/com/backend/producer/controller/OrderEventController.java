package com.backend.producer.controller;

import com.backend.producer.model.OrderEvent;
import com.backend.producer.service.EventProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderEventController {
    private final Logger logger = LoggerFactory.getLogger(OrderEventController.class);

    private final EventProducerService eventProducerService;

    public OrderEventController(EventProducerService eventProducerService) {
        this.eventProducerService = eventProducerService;
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createOrder(@RequestBody Map<String, Object> orderData) {
        try {
            String orderId = "ORDER-" + System.currentTimeMillis();
            String userId = orderData.get("userId").toString();
            BigDecimal total = (BigDecimal) orderData.get("amount");

            OrderEvent event = new OrderEvent(orderId, userId, "CREATED", total, "PENDING");
            eventProducerService.sendOrderEvent(event);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Order created successfully",
                    "orderId", orderId,
                    "eventId", event.getEventId()
            ));
        } catch (Exception e) {
            logger.error("Errror creating order: {}", e.getMessage(), e);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Error creating order."
            ));
        }
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Map<String, String>> updateOrderStatus(@PathVariable String orderId, @RequestBody Map<String, String> statusData) {
        try {
            String userId = statusData.get("userId");
            String status = statusData.get("status");
            BigDecimal total = new BigDecimal(statusData.getOrDefault("total", "0"));

            OrderEvent event = new OrderEvent(orderId, userId, "UPDATED", total, status);
            eventProducerService.sendOrderEvent(event);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Order status updated successfully",
                    "orderId", orderId,
                    "newStatus", status,
                    "eventId", event.getEventId()
            ));
        } catch (Exception e) {
            logger.error("Errror updating order: {}", e.getMessage(), e);
            return ResponseEntity.ok(Map.of(
                    "status", "error",
                    "message", "Error updating order."
            ));
        }
    }
}
