package com.yantra.service;

import com.yantra.dto.response.AdminProfileResponse;

public interface AdminService {

    AdminProfileResponse getAdminProfile(String email);

}
