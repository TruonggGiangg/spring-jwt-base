package com.example.learnspring1.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.learnspring1.domain.User;

public interface UserService {
    User createUser(User user, PasswordEncoder encoder);

    List<User> getAllUsers();

    Page<User> getUsersPage(Pageable pageable);

    Optional<User> getUserById(Long id);

    User getUserByEmail(String email);

    List<User> getUsersByName(String name);

    User updateUser(Long id, User user);

    void deleteUser(Long id);
    
    User changePassword(Long id, String newPassword, PasswordEncoder encoder);
}
