package com.dailycodework.dreamshops.config;

import com.dailycodework.dreamshops.rabbitmq.RabbitMQProperties;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class RabbitMQConfig {

    private final RabbitMQProperties rabbitMqProperties;

    public RabbitMQConfig(RabbitMQProperties rabbitMqProperties) {
        this.rabbitMqProperties = rabbitMqProperties;
    }

    @Bean
    public Queue createOrderQueue() {
        return new Queue(rabbitMqProperties.getOrderPublish().getCreateOrderQueue());
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(rabbitMqProperties.getProducer().getDirectExchange());
    }

    @Bean
    public Binding ngpInvoiceReplaceInvoiceBinding() {
        return BindingBuilder
                .bind(createOrderQueue())
                .to(directExchange())
                .with(rabbitMqProperties.getOrderPublish().getCreateOrderRoutingKey());
    }
}
