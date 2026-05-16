package com.dailycodework.dreamshops.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * WebSocket endpoints (STOMP over SockJS)
 *
 * === CONNECT ===
 *   const socket = new SockJS('http://localhost:8080/ws');
 *   const stompClient = Stomp.over(socket);
 *   stompClient.connect({}, onConnected);
 *
 * === SUBSCRIBE — nhận events đơn hàng theo công ty ===
 *   stompClient.subscribe('/topic/orders/{companyId}', msg => {
 *       const event = JSON.parse(msg.body);
 *       // event.eventType : ORDER_CREATED | ORDER_UPDATED | ORDER_DELETED | ORDER_STATUS_CHANGED
 *       // event.orderId   : Long
 *       // event.companyId : Long
 *       // event.data      : Order object (null nếu DELETED)
 *       // event.occurredAt: timestamp
 *   });
 *
 * === TRIGGERS (server tự bắn, client không cần gửi) ===
 *   POST /api/order/create          → ORDER_CREATED
 *   POST /api/order/update          → ORDER_UPDATED
 *   POST /api/order/update-status/{id}?status= → ORDER_STATUS_CHANGED
 *   DELETE /api/order/delete/{id}   → ORDER_DELETED
 */
@Controller
public class WebSocket {
    // tương đương @PostMapping("/hello")
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public void greeting(String message) {
    }
}
