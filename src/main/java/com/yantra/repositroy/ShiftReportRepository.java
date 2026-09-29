package com.yantra.repositroy;

import com.yantra.entity.ShiftReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShiftReportRepository extends JpaRepository<ShiftReport, Long> {
    Optional<ShiftReport> findTopByCashierIdAndShiftEndIsNullOrderByShiftStartDesc(Long cashierId);
    List<ShiftReport> findAllByTenantIdAndCashierId(Long tenantId, Long cashierId);
    List<ShiftReport> findAllByTenantId(Long tenantId);
}