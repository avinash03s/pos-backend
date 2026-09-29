package com.yantra.dto;

import java.time.LocalDateTime;

public record CustomerDTO(
        Long id, Long tenantId, String name, String phone, String email, LocalDateTime createdAt
) {}