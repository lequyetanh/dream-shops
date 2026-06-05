package com.dailycodework.dreamshops.rabbitmq.handler;

import com.dailycodework.dreamshops.entity.Order;
import com.dailycodework.dreamshops.service.product.IProductService;

import java.util.List;

public class StockUpdateHandler extends OrderHandler {

    private final IProductService productService;

    public StockUpdateHandler(IProductService productService) {
        this.productService = productService;
    }

    @Override
    public void handle(List<Order> orders) {
        productService.updateStockQuantity(orders);
        handleNext(orders);
    }
}
