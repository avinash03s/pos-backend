package com.yantra.dto;

import java.time.LocalDateTime;

public record BranchDTO(
        Long id,
        Long tenantId,
        String name,
        String address,
        String phone,
        String email,
        String branchCode,
        String city,
        String state,
        String pincode,
        String gstNumber,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}