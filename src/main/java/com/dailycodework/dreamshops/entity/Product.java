package com.dailycodework.dreamshops.entity;

import com.dailycodework.dreamshops.dto.product.ProductResponse;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "product")
@SqlResultSetMappings(
    {
        @SqlResultSetMapping(
            name = "ProductResponse",
            classes = {
                @ConstructorResult(
                    targetClass = ProductResponse.class,
                    columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "description", type = String.class),
                        @ColumnResult(name = "barcode", type = String.class),
                        @ColumnResult(name = "image", type = String.class),
                        @ColumnResult(name = "inPrice", type = BigDecimal.class),
                        @ColumnResult(name = "outPrice", type = BigDecimal.class),
                        @ColumnResult(name = "companyId", type = Long.class)
                    }
                )
            }
        )
    }
)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String barcode;
    private String image;
    @Column(name = "in_price")
    private BigDecimal inPrice;
    @Column(name = "out_Price")
    private BigDecimal outPrice;
    @Column(name = "company_id")
    private Long companyId;
}
