package com.yantra.dto;

import com.yantra.constant.OrderStatus;
import com.yantra.constant.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDTO(Long id, Long customerId, String customerName, String cashierName, List<OrderItemDTO> items,
        BigDecimal totalAmount, PaymentType paymentType, OrderStatus status, LocalDateTime createdAt
) {}