package com.example.inventoryservice.consumer;

import com.example.inventoryservice.event.OrderCreatedEvent;
import com.example.inventoryservice.service.InventoryService;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {

    private final InventoryService inventoryservice;

    public OrderConsumer(InventoryService inventoryservice){
        this.inventoryservice = inventoryservice;
    }

    @KafkaListener(
        topics="order-events",
        groupId="inventory-group"
    )

    public void consumerOrderCreatedEvent(OrderCreatedEvent event){

        System.out.println("Received Order Created Event:");
        System.out.println("Order ID: " + event.getOrderId());
        System.out.println("Customer ID: " + event.getCustomerId());
        System.out.println("Product ID: " + event.getProductId());
        System.out.println("Quantity: " + event.getQuantity());

        String inventoryStatus = inventoryservice.processOrder(event);

        System.out.println("Inventory Status: " + inventoryStatus);
    }
    
}
