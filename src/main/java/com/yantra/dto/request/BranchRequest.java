package com.yantra.dto.request;

public record BranchRequest(
        String name,
        String address,
        String phone,
        String email,
        String branchCode,
        String city,
        String state,
        String pincode,
        String gstNumber
) {}