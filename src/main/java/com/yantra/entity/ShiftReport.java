package com.yantra.entity;

import com.yantra.dto.ShiftReportDTO;
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
@Table(name = "shift_report", indexes = {
        @Index(name = "idx_shift_tenant", columnList = "tenantId"),
        @Index(name = "idx_shift_cashier", columnList = "cashierId")
})
public class ShiftReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tenantId;

    private Long branchId;

    private Long cashierId;

    private LocalDateTime shiftStart;

    private LocalDateTime shiftEnd;

    private BigDecimal totalSale = BigDecimal.ZERO;

    private BigDecimal totalRefunds = BigDecimal.ZERO;

    private BigDecimal netSale = BigDecimal.ZERO;

    private Integer totalOrders = 0;

    public ShiftReportDTO toDTO() {
        return new ShiftReportDTO(this.id, this.tenantId, this.branchId, this.cashierId,
                this.shiftStart, this.shiftEnd, this.totalSale, this.totalRefunds,
                this.netSale, this.totalOrders);
    }
}