package com.yantra.entity;

import com.yantra.constant.Roles;
import com.yantra.dto.UserDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "user_detail",
        uniqueConstraints = @UniqueConstraint(columnNames = {"tenant_id", "email"}),
        indexes = {
                @Index(name = "idx_user_email", columnList = "email"),
                @Index(name = "idx_user_role",  columnList = "role"),
                @Index(name = "idx_user_tenant", columnList = "tenant_id")
        }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Email
    private String email;

    private String phone;

    private String password;

    @Enumerated(EnumType.STRING)
    private Roles role;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "branch_id")
    private Long branchId;      // null = tenant-level user (ADMIN/STORE_MANAGER), set = specific branch ka Cashier/BranchManager

    private boolean active = true;   // employee deactivate karne ke liye

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLoginAt;

    public UserDTO toDTO() {
        return new UserDTO(this.id, this.name, this.email, this.phone, this.password, this.role,
                this.tenantId, this.branchId, this.active,
                this.createdAt, this.updatedAt, this.lastLoginAt);
    }
}