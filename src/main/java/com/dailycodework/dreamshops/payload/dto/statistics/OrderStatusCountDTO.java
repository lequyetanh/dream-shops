package com.dailycodework.dreamshops.payload.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusCountDTO {
    private Integer status;
    private Integer orderCount;
}
