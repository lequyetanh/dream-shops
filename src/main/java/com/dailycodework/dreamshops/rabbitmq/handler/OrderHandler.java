package com.dailycodework.dreamshops.rabbitmq.handler;

import com.dailycodework.dreamshops.entity.Order;

import java.util.List;

public abstract class OrderHandler {

    private OrderHandler next;

    public OrderHandler setNext(OrderHandler next) {
        this.next = next;
        return next;
    }

    public abstract void handle(List<Order> orders);

    protected void handleNext(List<Order> orders) {
        if (next != null) next.handle(orders);
    }
}
