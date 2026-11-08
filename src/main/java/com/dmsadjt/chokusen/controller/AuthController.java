package com.dmsadjt.chokusen.controller;

import com.dmsadjt.chokusen.dto.LoginRequest;
import com.dmsadjt.chokusen.dto.MeResponse;
import com.dmsadjt.chokusen.dto.RegisterRequest;
import com.dmsadjt.chokusen.dto.RegisterResponse;
import com.dmsadjt.chokusen.entity.User;
import com.dmsadjt.chokusen.security.AuthErrorCode;
import com.dmsadjt.chokusen.security.JwtUtil;
import com.dmsadjt.chokusen.service.UserService;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
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

    @Value("${app.jwt.expiration}")
    private long jwtExpirationMs;

    @PostMapping(path = "/auth/login")
    public ResponseEntity<?> login(
        @Valid @RequestBody LoginRequest loginRequest
    ) {
        User user = userService.getUserByUsername(loginRequest.getUsername());
        if (
            user == null ||
            !passwordEncoder.matches(
                loginRequest.getPassword(),
                user.getPassword()
            )
        ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(AuthErrorCode.INVALID_CREDENTIALS);
        }
        String token = jwtUtil.generateToken(loginRequest.getUsername());
        ResponseCookie cookie = tokenCookie(token, jwtExpirationMs / 1000);
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .build();
    }

    @PostMapping(path = "/auth/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie cookie = tokenCookie("", 0);
        return ResponseEntity.noContent()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .build();
    }

    private ResponseCookie tokenCookie(String value, long maxAgeSeconds) {
        return ResponseCookie.from("token", value)
            .httpOnly(true)
            .secure(false) //change when going prod
            .sameSite("Strict")
            .path("/")
            .maxAge(maxAgeSeconds)
            .build();
    }

    @GetMapping(path = "/me")
    public ResponseEntity<MeResponse> me(Authentication authentication) {
        User user = userService.getUserByUsername(authentication.getName());
        return ResponseEntity.ok(
            new MeResponse(user.getUserId(), user.getUsername(), user.getEmail())
        );
    }

    @PostMapping(path = "/auth/register")
    public ResponseEntity<RegisterResponse> register(
        @Valid @RequestBody RegisterRequest registerRequest
    ) {
        User newUser = new User();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setEmail(registerRequest.getEmail());
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setPassword(
            passwordEncoder.encode(registerRequest.getPassword())
        );
        User createdUser = userService.createUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            new RegisterResponse(
                createdUser.getUserId(),
                createdUser.getUsername(),
                createdUser.getEmail(),
                createdUser.getCreatedAt()
            )
        );
    }
}
