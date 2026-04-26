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
        User existingUser = userRepository
            .findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        existingUser.setUsername(user.getUsername());
        userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
