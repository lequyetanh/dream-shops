package com.dailycodework.dreamshops.rabbitmq.producer;

import com.dailycodework.dreamshops.config.RabbitMQConfig;
import com.dailycodework.dreamshops.rabbitmq.RabbitMQProperties;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class OrderProducer {
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQProperties rabbitMQProperties;
    private final String commonExchange;

//    public void sendMessage(String message) {
//        rabbitTemplate.convertAndSend(
//                RabbitMQConfig.EXCHANGE_NAME,
//                RabbitMQConfig.ROUTING_KEY,
//                message
//        );
//        System.out.println("Sent: " + message);
//    }

    public void checkInvoice(TaskLogIdEnqueueMessage message) {
        log.debug("Begin to produce check invoice message: {}", message);
        String routingKey = rabbitMqProperties.getNgoGiaPhatInvoice().getNgpCheckInvoiceRoutingKey();
        rabbitTemplate.convertAndSend(commonExchange, routingKey, message.getTaskLogId());
    }
}
