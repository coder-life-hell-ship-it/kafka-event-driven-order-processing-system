package com.example.notificationservice.consumer;

import com.example.notificationservice.event.InventoryProcessedEvent;
import com.example.notificationservice.service.NotificationService;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.kafka.support.Acknowledgment;

@Service
public class NotificationConsumer {

    public final NotificationService notificationservice;

    public NotificationConsumer(NotificationService notificationservice){
        this.notificationservice = notificationservice;
    }
    
    @KafkaListener(
        topics = "inventory-events",
        groupId = "notification-group"
    )

    public void consumeInventoryProcessedEvent(InventoryProcessedEvent event, Acknowledgment acknowledgement){

        System.out.println("Received Inventory Processed Event:");
        System.out.println("Order ID: " + event.getOrderId());
        System.out.println("Product ID: " + event.getProductId());
        System.out.println("Inventory Status: " + event.getInventoryStatus());
        System.out.println("Event ID: " + event.getEventId());

        
        // if("ORD-FAIL-002".equals(event.getOrderId())){
        //     System.out.println("!!! Simulating notification processing failure !!!");;
        //     throw new RuntimeException("Simulated notification failure");
        // }
        
        notificationservice.processNotification(event);
        
        acknowledgement.acknowledge();

         System.out.println("Message acknowledged successfully.");
    }


}
