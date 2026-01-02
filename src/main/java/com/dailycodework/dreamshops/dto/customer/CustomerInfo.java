package com.dailycodework.dreamshops.dto.customer;

import lombok.Data;

@Data
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
