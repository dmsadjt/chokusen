package com.dmsadjt.chokusen.service.impl;

import com.dmsadjt.chokusen.exception.DuplicateResourceException;
import com.dmsadjt.chokusen.exception.ResourceNotFoundException;
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
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public void updateUser(UUID userId, User user) {
        User existingUser = userRepository
            .findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (
            user.getEmail() != null &&
            !user.getEmail().equals(existingUser.getEmail())
        ) {
            if (userRepository.findByEmail(user.getEmail()) != null) {
                throw new DuplicateResourceException("Email already registered");
            }
            existingUser.setEmail(user.getEmail());
        }
        if (user.getPassword() != null) {
            existingUser.setPassword(user.getPassword());
        }
        userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public User createUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new DuplicateResourceException("Username already taken");
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new DuplicateResourceException("Email already registered");
        }
        return userRepository.save(user);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
