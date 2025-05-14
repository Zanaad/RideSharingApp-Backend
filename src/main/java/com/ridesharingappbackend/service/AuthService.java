package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.AuthRequest;
import com.ridesharingappbackend.dto.AuthResponse;
import com.ridesharingappbackend.dto.SignupRequest;
import com.ridesharingappbackend.exceptions.DuplicateUserException;
import com.ridesharingappbackend.exceptions.InvalidCredentialsException;
import com.ridesharingappbackend.exceptions.ResourceNotFoundException;
import com.ridesharingappbackend.mappers.UserMapper;
import com.ridesharingappbackend.models.Company;
import com.ridesharingappbackend.models.Role;
import com.ridesharingappbackend.models.User;
import com.ridesharingappbackend.repository.CompanyRepository;
import com.ridesharingappbackend.repository.RoleRepository;
import com.ridesharingappbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CompanyRepository companyRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserMapper userMapper;

    public AuthResponse signup(SignupRequest request) {
        if (userRepo.existsByEmail(request.getEmail())) {
            throw new DuplicateUserException("User with this email already exists");
        }

        Company company = companyRepo.findById(request.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        Role role = roleRepo.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        // Map SignupRequest to User (excluding role & company)
        User user = userMapper.fromSignupRequest(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setCompany(company);
        user.setRole(role);

        User saved = userRepo.save(user);
        String token = jwtService.generateToken(saved);

        return new AuthResponse(token, role.getName(), company.getId());
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        String token = jwtService.generateToken(user);
        return new AuthResponse(token, user.getRole().getName(), user.getCompany().getId());
    }
}
