package com.yantra.service.serviceImp;

import com.yantra.entity.User;
import com.yantra.exception.PosException;
import com.yantra.jwt.JwtAuthFilter;
import com.yantra.repositroy.UserRepository;
import com.yantra.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final JwtAuthFilter jwtAuthFilter;

    @Override
    public User getUserFormJwtToken(String token) {
        return null;
    }

    @Override
    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new PosException("USER_NOT_FOUND"));
    }

    @Override
    public User getUserByEmail(String email) {
        String email1 = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email1).orElseThrow(() -> new PosException("USER_NOT_FOUND"));
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(()->new PosException("USER_NOT_FOUND"));
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }
}
