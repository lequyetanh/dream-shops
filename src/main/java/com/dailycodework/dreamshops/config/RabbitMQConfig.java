package com.dailycodework.dreamshops.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class RabbitMQConfig {


    @Bean
    public Queue ngpInvoiceImportInvoiceQueue() {
        return new Queue(rabbitMqProperties.getNgoGiaPhatInvoice().getNgpImportInvoiceQueue());
    }
}
