package com.gamesup.api.model;

import com.gamesup.api.enumeration.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString(exclude = "password")
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users") // Using "users" as the table name to avoid conflict with SQL reserved keywords
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Review> reviews = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Order> orders = new HashSet<>();

    public User(Long id) {
        this.id = id;
    }

    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }
}