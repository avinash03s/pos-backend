package com.yantra.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductDTO(Long id, Long tenantId, String name, String sku, String barcode, String description,
        Long categoryId, String categoryName, BigDecimal mrp, BigDecimal sellingPrice, Integer stockQuantity,
        Integer lowStockAlert, boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt) {}