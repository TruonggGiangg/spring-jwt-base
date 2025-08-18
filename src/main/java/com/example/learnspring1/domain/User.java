package com.example.learnspring1.domain;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "username is required")
    @Column(nullable = false, length = 50)
    private String username;

    @NotBlank(message = "email is required")
    @Column(nullable = false, length = 120)
    @Email(message = "email should be valid")
    private String email;

    @NotBlank(message = "password is required")
    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    private String avatarUrl;

    @Builder.Default
    private Instant createdAt = Instant.now();

    @Builder.Default
    private boolean enabled = true;
}