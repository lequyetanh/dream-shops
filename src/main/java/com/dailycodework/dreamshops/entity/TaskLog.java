package com.dailycodework.dreamshops.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "task_log")
public class TaskLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String type;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String content;
}
