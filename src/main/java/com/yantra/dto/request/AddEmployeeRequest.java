package com.yantra.dto.request;

import com.yantra.constant.Roles;

public record AddEmployeeRequest(
        String name,
        String email,
        String phone,
        String password,
        Roles role,       // ROLE_CASHIER, ROLE_BRANCH_MANAGER, ROLE_STORE_MANAGER
        Long branchId      // optional — which branch employee
) {}