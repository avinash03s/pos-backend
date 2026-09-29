package com.yantra.service.serviceImp;

import com.yantra.dto.ShiftReportDTO;
import com.yantra.entity.Order;
import com.yantra.entity.Refund;
import com.yantra.entity.ShiftReport;
import com.yantra.entity.User;
import com.yantra.exception.PosException;
import com.yantra.repositroy.OrderRepository;
import com.yantra.repositroy.RefundRepository;
import com.yantra.repositroy.ShiftReportRepository;
import com.yantra.service.ShiftReportService;
import com.yantra.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShiftReportServiceImpl implements ShiftReportService {

    private final ShiftReportRepository shiftReportRepository;
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final RefundRepository refundRepository;

    @Override
    public ShiftReportDTO startShift() {
        User currentUser = userService.getCurrentUser();

        //if already any ship are open (shiftEnd null), no new start
        shiftReportRepository.findTopByCashierIdAndShiftEndIsNullOrderByShiftStartDesc(currentUser.getId())
                .ifPresent(s -> { throw new PosException("SHIFT_ALREADY_ACTIVE"); });

        ShiftReport shift = new ShiftReport();
        shift.setTenantId(currentUser.getTenantId());
        shift.setBranchId(currentUser.getBranchId());
        shift.setCashierId(currentUser.getId());
        shift.setShiftStart(LocalDateTime.now());

        return shiftReportRepository.save(shift).toDTO();
    }

    @Override
    public ShiftReportDTO endShift() {
        User currentUser = userService.getCurrentUser();

        ShiftReport shift = shiftReportRepository
                .findTopByCashierIdAndShiftEndIsNullOrderByShiftStartDesc(currentUser.getId())
                .orElseThrow(() -> new PosException("SHIFT_NOT_ACTIVE"));

        shift.setShiftEnd(LocalDateTime.now());

        //get order and calculate total sale
        List<Order> orders = orderRepository
                .findAllByTenantIdAndCashier_Id(currentUser.getTenantId(), currentUser.getId())
                .stream()
                .filter(o -> !o.getCreatedAt().isBefore(shift.getShiftStart())
                        && !o.getCreatedAt().isAfter(shift.getShiftEnd()))
                .toList();

        BigDecimal totalSale = orders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //get (collect) refunds and calculate total refunds
        List<Refund> refunds = refundRepository.findAllByCashierIdAndCreatedAtBetween(
                currentUser.getId(), shift.getShiftStart(), shift.getShiftEnd()
        );
        BigDecimal totalRefunds = refunds.stream()
                .map(Refund::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        shift.setTotalSale(totalSale);
        shift.setTotalOrders(orders.size());
        shift.setTotalRefunds(totalRefunds);
        shift.setNetSale(totalSale.subtract(totalRefunds));   // local variable use, not entity field

        return shiftReportRepository.save(shift).toDTO();
    }
    @Override
    public List<ShiftReportDTO> getMyShifts() {
        User currentUser = userService.getCurrentUser();
        return shiftReportRepository.findAllByTenantIdAndCashierId(currentUser.getTenantId(), currentUser.getId())
                .stream().map(ShiftReport::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<ShiftReportDTO> getAllShifts() {
        User currentUser = userService.getCurrentUser();
        return shiftReportRepository.findAllByTenantId(currentUser.getTenantId())
                .stream().map(ShiftReport::toDTO).collect(Collectors.toList());
    }
}