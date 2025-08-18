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
        // User newUser = new User();
        user.setPassword(encoder.encode(user.getPassword())); // Encode the password

        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Page<User> getUsersPage(Pageable pageable) {
        return userRepository.findAll(pageable);
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
            existing.setPassword(user.getPassword());
            return userRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return (User) user.get();
        }
        throw new RuntimeException("User not found with email " + email);
    }

}