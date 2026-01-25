package com.dailycodework.dreamshops.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocket {
    // tương đương @PostMapping("/hello")
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public void greeting(String message) {
        // No implementation needed for this example
    }
}
