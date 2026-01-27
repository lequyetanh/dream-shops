package com.dailycodework.dreamshops.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "company_id")
    private Long companyId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "ProductCategory",
            joinColumns = { @JoinColumn(name = "category_id", referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "product_id", columnDefinition = "id") }
    )
    private List<Product> products = new ArrayList<>();
}
