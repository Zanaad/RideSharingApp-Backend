package com.ridesharingappbackend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String phoneNumber;

    @ManyToOne()
    @JoinColumn(name = "role_id")
    private Role role;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
}