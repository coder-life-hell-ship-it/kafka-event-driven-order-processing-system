package com.example.orderservice.modal;

public class Order {
    private String orderId;
    private String customerId;
    private String productId;
    private int quantity;
    private String status;

    public Order(){}

    public Order(String orderId, String customerId, String productId, int quantity, String status){
        this.orderId = orderId;
        this.customerId = customerId;
        this.productId = productId;
        this.quantity = quantity;
        this.status = status;
    }

    public String getOrderId(){
        return orderId;
    }

    public void setOrderId(String orderId){
        this.orderId = orderId;
    }


        public String getCustomerId(){
        return customerId;
    }

    public void setCustomerId(String customerId){
        this.customerId = customerId;
    }
        public String getProductId(){
        return productId;
    }

    public void setProductId(String productId){
        this.productId = productId;
    }

    public int getQuantity(){
        return quantity;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

        public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }
}
