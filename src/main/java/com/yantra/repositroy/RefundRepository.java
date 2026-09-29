package com.yantra.repositroy;

import com.yantra.entity.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RefundRepository extends JpaRepository<Refund, Long> {
    List<Refund> findAllByTenantId(Long tenantId);
    List<Refund> findAllByCashierIdAndCreatedAtBetween(Long cashierId, LocalDateTime start, LocalDateTime end);
    List<Refund> findAllByOrder_Id(Long orderId);
}