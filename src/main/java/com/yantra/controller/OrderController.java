package com.yantra.controller;


import com.yantra.dto.OrderDTO;
import com.yantra.dto.request.CreateOrderRequest;
import com.yantra.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/createOrder")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody CreateOrderRequest request) {
        OrderDTO order = orderService.createOrder(request);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        OrderDTO orderById = orderService.getOrderById(id);
        return new ResponseEntity<>(orderById,HttpStatus.OK);
    }

    @GetMapping("/getAllOrders")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> allOrders = orderService.getAllOrders();
        return new ResponseEntity<>(allOrders,HttpStatus.OK);
    }
}