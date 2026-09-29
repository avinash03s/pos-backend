package com.yantra.dto;

import java.math.BigDecimal;

public record OrderItemDTO(
        Long productId, String productName, Integer quantity,
        BigDecimal unitPrice, BigDecimal lineTotal
) {}