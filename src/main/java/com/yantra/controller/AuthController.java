package com.yantra.controller;

import com.yantra.dto.request.LoginRequest;
import com.yantra.dto.request.RegisterRequest;
import com.yantra.dto.response.AuthResponse;
import com.yantra.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    @Operation(summary = "Register New User (Business Owner)")
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest request) {
        log.info("Register attempt for email: {}", request.email());
        AuthResponse response = authService.register(request);
        log.info("User registered successfully with email: {}", request.email());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Login User (Business Owner and there Employees)")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        log.info("Login attempt for email: {}", request.email());
        AuthResponse response = authService.login(request);
        log.info("Login successful for email: {}", request.email());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

//package com.yantra.controller;
//
//import com.yantra.dto.LoginDTO;
//import com.yantra.dto.UserDTO;
//import com.yantra.dto.response.ResponseDTO;
//import com.yantra.exception.PosException;
//import com.yantra.jwt.JwtUtil;
//import com.yantra.service.AuthService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//public class AuthController {
//
//    private static final Logger log = LoggerFactory.getLogger(UserController.class);
//
//    private final AuthService authService;
//
//    private final UserDetailsService userDetailsService;
//
//    private final AuthenticationManager authenticationManager;
//
//    private final JwtUtil jwtUtil;
//
//    @PostMapping("/register")
//    public ResponseEntity<ResponseDTO> registerUser(@RequestBody @Valid UserDTO userDTO){
//        log.info("Received request to register user with email: {}", userDTO.getEmail());
//        authService.registerUser(userDTO);
//        log.info("User registered successfully with email: {}", userDTO.getEmail());
//        return new ResponseEntity<>(new ResponseDTO("Register Successfully."), HttpStatus.CREATED);
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody @Valid LoginDTO loginDTO){
//        log.info("Login attempt for email: {}", loginDTO.getEmail());
//        try {
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(),
//                            loginDTO.getPassword())
//            );
//            log.info("Authentication successful for email: {}", loginDTO.getEmail());
//        } catch (AuthenticationException e) {
//            log.warn("Invalid login attempt for email: {}", loginDTO.getEmail());
//            throw new PosException("INVALID_CREDENTIALS");
//        }
//        UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getEmail());
//        log.debug("User details loaded for email: {}", loginDTO.getEmail());
//        String jwt = jwtUtil.generateToken(userDetails);
//        log.info("JWT token generated for email: {}", loginDTO.getEmail());
//        return new ResponseEntity<>(jwt, HttpStatus.OK);
//    }
//}
