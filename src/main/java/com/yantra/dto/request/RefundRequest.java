package com.yantra.dto.request;

import java.math.BigDecimal;

public record RefundRequest(
        Long orderId,
        String reason,
        BigDecimal amount   // full or partial refund amount
) {}