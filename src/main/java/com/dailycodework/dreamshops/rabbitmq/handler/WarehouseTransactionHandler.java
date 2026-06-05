package com.dailycodework.dreamshops.rabbitmq.handler;

import com.dailycodework.dreamshops.entity.Order;
import com.dailycodework.dreamshops.service.warehouseTransaction.WarehouseTransactionService;

import java.util.List;

public class WarehouseTransactionHandler extends OrderHandler {

    private final WarehouseTransactionService warehouseTransactionService;

    public WarehouseTransactionHandler(WarehouseTransactionService warehouseTransactionService) {
        this.warehouseTransactionService = warehouseTransactionService;
    }

    @Override
    public void handle(List<Order> orders) {
        warehouseTransactionService.createWarehouseTransactionFromListOrder(orders);
        handleNext(orders);
    }
}
