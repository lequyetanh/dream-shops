package com.dailycodework.dreamshops.dto.customer;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class customerInfo {
    private Long id;
    private String name;
    private String code;
    private String address;
    private String email;
    private String phone;
    private Integer type;
    private Long companyId;
}
