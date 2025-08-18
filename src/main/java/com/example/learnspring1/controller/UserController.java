package com.example.learnspring1.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.learnspring1.domain.User;
import com.example.learnspring1.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder encoder;

    public UserController(UserService userService, PasswordEncoder encoder) {
        this.userService = userService;
        this.encoder = encoder;
    }

    // Create
    @PostMapping
    public User createNewUser(@Valid @RequestBody User input) {
        User user = this.userService.createUser(input, encoder);
        return user;
    }

    // Lấy tất cả
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Phân trang
    @GetMapping("/page")
    public Page<User> getUsersPage(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return userService.getUsersPage(pageable);
    }

    // Tìm theo ID
    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Long id) {
        return userService.getUserById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id " + id));
    }

    // Tìm theo tên
    @GetMapping("/search")
    public List<User> getUsersByName(@RequestParam("name") String name) {
        return userService.getUsersByName(name);
    }

    // Cập nhật
    @PutMapping("/{id}")
    public User updateUser(
            @PathVariable("id") Long id,
            @Valid @RequestBody User input) {
        return userService.updateUser(id, input);
    }

    // Xóa
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
