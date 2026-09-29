package com.yantra.dto;

import com.yantra.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    private Long id;

    private Long tenantId;

    private String name;

    private String description;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Category toEntity() {
        return new Category(this.id, this.tenantId, this.name, this.description,
                this.createdAt, this.updatedAt);
    }
}
