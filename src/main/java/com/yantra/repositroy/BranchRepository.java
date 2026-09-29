package com.yantra.repositroy;

import com.yantra.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch, Long> {

    Optional<Branch> findByIdAndTenantId(Long id, Long tenantId);

    List<Branch> findAllByTenantId(Long tenantId);

    boolean existsByBranchCodeAndTenantId(String branchCode, Long tenantId);
}