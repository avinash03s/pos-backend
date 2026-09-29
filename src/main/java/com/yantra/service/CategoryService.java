package com.yantra.service;

import com.yantra.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    CategoryDTO createCategory(CategoryDTO dto);
    List<CategoryDTO> getAllCategories();
    CategoryDTO updateCategory(Long id, CategoryDTO dto);
    void deleteCategory(Long id);
}
