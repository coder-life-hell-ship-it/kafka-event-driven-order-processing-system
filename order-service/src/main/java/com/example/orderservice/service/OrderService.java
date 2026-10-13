package com.example.orderservice.service;

import org.springframework.stereotype.Service;

import com.example.orderservice.event.OrderCreatedEvent;
import com.example.orderservice.modal.Order;
import com.example.orderservice.producer.OrderProducer;

@Service 
public class OrderService {
    
    private final OrderProducer orderProducer;

    public OrderService(OrderProducer orderProducer){
        this.orderProducer = orderProducer;
    }

    public Order createOrder(Order order){
        order.setStatus("CREATED");

        OrderCreatedEvent event = new OrderCreatedEvent(
            order.getOrderId(),
            order.getCustomerId(),
            order.getProductId(),
            order.getQuantity()
        );

        orderProducer.sendOrderCreatedEvent(event);
        return order;
    }
}
