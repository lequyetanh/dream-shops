package com.dailycodework.dreamshops.service.order;

import com.dailycodework.dreamshops.entity.Order;
import com.dailycodework.dreamshops.payload.dto.order.OrderEventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class OrderWebSocketService {
    private final SimpMessagingTemplate messagingTemplate;

    // Client subscribe: /topic/orders/{companyId}
    private static final String ORDER_TOPIC = "/topic/orders/";

    public void notifyOrderCreated(Order order) {
        send(order.getCompanyId(), "ORDER_CREATED", order.getId(), order);
    }

    public void notifyOrderUpdated(Order order) {
        send(order.getCompanyId(), "ORDER_UPDATED", order.getId(), order);
    }

    public void notifyOrderDeleted(Long orderId, Long companyId) {
        send(companyId, "ORDER_DELETED", orderId, null);
    }

    public void notifyOrderStatusChanged(Order order) {
        send(order.getCompanyId(), "ORDER_STATUS_CHANGED", order.getId(), order);
    }

    private void send(Long companyId, String eventType, Long orderId, Object data) {
        OrderEventDTO event = new OrderEventDTO(eventType, orderId, companyId, data, ZonedDateTime.now());
        messagingTemplate.convertAndSend(ORDER_TOPIC + companyId, event);
    }
}
