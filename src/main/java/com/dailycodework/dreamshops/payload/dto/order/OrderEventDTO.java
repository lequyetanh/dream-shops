package com.dailycodework.dreamshops.payload.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEventDTO {
    private String eventType;  // ORDER_CREATED | ORDER_UPDATED | ORDER_DELETED | ORDER_STATUS_CHANGED
    private Long orderId;
    private Long companyId;
    private Object data;
    private ZonedDateTime occurredAt;
}
