package com.backend.producer.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class OrderEvent {
    @JsonProperty("event_id")
    private String eventId;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("event_type")
    private String eventType;

    @Positive
    private BigDecimal total;

    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public OrderEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
    }

    public OrderEvent(String userId, String orderId, String eventType, BigDecimal total, String status) {
        this();
        this.userId = userId;
        this.orderId = orderId;
        this.eventType = eventType;
        this.total = total;
        this.status = status;
    }
}
