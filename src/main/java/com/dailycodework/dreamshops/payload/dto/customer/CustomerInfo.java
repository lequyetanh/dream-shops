package com.dailycodework.dreamshops.payload.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerInfo {
    private Long id;
    private String name;
    private String code;
    private String address;
    private String email;
    private String phone;
    private Integer type;
    private Long companyId;
}
