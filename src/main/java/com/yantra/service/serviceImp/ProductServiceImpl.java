package com.yantra.service.serviceImp;

import com.yantra.dto.ProductDTO;
import com.yantra.entity.Category;
import com.yantra.entity.Product;
import com.yantra.entity.User;
import com.yantra.exception.PosException;
import com.yantra.repositroy.CategoryRepository;
import com.yantra.repositroy.ProductRepository;
import com.yantra.service.ProductService;
import com.yantra.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserService userService;

    @Override
    public ProductDTO createProduct(ProductDTO dto) {
        User currentUser = userService.getCurrentUser();

        if (dto.name() == null || dto.name().isBlank()) {
            throw new PosException("PRODUCT_NAME_REQUIRED");
        }
        if (dto.sku() == null || dto.sku().isBlank()) {
            throw new PosException("PRODUCT_SKU_REQUIRED");
        }
        if (productRepository.existsByTenantIdAndSkuIgnoreCase(currentUser.getTenantId(), dto.sku())) {
            throw new PosException("PRODUCT_SKU_DUPLICATE");
        }

        Category category = null;
        if (dto.categoryId() != null) {
            category = categoryRepository.findByIdAndTenantId(dto.categoryId(), currentUser.getTenantId())
                    .orElseThrow(() -> new PosException("CATEGORY_NOT_FOUND"));
        }

        Product product = new Product();
        product.setTenantId(currentUser.getTenantId());
        product.setName(dto.name());
        product.setSku(dto.sku());
        product.setBarcode(dto.barcode());
        product.setDescription(dto.description());
        product.setCategory(category);
        product.setMrp(dto.mrp());
        product.setSellingPrice(dto.sellingPrice());
        product.setStockQuantity(dto.stockQuantity() != null ? dto.stockQuantity() : 0);
        product.setLowStockAlert(dto.lowStockAlert() != null ? dto.lowStockAlert() : 10);
        product.setActive(true);
        product.setCreatedAt(LocalDateTime.now());

        return productRepository.save(product).toDTO();
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO dto) {
        User currentUser = userService.getCurrentUser();

        Product product = productRepository.findByIdAndTenantId(id, currentUser.getTenantId())
                .orElseThrow(() -> new PosException("PRODUCT_NOT_FOUND"));

        if (dto.categoryId() != null) {
            Category category = categoryRepository.findByIdAndTenantId(dto.categoryId(), currentUser.getTenantId())
                    .orElseThrow(() -> new PosException("PRODUCT_NOT_FOUND"));
            product.setCategory(category);
        }

        product.setName(dto.name());
        product.setBarcode(dto.barcode());
        product.setDescription(dto.description());
        product.setMrp(dto.mrp());
        product.setSellingPrice(dto.sellingPrice());
        product.setStockQuantity(dto.stockQuantity());
        product.setLowStockAlert(dto.lowStockAlert());
        product.setUpdatedAt(LocalDateTime.now());

        return productRepository.save(product).toDTO();
    }

    @Override
    public void deleteProduct(Long id) {
        User currentUser = userService.getCurrentUser();

        Product product = productRepository.findByIdAndTenantId(id, currentUser.getTenantId())
                .orElseThrow(() -> new PosException("PRODUCT_NOT_FOUND"));

        // Soft delete (past orders reference this product)
        product.setActive(false);
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        User currentUser = userService.getCurrentUser();
        return productRepository.findAllByTenantId(currentUser.getTenantId())
                .stream().map(Product::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> searchProducts(String keyword) {
        User currentUser = userService.getCurrentUser();
        return productRepository.searchByKeyword(currentUser.getTenantId(), keyword)
                .stream().map(Product::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getLowStockProducts() {
        User currentUser = userService.getCurrentUser();
        return productRepository.findAllByTenantId(currentUser.getTenantId())
                .stream()
                .filter(p -> p.getStockQuantity() != null && p.getLowStockAlert() != null
                        && p.getStockQuantity() <= p.getLowStockAlert())
                .map(Product::toDTO)
                .collect(Collectors.toList());
    }
}