package com.yantra.entity;

import com.yantra.dto.ProductDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "product",
        indexes = {
                @Index(name = "idx_product_tenant", columnList = "tenantId"),
                @Index(name = "idx_product_sku", columnList = "sku")
        }
)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tenantId;

    private String name;

    private String sku;

    private String barcode;

    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private BigDecimal mrp;

    private BigDecimal sellingPrice;

    private Integer stockQuantity;

    private Integer lowStockAlert;

    private boolean isActive = true;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProductDTO toDTO() {
        return new ProductDTO(
                this.id,
                this.tenantId,
                this.name,
                this.sku,
                this.barcode,
                this.description,
                this.category != null ? this.category.getId() : null,
                this.category != null ? this.category.getName() : null,
                this.mrp,
                this.sellingPrice,
                this.stockQuantity,
                this.lowStockAlert,
                this.isActive,
                this.createdAt,
                this.updatedAt
        );
    }
}