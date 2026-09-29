package com.yantra.service;

import com.yantra.dto.UserDTO;
import com.yantra.entity.User;

import java.util.List;

public interface UserService {

    User getUserFormJwtToken(String token);
    User getCurrentUser();
    User getUserByEmail(String email);
    User getUserById(Long id);
    List<User> getAllUser();

}
