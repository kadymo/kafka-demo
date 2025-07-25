package com.backend.consumer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "processed_events")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProcessedEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String eventId;

    @Column(nullable = false)
    private String partitionId;

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false)
    private String topic;

    @Column(name = "offset_value")
    private Long offsetValue;

    @Column(nullable = false)
    private LocalDateTime processedAt;

    @Column(nullable = false)
    private String processingStatus;

    private String errorMessage;

    public ProcessedEvent(String eventId, String partitionId, String eventType, String topic, Long offsetValue) {
        this.eventId = eventId;
        this.partitionId = partitionId;
        this.eventType = eventType;
        this.topic = topic;
        this.offsetValue = offsetValue;
    }
}
