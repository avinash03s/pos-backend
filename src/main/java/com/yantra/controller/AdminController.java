package com.yantra.controller;


import com.yantra.dto.response.AdminProfileResponse;
import com.yantra.service.AdminService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import io.swagger.v3.oas.annotations.Operation;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {


    private final AdminService adminService;


    @Operation(summary = "Load Admin Profile")
    @GetMapping("/profile")
    public ResponseEntity<AdminProfileResponse> getProfile(Authentication authentication){

        String email = authentication.getName();
        AdminProfileResponse adminProfile = adminService.getAdminProfile(email);
        return new ResponseEntity<>(adminProfile, HttpStatus.OK);
    }

}