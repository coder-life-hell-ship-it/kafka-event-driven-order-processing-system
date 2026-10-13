package com.example.notificationservice.entity;

import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
@Table(
    name = "processed_events",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "event_id")
    }
)
public class ProcessedEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable=false, unique = true)
    private String eventId;

    @Column(name = "processed_at", nullable=false)
    private LocalDateTime processedAt;

    public ProcessedEvent(){}

    public ProcessedEvent(String eventId, LocalDateTime processedAt){
        this.eventId = eventId;
        this.processedAt = processedAt;
    }

     public Long getId() {
        return id;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }
    
}
