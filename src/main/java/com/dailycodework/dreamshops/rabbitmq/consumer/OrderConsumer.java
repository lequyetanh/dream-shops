package com.dailycodework.dreamshops.rabbitmq.consumer;

import com.dailycodework.dreamshops.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderConsumer {
//    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
//    public void handle(String message) {
//        System.out.println("Handle message: " + message);
//    }
}
