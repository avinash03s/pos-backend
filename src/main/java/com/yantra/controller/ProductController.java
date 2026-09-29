package com.yantra.controller;

import com.yantra.dto.ProductDTO;
import com.yantra.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/createProduct")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO dto) {
        ProductDTO product = productService.createProduct(dto);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @GetMapping("/getAllProducts")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> allProducts = productService.getAllProducts();
        return new ResponseEntity<>(allProducts,HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam String keyword) {
        List<ProductDTO> productDTOS = productService.searchProducts(keyword);
        return new ResponseEntity<>(productDTOS,HttpStatus.OK);
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductDTO>> getLowStockProducts() {
        List<ProductDTO> lowStockProducts = productService.getLowStockProducts();
        return new ResponseEntity<>(lowStockProducts,HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO dto) {
        ProductDTO productDTO = productService.updateProduct(id, dto);
        return new ResponseEntity<>(productDTO,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>("Product Deleted...!",HttpStatus.OK);
    }
}