package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.*;
import com.ridesharingappbackend.exceptions.BadRequestException;
import com.ridesharingappbackend.exceptions.ResourceNotFoundException;
import com.ridesharingappbackend.models.Company;
import com.ridesharingappbackend.models.User;
import com.ridesharingappbackend.repository.CompanyRepository;
import com.ridesharingappbackend.repository.UserRepository;
import com.ridesharingappbackend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final CompanyRepository companyRepo;
    private final UserRepository userRepo;

    public AuthController(AuthService authService, CompanyRepository companyRepo, UserRepository userRepo) {
        this.authService = authService;
        this.companyRepo = companyRepo;
        this.userRepo = userRepo;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest request, @RequestHeader("X-Company-Domain") String companyDomain) {
        Company company = companyRepo.findByDomain(companyDomain).orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        request.setCompanyId(company.getId());
        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request, @RequestHeader(value = "X-Company-Domain", required = false) String companyDomain) {
        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Check for SuperAdmin (roleId == 4)
        if (user.getRole().getId() == 4) {
            return ResponseEntity.ok(authService.login(request, null)); // null companyId for SuperAdmin
        }

        if (companyDomain == null || companyDomain.isEmpty()) {
            throw new BadRequestException("Company domain is required");
        }

        Company company = companyRepo.findByDomain(companyDomain)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        return ResponseEntity.ok(authService.login(request, company.getId()));
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        authService.initiatePasswordReset(request.getEmail(), request.getCompanyDomain());
        return ResponseEntity.ok("Reset link sent");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok("Password successfully changed");
    }

}
