package com.yantra.dto.request;

import com.yantra.constant.PaymentType;

import java.util.List;

//Separate input DTO for checkout - only accept what the cashier sends, not the full order object)
public record CreateOrderRequest(Long customerId,          // optional, null = walk-in
        List<OrderItemRequest> items, PaymentType paymentType) {
    public record OrderItemRequest(Long productId, Integer quantity) {}
}