package com.example.inventoryservice.event;

public class InventoryProcessedEvent {
    
    private String orderId;
    private String productId;
    private String inventoryStatus;
    private String eventId;

    public InventoryProcessedEvent(){}

    public InventoryProcessedEvent(String orderId, String productId, String inventoryStatus, String eventId){
        this.orderId = orderId;
        this.productId = productId;
        this.inventoryStatus = inventoryStatus;
        this.eventId = eventId;
    }

     public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getInventoryStatus() {
        return inventoryStatus;
    }

    public void setInventoryStatus(String inventoryStatus) {
        this.inventoryStatus = inventoryStatus;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
