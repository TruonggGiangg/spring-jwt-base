package com.example.learnspring1.repository;

import org.springframework.stereotype.Repository;

import com.example.learnspring1.domain.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByUsernameContainingIgnoreCase(String username);

    // You can define custom query methods if needed, for example:
    // List<User> findByEmail(String email);
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

}
