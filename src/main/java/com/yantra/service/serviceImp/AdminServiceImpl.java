package com.yantra.service.serviceImp;


import com.yantra.dto.response.AdminProfileResponse;
import com.yantra.entity.Tenant;
import com.yantra.entity.User;

import com.yantra.repositroy.TenantRepository;
import com.yantra.repositroy.UserRepository;
import com.yantra.service.AdminService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    private final TenantRepository tenantRepository;

    @Override
    public AdminProfileResponse getAdminProfile(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Tenant tenant = tenantRepository.findById(user.getTenantId())
                .orElseThrow(() -> new RuntimeException("Business not found"));

        return new AdminProfileResponse(tenant.getName(), user.getName(),
                user.getEmail(), user.getPhone(), user.getRole().name());
    }

}