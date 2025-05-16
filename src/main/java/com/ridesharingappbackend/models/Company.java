package com.ridesharingappbackend.models;

import com.ridesharingappbackend.enums.CompanyStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    private String address;
    private String contactEmail;
    private String contactPhone;
    private String domain;

    @Enumerated(EnumType.STRING)
    private CompanyStatus status = CompanyStatus.ACTIVE;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public String getName() {
        return name;
    }
    public void setId(Integer id) {
        this.id = id;
    }
}
