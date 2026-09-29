package com.yantra.dto.response;

import com.yantra.constant.Roles;

public record AuthResponse(
        String token,
        Long userId,
        String name,
        String email,
        Roles role,
        Long tenantId
) {}