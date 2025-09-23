package com.example.backendpractice.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime createdAt;
}