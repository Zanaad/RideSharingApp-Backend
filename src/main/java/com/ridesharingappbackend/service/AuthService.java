package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.AuthRequest;
import com.ridesharingappbackend.dto.AuthResponse;
import com.ridesharingappbackend.dto.SignupRequest;
import com.ridesharingappbackend.exceptions.DuplicateUserException;
import com.ridesharingappbackend.exceptions.InvalidCredentialsException;
import com.ridesharingappbackend.exceptions.ResourceNotFoundException;
import com.ridesharingappbackend.models.Company;
import com.ridesharingappbackend.models.User;
import com.ridesharingappbackend.repository.CompanyRepository;
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
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    public AuthResponse signup(SignupRequest request) {
        Company company = companyRepo.findById(request.getCompanyId()).orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        if (userRepo.existsByEmail(request.getEmail())) {
            throw new DuplicateUserException("User with this email already exists");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setUserType(request.getUserType());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCompany(company);
        user.setCreatedAt(LocalDateTime.now());

        User saved = userRepo.save(user);
        String token = jwtService.generateToken(saved);

        return new AuthResponse(token, saved.getUserType().name(), company.getId());
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepo.findByEmail(request.getEmail()).orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        String token = jwtService.generateToken(user);
        return new AuthResponse(token, user.getUserType().name(), user.getCompany().getId());
    }

}
