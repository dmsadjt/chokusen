package com.dmsadjt.chokusen.service;

import com.dmsadjt.chokusen.entity.User;
import java.util.List;
import java.util.UUID;

public interface UserService {
    User getUserById(UUID userId);
    void updateUser(UUID userId, User user);
    void deleteUser(UUID userId);
    List<User> getAllUsers();
}
