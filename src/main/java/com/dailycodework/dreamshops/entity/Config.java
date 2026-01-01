package com.dailycodework.dreamshops.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "config")
public class Config {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "company_id")
    private Long companyId;
    private String code;
    private String value;
    private String description;
}