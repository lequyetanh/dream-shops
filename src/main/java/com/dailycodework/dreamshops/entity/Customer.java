package com.dailycodework.dreamshops.entity;

import com.dailycodework.dreamshops.payload.dto.customer.CustomerInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
@Table(name = "customer")
@SqlResultSetMappings(
    {
        @SqlResultSetMapping(
            name = "CustomerResponse",
            classes = {
                @ConstructorResult(
                    targetClass = CustomerInfo.class,
                    columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "code", type = String.class),
                        @ColumnResult(name = "address", type = String.class),
                        @ColumnResult(name = "email", type = String.class),
                        @ColumnResult(name = "phone", type = String.class),
                        @ColumnResult(name = "type", type = Integer.class),
                        @ColumnResult(name = "companyId", type = Long.class)
                    }
                )
            }
        )
    }
)
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String name;

    @Size(max = 100)
    @Column(length = 100)
    private String code;

    @Size(max = 500)
    @Column(length = 500)
    private String address;

    @Email
    @Size(max = 255)
    @Column(length = 255)
    private String email;

    @Size(max = 50)
    @Column(length = 50)
    private String phone;

    @Min(0)
    private Integer type;

    @NotNull
    @Column(name = "company_id", nullable = false)
    private Long companyId;
}
