package com.dailycodework.dreamshops.dto.company;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CompanyInfo {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String website;
    private String taxCode;
    private List<BigDecimal> extra;
}
