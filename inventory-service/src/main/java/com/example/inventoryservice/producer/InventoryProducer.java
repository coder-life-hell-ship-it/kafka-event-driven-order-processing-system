package com.example.inventoryservice.producer;

import com.example.inventoryservice.event.InventoryProcessedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class InventoryProducer {
    
    private static final String TOPIC = "inventory-events";

    private final KafkaTemplate<String, InventoryProcessedEvent> kafkaTemplate;

    public InventoryProducer(
        KafkaTemplate<String, InventoryProcessedEvent> kafkaTemplate){
            this.kafkaTemplate = kafkaTemplate;
    }

    public void sendInventoryProcessedEvent(InventoryProcessedEvent event){

        kafkaTemplate.send(
            TOPIC,
            event.getOrderId(),
            event
        );
    }
}
