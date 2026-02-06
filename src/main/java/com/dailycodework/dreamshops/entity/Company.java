package com.dailycodework.dreamshops.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String website;
    @Column(name = "tax_code")
    private String taxCode;
    private String password;
    private String extra;
}
