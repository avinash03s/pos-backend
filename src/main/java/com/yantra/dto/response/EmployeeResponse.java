package com.yantra.dto.response;

import com.yantra.constant.Roles;

import java.time.LocalDateTime;

public record EmployeeResponse(
        Long id,
        String name,
        String email,
        String phone,
        Roles role,
        Long tenantId,
        Long branchId,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime lastLoginAt
) {}