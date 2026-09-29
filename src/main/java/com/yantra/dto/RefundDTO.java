package com.yantra.dto;

import com.yantra.constant.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RefundDTO(
        Long id, Long tenantId, Long branchId, Long orderId, Long cashierId,
        String reason, BigDecimal amount, PaymentType paymentType, LocalDateTime createdAt
) {}