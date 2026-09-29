package com.yantra.service.serviceImp;

import com.yantra.constant.OrderStatus;
import com.yantra.dto.RefundDTO;
import com.yantra.dto.request.RefundRequest;
import com.yantra.entity.Order;
import com.yantra.entity.Refund;
import com.yantra.entity.User;
import com.yantra.exception.PosException;
import com.yantra.repositroy.OrderRepository;
import com.yantra.repositroy.RefundRepository;
import com.yantra.service.RefundService;
import com.yantra.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RefundServiceImpl implements RefundService {

    private final RefundRepository refundRepository;
    private final OrderRepository orderRepository;
    private final UserService userService;

    @Override
    @Transactional
    public RefundDTO createRefund(RefundRequest request) {
        User currentUser = userService.getCurrentUser();

        Order order = orderRepository.findByIdAndTenantId(request.orderId(), currentUser.getTenantId())
                .orElseThrow(() -> new PosException("ORDER_NOT_FOUND"));

        if (order.getStatus() == OrderStatus.REFUNDED) {
            throw new PosException("ORDER_ALREADY_REFUNDED");
        }

        if (request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new PosException("REFUND_AMOUNT_INVALID");
        }

        if (request.amount().compareTo(order.getTotalAmount()) > 0) {
            throw new PosException("REFUND_AMOUNT_EXCEED_ORDER");
        }

        Refund refund = new Refund();
        refund.setTenantId(currentUser.getTenantId());
        refund.setBranchId(order.getBranchId());
        refund.setOrder(order);
        refund.setCashierId(currentUser.getId());
        refund.setReason(request.reason());
        refund.setAmount(request.amount());
        refund.setPaymentType(order.getPaymentType());
        refund.setCreatedAt(LocalDateTime.now());

        Refund saved = refundRepository.save(refund);

        //if full amount are refund then order status are update
        if (request.amount().compareTo(order.getTotalAmount()) == 0) {
            order.setStatus(OrderStatus.REFUNDED);
            orderRepository.save(order);
        }

        return saved.toDTO();
    }

    @Override
    public List<RefundDTO> getAllRefunds() {
        User currentUser = userService.getCurrentUser();
        return refundRepository.findAllByTenantId(currentUser.getTenantId())
                .stream().map(Refund::toDTO).collect(Collectors.toList());
    }
}