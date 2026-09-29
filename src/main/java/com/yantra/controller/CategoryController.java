package com.yantra.controller;

import com.yantra.dto.CategoryDTO;
import com.yantra.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Create New Category")
    @PostMapping("/create")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO dto) {
        CategoryDTO category = categoryService.createCategory(dto);
        return new ResponseEntity<>(category,HttpStatus.CREATED);
    }

    @Operation(summary = "Get All List of Category")
    @GetMapping("/getAll")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> allCategories = categoryService.getAllCategories();
        return new ResponseEntity<>(allCategories,HttpStatus.OK);
    }

    @Operation(summary = "Update Category Data")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO dto) {
        CategoryDTO categoryDTO = categoryService.updateCategory(id, dto);
        return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
    }

    @Operation(summary = "Delete Exiting Category")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>("Category Deleted..!",HttpStatus.OK);
    }
}