package com.yantra.service.serviceImp;

import com.yantra.constant.Roles;
import com.yantra.dto.UserDTO;
import com.yantra.dto.request.LoginRequest;
import com.yantra.dto.request.RegisterRequest;
import com.yantra.dto.response.AuthResponse;
import com.yantra.entity.Tenant;
import com.yantra.entity.User;
import com.yantra.exception.PosException;
import com.yantra.jwt.JwtUtil;
import com.yantra.repositroy.TenantRepository;
import com.yantra.repositroy.UserRepository;
import com.yantra.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new PosException("EMAIL_ALREADY_REGISTRED");
        }

        Tenant tenant = new Tenant();
        tenant.setName(request.businessName());
        tenant.setCreatedAt(LocalDateTime.now());
        tenant = tenantRepository.save(tenant);

        User admin = new User();
        admin.setName(request.adminName());
        admin.setEmail(request.email());
        admin.setPhone(request.phone());
        admin.setPassword(passwordEncoder.encode(request.password()));
        admin.setRole(Roles.ROLE_ADMIN);
        admin.setTenantId(tenant.getId());
        admin.setCreatedAt(LocalDateTime.now());
        admin = userRepository.save(admin);

        // Login-like flow -> Authenticate with the AuthenticationManager and create a UserDataTiles object
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);

        return new AuthResponse(token, admin.getId(), admin.getName(), admin.getEmail(),
                admin.getRole(), tenant.getId());
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
        } catch (Exception e) {
            throw new PosException("INVALID_CREDENTIALS");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new PosException("INVALID_CREDENTIALS"));

        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail(),
                user.getRole(), user.getTenantId());
    }

    @Override
    public UserDTO getUser(String email) {
        return userRepository.findByEmail(email)
                .map(User::toDTO)
                .orElse(null);
    }
}

//package com.yantra.service.serviceImp;
//
//import com.yantra.constant.Roles;
//import com.yantra.dto.UserDTO;
//import com.yantra.dto.request.LoginRequest;
//import com.yantra.dto.request.RegisterRequest;
//import com.yantra.dto.response.AuthResponse;
//import com.yantra.entity.Tenant;
//import com.yantra.entity.User;
//import com.yantra.exception.PosException;
//import com.yantra.jwt.JwtUtil;
//import com.yantra.repositroy.TenantRepository;
//import com.yantra.repositroy.UserRepository;
//import com.yantra.service.AuthService;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class AuthServiceImp implements AuthService {
//    private static final Logger log = LoggerFactory.getLogger(UserServiceImp.class);
//
//    private final TenantRepository tenantRepository;
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtUtil jwtUtil;
//
//    @Override
//    @Transactional
//    public AuthResponse register(RegisterRequest request) {
//        if (userRepository.existsByEmail(request.email())) {
//            throw new PosException("email.already.registered");
//        }
//
//        Tenant tenant = new Tenant();
//        tenant.setName(request.businessName());
//        tenant.setCreatedAt(LocalDateTime.now());
//        tenant = tenantRepository.save(tenant);
//
//        User admin = new User();
//        admin.setName(request.adminName());
//        admin.setEmail(request.email());
//        admin.setPhone(request.phone());
//        admin.setPassword(passwordEncoder.encode(request.password()));
//        admin.setRole(Roles.ROLE_ADMIN);
//        admin.setTenantId(tenant.getId());
//        admin.setCreatedAt(LocalDateTime.now());
//        admin = userRepository.save(admin);
//
//        String token = jwtUtil.generateToken(admin.getId(), admin.getEmail(), admin.getRole(), tenant.getId());
//
//        return new AuthResponse(token, admin.getId(), admin.getName(), admin.getEmail(),
//                admin.getRole(), tenant.getId());
//    }
//
//    @Override
//    public AuthResponse login(LoginRequest request) {
//        User user = userRepository.findByEmail(request.email())
//                .orElseThrow(() -> new PosException("invalid.credentials"));
//
//        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
//            throw new PosException("invalid.credentials");
//        }
//
//        user.setLastLoginAt(LocalDateTime.now());
//        userRepository.save(user);
//
//        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole(), user.getTenantId());
//
//        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail(),
//                user.getRole(), user.getTenantId());
//    }


//    @Override
//    public void registerUser(UserDTO userDTO) {
//        Optional<User> byEmail = authRepository.findByEmail(userDTO.getEmail());
//        if (byEmail.isPresent()) {
//            throw new PosException("USER_ALREADY_EXITS");
//        }
//        if (userDTO.getRole().equals(Roles.ROLE_ADMIN)){
//            throw new PosException("ADMIN IS NOT ALLOWED !");
//        }
//        userDTO.setRole(Roles.ROLE_USER);
//        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
//        userDTO.setLastLoginAt(LocalDateTime.now());
//        userDTO.setCreatedAt(LocalDateTime.now());
//        userDTO.setUpdatedAt(LocalDateTime.now());
//        authRepository.save(userDTO.toEntity());
//    }
//
//    @Override
//    public UserDTO loginUser(UserDTO userDTO) {
//        log.info("Login attempt for email: {}", userDTO.getEmail());
//        User user = authRepository.findByEmail(userDTO.getEmail())
//                .orElseThrow(() -> {
//                    log.warn("User not found for email: {}", userDTO.getEmail());
//                    return new PosException("USER_NOT_FOUND");
//                });
//        if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
//            log.warn("Invalid password attempt for email: {}", userDTO.getEmail());
//            throw new PosException("INVALID_CREDENTAILS");
//        }
//        log.info("User login successful for email: {}", userDTO.getEmail());
//        user.setPassword(null);
//        user.setLastLoginAt(LocalDateTime.now());
//        return user.toDTO();
//    }
//
//    @Override
//    public UserDTO getUser(String email) {
//        log.info("Fetching user by email: {}", email);
//        return authRepository.findByEmail(email)
//                .orElseThrow(() -> {
//                    log.warn("User not found with email: {}", email);
//                    return new PosException("USER_NOT_FOUND");
//                })
//                .toDTO();
//    }
//}
