package com.dmsadjt.chokusen.service.impl;

import com.dmsadjt.chokusen.entity.User;
import com.dmsadjt.chokusen.repository.UserRepository;
import com.dmsadjt.chokusen.service.UserService;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(UUID id) {
        return userRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void updateUser(UUID userId, User user) {
        user.setUserId(userId);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }
}
