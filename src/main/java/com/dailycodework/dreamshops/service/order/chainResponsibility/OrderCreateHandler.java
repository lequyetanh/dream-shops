package com.dailycodework.dreamshops.service.order.chainResponsibility;

import com.dailycodework.dreamshops.entity.Order;
import com.fasterxml.jackson.core.JsonProcessingException;

public abstract class OrderCreateHandler {
    OrderCreateHandler next;

    public static OrderCreateHandler link(OrderCreateHandler first, OrderCreateHandler... chains) {
        OrderCreateHandler head = first;
        for (OrderCreateHandler nextChain : chains) {
            head.next = nextChain;
            head = nextChain;
        }
        return first;
    }
    public abstract Object createBillHandler(Order orderDTO, Object relateObject)
            throws JsonProcessingException;

    protected Object checkNext(Order orderDTO, Object relateObject)
            throws JsonProcessingException {
        if (next == null) {
            return relateObject;
        }
        return next.createBillHandler(orderDTO, relateObject);
    }
}
