package com.yantra.service.serviceImp;

import com.yantra.dto.CategoryDTO;
import com.yantra.entity.Category;
import com.yantra.entity.User;
import com.yantra.exception.PosException;
import com.yantra.repositroy.CategoryRepository;
import com.yantra.service.CategoryService;
import com.yantra.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserService userService;

    @Override
    public CategoryDTO createCategory(CategoryDTO dto) {
        User currentUser = userService.getCurrentUser();

        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new PosException("CATEGORY_NAME_IS_REQUIRED");
        }

        boolean exists = categoryRepository.existsByTenantIdAndNameIgnoreCase(
                currentUser.getTenantId(), dto.getName());
        if (exists) {
            throw new PosException("CATEGORY_WITH_THIS_NAME_ALREADY_EXITSTS");
        }

        Category category = new Category();
        category.setTenantId(currentUser.getTenantId());
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setCreatedAt(LocalDateTime.now());

        Category saved = categoryRepository.save(category);
        return saved.toDTO();
    }

    @Override
    public List<CategoryDTO> getAllCategories(){
        User currentUser = userService.getCurrentUser();
        return categoryRepository.findAllByTenantId(currentUser.getTenantId())
                .stream().map(Category::toDTO).collect(Collectors.toList());
    }

    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO dto) {
        User currentUser = userService.getCurrentUser();
        Category category = categoryRepository.findByIdAndTenantId(id, currentUser.getTenantId())
                .orElseThrow(() -> new PosException("CATEGORY_NOT_FOUND"));

        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setUpdatedAt(LocalDateTime.now());

        return categoryRepository.save(category).toDTO();
    }

    @Override
    public void deleteCategory(Long id) {
        User currentUser = userService.getCurrentUser();

        Category category = categoryRepository.findByIdAndTenantId(id, currentUser.getTenantId())
                .orElseThrow(() -> new PosException("CATEGORY_NOT_FOUND"));

        categoryRepository.delete(category);
    }
}