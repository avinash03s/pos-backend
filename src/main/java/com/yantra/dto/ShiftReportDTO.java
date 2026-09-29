package com.yantra.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ShiftReportDTO(
        Long id, Long tenantId, Long branchId, Long cashierId,
        LocalDateTime shiftStart, LocalDateTime shiftEnd,
        BigDecimal totalSale, BigDecimal totalRefunds, BigDecimal netSale, Integer totalOrders
) {}