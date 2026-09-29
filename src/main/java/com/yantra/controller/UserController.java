package com.yantra.controller;

import com.yantra.dto.UserDTO;
import com.yantra.entity.User;
import com.yantra.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getUserProfile(@RequestHeader("Authorization") String jwt){
       User userFormJwtToken = userService.getUserFormJwtToken(jwt);
       return ResponseEntity.ok(userFormJwtToken.toDTO());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") Long id){
        User userById = userService.getUserById(id);
        return ResponseEntity.ok(userById.toDTO());
    }

}
