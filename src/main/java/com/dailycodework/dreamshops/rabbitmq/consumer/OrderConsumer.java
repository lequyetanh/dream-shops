package com.dailycodework.dreamshops.rabbitmq.consumer;

import com.dailycodework.dreamshops.service.order.OrderService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderConsumer {

    private final OrderService orderService;

    @RabbitListener(queues = "create-order-queue")
    public void handle(Object payload, Integer orderId, Channel channel, Message amqpMessage) {
        System.out.println("Handle message: " + payload);
    }
}
