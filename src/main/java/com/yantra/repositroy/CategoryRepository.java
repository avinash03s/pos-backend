package com.yantra.repositroy;

import com.yantra.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    List<Category> findAllByTenantId(Long tenantId);
    Optional<Category> findByIdAndTenantId(Long id, Long tenantId);
    boolean existsByTenantIdAndNameIgnoreCase(Long tenantId, String name);
}
