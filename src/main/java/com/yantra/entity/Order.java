package com.yantra.entity;

import com.yantra.constant.OrderStatus;
import com.yantra.constant.PaymentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders", indexes = {
        @Index(name = "idx_order_tenant", columnList = "tenantId")
})
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tenantId;
    private Long branchId;   // kis branch mein ye order hua — cashier ke branchId se auto-fill hoga

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;   // nullable -> walk-in customer allowed

    @ManyToOne
    @JoinColumn(name = "cashier_id")
    private User cashier;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;

    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime createdAt;
}