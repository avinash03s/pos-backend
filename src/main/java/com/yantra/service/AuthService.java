package com.yantra.service;

import com.yantra.dto.UserDTO;
import com.yantra.dto.request.LoginRequest;
import com.yantra.dto.request.RegisterRequest;
import com.yantra.dto.response.AuthResponse;

public interface AuthService {

//    void registerUser(UserDTO userDTO);
//    UserDTO loginUser(UserDTO userDTO);
//    UserDTO getUser(String email);

    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    UserDTO getUser(String email);
}
