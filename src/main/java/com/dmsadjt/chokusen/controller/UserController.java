package com.dmsadjt.chokusen.controller;

import com.dmsadjt.chokusen.dto.UpdateUserRequest;
import com.dmsadjt.chokusen.dto.UserResponse;
import com.dmsadjt.chokusen.entity.User;
import com.dmsadjt.chokusen.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping(path = "/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(
            userService.getAllUsers()
                .stream()
                .map(UserController::toResponse)
                .collect(Collectors.toList())
        );
    }

    @GetMapping(path = "/users/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID userId) {
        return ResponseEntity.ok(toResponse(userService.getUserById(userId)));
    }

    private static UserResponse toResponse(User user) {
        return new UserResponse(
            user.getUserId(),
            user.getUsername(),
            user.getEmail(),
            user.getCreatedAt()
        );
    }

    @PutMapping(path = "/users/{userId}")
    public ResponseEntity<Void> updateUser(
        @PathVariable UUID userId,
        @Valid @RequestBody UpdateUserRequest request,
        Authentication authentication
    ) {
        if (!isSelf(userId, authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        User patch = new User();
        patch.setEmail(request.getEmail());
        if (request.getPassword() != null) {
            patch.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        userService.updateUser(userId, patch);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = "/users/{userId}")
    public ResponseEntity<Void> deleteUser(
        @PathVariable UUID userId,
        Authentication authentication
    ) {
        if (!isSelf(userId, authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    private boolean isSelf(UUID userId, Authentication authentication) {
        User current = userService.getUserByUsername(authentication.getName());
        return current != null && current.getUserId().equals(userId);
    }
}
