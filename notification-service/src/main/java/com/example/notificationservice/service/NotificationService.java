package com.example.notificationservice.service;

import com.example.notificationservice.entity.ProcessedEvent;
import com.example.notificationservice.event.InventoryProcessedEvent;
import com.example.notificationservice.repository.ProcessedEventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationService {

    private final ProcessedEventRepository processedEventRepository;

    public NotificationService(ProcessedEventRepository processedEventRepository){
        this.processedEventRepository = processedEventRepository;
    }

    public boolean processNotification(InventoryProcessedEvent event){
        String eventId = event.getEventId();

        // check whether this event was already processed
        if(processedEventRepository.existsByEventId(eventId)){
            System.out.println("Duplicate event detected. Skipping event: " + eventId);
            return false;
        }

        // Simulate notification processing
        System.out.println("Sending notification:");
        System.out.println("Order ID: " + event.getOrderId());
        System.out.println("Product ID: " + event.getProductId());
        System.out.println("Inventory Status: " + event.getInventoryStatus());

        // save the event only after successful processing
        ProcessedEvent processedEvent = new ProcessedEvent(
                eventId, LocalDateTime.now());

        processedEventRepository.save(processedEvent);

        System.out.println("Event marked as processed: " + eventId);

        return true;
    }
}
