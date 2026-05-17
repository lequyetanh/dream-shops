package com.dailycodework.dreamshops.entity;

import com.dailycodework.dreamshops.payload.dto.product.ProductResponse;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
                        @ColumnResult(name = "companyId", type = Long.class),
                        @ColumnResult(name = "stockQuantity", type = Integer.class)
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

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String name;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Size(max = 100)
    @Column(length = 100)
    private String barcode;

    @Size(max = 500)
    @Column(length = 500)
    private String image;

    @NotNull
    @PositiveOrZero
    @Column(name = "in_price", nullable = false, precision = 19, scale = 2)
    private BigDecimal inPrice;

    @NotNull
    @PositiveOrZero
    @Column(name = "out_Price", nullable = false, precision = 19, scale = 2)
    private BigDecimal outPrice;

    @NotNull
    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Min(0)
    @Column(name = "stock_quantity")
    private Integer stockQuantity;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "product_category",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories = new ArrayList<>();
}
