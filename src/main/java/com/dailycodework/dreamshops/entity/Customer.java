package com.dailycodework.dreamshops.entity;

import com.dailycodework.dreamshops.payload.dto.customer.CustomerInfo;
import jakarta.persistence.*;
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
    private String name;
    private String code;
    private String address;
    private String email;
    private String phone;
    private Integer type;
    @Column(name = "company_id")
    private Long companyId;
}
