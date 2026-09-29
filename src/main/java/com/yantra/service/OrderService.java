package com.yantra.service;


import com.yantra.dto.OrderDTO;
import com.yantra.dto.request.CreateOrderRequest;

import java.util.List;

public interface OrderService {
    OrderDTO createOrder(CreateOrderRequest request);
    OrderDTO getOrderById(Long id);
    List<OrderDTO> getAllOrders();
}