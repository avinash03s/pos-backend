package com.yantra.entity;

import com.yantra.dto.CategoryDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "category",
        indexes = {
                @Index(name = "idx_category_tenant", columnList = "tenantId")
        }
)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tenantId;

    private String name;

    private String description;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CategoryDTO toDTO() {
        return new CategoryDTO(this.id, this.tenantId, this.name, this.description,
                this.createdAt, this.updatedAt);
    }
}