package com.dailycodework.dreamshops.rabbitmq.producer;

import com.dailycodework.dreamshops.dto.order.OrderInfo;
import com.dailycodework.dreamshops.rabbitmq.RabbitMQProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQProperties rabbitMQProperties;
    private final String commonExchange;

    public OrderProducer(RabbitMQProperties rabbitMqProperties, RabbitTemplate rabbitTemplate) {
        this.rabbitMQProperties = rabbitMqProperties;
        this.rabbitTemplate = rabbitTemplate;
        this.commonExchange = rabbitMqProperties.getProducer().getDirectExchange();
    }

    public void createOrderQueue(OrderInfo message) {
        String routingKey = rabbitMQProperties.getOrderPublish().getCreateOrderRoutingKey();
        rabbitTemplate.convertAndSend(commonExchange, routingKey, message);
    }
}
