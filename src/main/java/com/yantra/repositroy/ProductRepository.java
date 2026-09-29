package com.yantra.repositroy;

import com.yantra.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByTenantId(Long tenantId);

    Optional<Product> findByIdAndTenantId(Long id, Long tenantId);

    boolean existsByTenantIdAndSkuIgnoreCase(Long tenantId, String sku);

    @Query("SELECT p FROM Product p WHERE p.tenantId = :tenantId AND (" +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.sku) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.barcode) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Product> searchByKeyword(@Param("tenantId") Long tenantId, @Param("keyword") String keyword);

    List<Product> findAllByTenantIdAndStockQuantityLessThanEqual(Long tenantId, Integer threshold);
}