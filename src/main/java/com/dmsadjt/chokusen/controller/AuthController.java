package com.dmsadjt.chokusen.controller;

import com.dmsadjt.chokusen.dto.LoginRequest;
import com.dmsadjt.chokusen.dto.RegisterRequest;
import com.dmsadjt.chokusen.entity.User;
import com.dmsadjt.chokusen.security.JwtUtil;
import com.dmsadjt.chokusen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping(path = "/auth/login")
    public ResponseEntity<String> login(
        @RequestBody LoginRequest loginRequest
    ) {
        User user = userService.getUserByUsername(loginRequest.getUsername());
        if (
            user == null ||
            !passwordEncoder.matches(
                loginRequest.getPassword(),
                user.getPassword()
            )
        ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        String token = jwtUtil.generateToken(loginRequest.getUsername());
        return ResponseEntity.ok(token);
    }

    @PostMapping(path = "/auth/register")
    public ResponseEntity<User> register(
        @RequestBody RegisterRequest registerRequest
    ) {
        User newUser = new User();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setEmail(registerRequest.getEmail());
        newUser.setPassword(
            passwordEncoder.encode(registerRequest.getPassword())
        );
        User createdUser = userService.createUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
}
