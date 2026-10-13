package com.example.inventoryservice.service;

import com.example.inventoryservice.event.InventoryProcessedEvent;
import com.example.inventoryservice.event.OrderCreatedEvent;
import com.example.inventoryservice.producer.InventoryProducer;

import org.springframework.stereotype.Service;

@Service
public class InventoryService {
    
    private static final int AVAILABLE_STOCK = 10;

    private final InventoryProducer inventoryProducer;

    public InventoryService(com.example.inventoryservice.producer.InventoryProducer inventoryProducer){
        this.inventoryProducer = inventoryProducer;
    }

    public String processOrder(OrderCreatedEvent event){
        System.out.println("Processing inventory for order: "+ event.getOrderId());;

        String inventoryStatus;

        if(event.getQuantity() <= AVAILABLE_STOCK){
            System.out.println("Inventory available for products: "+event.getProductId());;
            inventoryStatus = "IN_STOCK";
        }else{
            System.out.println("Insufficient inventory for product: "+ event.getProductId());;
            inventoryStatus =  "OUT_OF_STOCK";
        }

        String eventId = "INV-" + event.getOrderId();

        InventoryProcessedEvent inventoryEvent = new InventoryProcessedEvent(
            event.getOrderId(),
            event.getProductId(),
            inventoryStatus,
            eventId
        );

        inventoryProducer.sendInventoryProcessedEvent(inventoryEvent);

        return inventoryStatus;

    }
}
