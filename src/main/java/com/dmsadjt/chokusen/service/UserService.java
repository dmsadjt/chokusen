package com.dmsadjt.chokusen.service;

import com.dmsadjt.chokusen.entity.User;
import java.util.List;
import java.util.UUID;

public interface UserService {
    User getUserById(UUID userId);
    User getUserByUsername(String username);
    User createUser(User user);
    void updateUser(UUID userId, User user);
    void deleteUser(UUID userId);
    List<User> getAllUsers();
}
