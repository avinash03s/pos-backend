package com.yantra.service.serviceImp;

import com.yantra.constant.OrderStatus;
import com.yantra.dto.OrderDTO;
import com.yantra.dto.OrderItemDTO;
import com.yantra.dto.request.CreateOrderRequest;
import com.yantra.entity.*;
import com.yantra.exception.PosException;
import com.yantra.repositroy.CustomerRepository;
import com.yantra.repositroy.OrderRepository;
import com.yantra.repositroy.ProductRepository;
import com.yantra.service.OrderService;
import com.yantra.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final UserService userService;

    @Override
    @Transactional
    //if any problem occurs (e.g. if 3 product order but 1 product are not available) then all process rollback.
    public OrderDTO createOrder(CreateOrderRequest request) {
        User cashier = userService.getCurrentUser();
        Long tenantId = cashier.getTenantId();

        if (request.items() == null || request.items().isEmpty()) {
            throw new PosException("ORDER_ITEMS_REQUIRED");
        }

        Customer customer = null;
        if (request.customerId() != null) {
            customer = customerRepository.findByIdAndTenantId(request.customerId(), tenantId)
                    .orElseThrow(() -> new PosException("CUSTOMER_NOT_FOUND"));
        }

        Order order = new Order();
        order.setTenantId(tenantId);
        order.setCustomer(customer);
        order.setCashier(cashier);
        order.setBranchId(cashier.getBranchId());
        order.setPaymentType(request.paymentType());
        order.setStatus(OrderStatus.COMPLETED);
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CreateOrderRequest.OrderItemRequest itemReq : request.items()) {
            Product product = productRepository.findByIdAndTenantId(itemReq.productId(), tenantId)
                    .orElseThrow(() -> new PosException("PRODUCT_NOT_FOUND"));

            if (!product.isActive()) {
                throw new PosException("PRODUCT_INACTIVE");
            }

            if (product.getStockQuantity() < itemReq.quantity()) {
                // Low on stock -> fail the entire order, partial billing is not allowed
                throw new PosException("PRODUCT_INSUFFICIENT_STOCK");
            }

            // Stock deduct karo
            product.setStockQuantity(product.getStockQuantity() - itemReq.quantity());
            productRepository.save(product);

            BigDecimal lineTotal = product.getSellingPrice().multiply(BigDecimal.valueOf(itemReq.quantity()));

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setOrder(order);
            orderItem.setQuantity(itemReq.quantity());
            orderItem.setUnitPrice(product.getSellingPrice());   // snapshot at sale time
            orderItem.setLineTotal(lineTotal);

            orderItems.add(orderItem);
            total = total.add(lineTotal);
        }

        order.setItems(orderItems);
        order.setTotalAmount(total);

        Order saved = orderRepository.save(order);
        return toDTO(saved);
    }

    @Override
    public OrderDTO getOrderById(Long id) {
        User currentUser = userService.getCurrentUser();
        Order order = orderRepository.findByIdAndTenantId(id, currentUser.getTenantId())
                .orElseThrow(() -> new PosException("ORDER_NOT_FOUND"));
        return toDTO(order);
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        User currentUser = userService.getCurrentUser();
        return orderRepository.findAllByTenantId(currentUser.getTenantId())
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    private OrderDTO toDTO(Order order) {
        List<OrderItemDTO> itemDTOs = order.getItems().stream()
                .map(i -> new OrderItemDTO(
                        i.getProduct().getId(),
                        i.getProduct().getName(),
                        i.getQuantity(),
                        i.getUnitPrice(),
                        i.getLineTotal()))
                .collect(Collectors.toList());

        return new OrderDTO(
                order.getId(),
                order.getCustomer() != null ? order.getCustomer().getId() : null,
                order.getCustomer() != null ? order.getCustomer().getName() : "Walk-in Customer",
                order.getCashier().getName(),
                itemDTOs,
                order.getTotalAmount(),
                order.getPaymentType(),
                order.getStatus(),
                order.getCreatedAt()
        );
    }
}
//@Transactional — If two of the three items are deducted and the third item is out of stock, the entire order (even the first two) will be automatically rolled back. Without this, partial/broken orders may be created.
//The unitPrice snapshot is taken from the product, not a live reference — the invoice history will always be accurate.
//CreateOrderRequest is a separate DTO from the OrderDTO — the cashier only sends the productId + quantity, it does not calculate the price/total itself (to avoid client-side price manipulation)