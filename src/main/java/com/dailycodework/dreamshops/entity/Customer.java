package com.dailycodework.dreamshops.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String code;
    private String address;
    private String email;
    private String phone;
    private Integer type;
    @Column(name = "company_id")
    private Long companyId;
}
