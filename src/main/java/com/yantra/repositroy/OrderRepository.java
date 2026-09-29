package com.yantra.repositroy;

import com.yantra.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByTenantId(Long tenantId);
    Optional<Order> findByIdAndTenantId(Long id, Long tenantId);
    List<Order> findAllByTenantIdAndCashier_Id(Long tenantId, Long cashierId);
}