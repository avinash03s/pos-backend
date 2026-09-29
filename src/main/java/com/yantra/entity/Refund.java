package com.yantra.entity;

import com.yantra.constant.PaymentType;
import com.yantra.dto.RefundDTO;
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
@Table(name = "refund", indexes = {
        @Index(name = "idx_refund_tenant", columnList = "tenantId"),
        @Index(name = "idx_refund_order", columnList = "order_id")
})
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tenantId;

    private Long branchId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private Long cashierId;      // kisne refund process kiya

    private String reason;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    private LocalDateTime createdAt;

    public RefundDTO toDTO() {
        return new RefundDTO(this.id, this.tenantId, this.branchId,
                this.order != null ? this.order.getId() : null,
                this.cashierId, this.reason, this.amount, this.paymentType, this.createdAt);
    }
}