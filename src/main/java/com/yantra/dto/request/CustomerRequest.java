package com.yantra.dto.request;

public record CustomerRequest(
        String name,
        String phone,
        String email
) {}