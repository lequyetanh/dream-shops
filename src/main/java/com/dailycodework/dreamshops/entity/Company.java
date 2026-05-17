package com.dailycodework.dreamshops.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String name;

    @Size(max = 500)
    @Column(length = 500)
    private String address;

    @Size(max = 50)
    @Column(length = 50)
    private String phone;

    @Email
    @Size(max = 255)
    @Column(length = 255)
    private String email;

    @Size(max = 255)
    @Column(length = 255)
    private String website;

    @Size(max = 100)
    @Column(name = "tax_code", unique = true, length = 100)
    private String taxCode;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String extra;
}
