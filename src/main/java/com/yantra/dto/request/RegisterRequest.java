package com.yantra.dto.request;


public record RegisterRequest(
        String businessName,
        String adminName,
        String email,
        String phone,
        String password
) {}