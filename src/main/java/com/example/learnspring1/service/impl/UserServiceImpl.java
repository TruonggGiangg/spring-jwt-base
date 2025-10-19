package com.example.learnspring1.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.learnspring1.domain.User;
import com.example.learnspring1.repository.UserRepository;
import com.example.learnspring1.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user, PasswordEncoder encoder) {
        // Kiểm tra trùng email
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }
        // Kiểm tra trùng username
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("UserName đã tồn tại");
        }
        user.setPassword(encoder.encode(user.getPassword())); // Encode the password
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findByIsActiveTrue();
    }

    @Override
    public Page<User> getUsersPage(Pageable pageable) {
        return userRepository.findByIsActiveTrue(pageable);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> getUsersByName(String name) {
        return userRepository.findByUsernameContainingIgnoreCase(name);
    }

    @Override
    public User updateUser(Long id, User user) {
        return userRepository.findById(id).map(existing -> {
            existing.setUsername(user.getUsername());
            existing.setEmail(user.getEmail());
            // KHÔNG cho phép update password qua endpoint này
            // Dùng endpoint riêng để đổi password
            if (user.getAvatarUrl() != null) {
                existing.setAvatarUrl(user.getAvatarUrl());
            }
            return userRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new java.util.NoSuchElementException("User not found with id " + id));
        user.softDelete();
        userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return (User) user.get();
        }
        throw new RuntimeException("User not found with email " + email);
    }

    @Override
    public User changePassword(Long id, String newPassword, PasswordEncoder encoder) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id " + id));
        user.setPassword(encoder.encode(newPassword));
        return userRepository.save(user);
    }

}