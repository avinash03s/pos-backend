package com.yantra.service;

import com.yantra.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    ProductDTO createProduct(ProductDTO dto);
    ProductDTO updateProduct(Long id, ProductDTO dto);
    void deleteProduct(Long id);
    List<ProductDTO> getAllProducts();
    List<ProductDTO> searchProducts(String keyword);
    List<ProductDTO> getLowStockProducts();
}