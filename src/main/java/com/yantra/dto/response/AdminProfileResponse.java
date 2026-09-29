package com.yantra.dto.response;

public record AdminProfileResponse(
         String businessName,
         String ownerName,
         String email,
         String phone,
         String role
) {
}
