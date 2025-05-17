package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.AuthRequest;
import com.ridesharingappbackend.dto.AuthResponse;
import com.ridesharingappbackend.dto.SignupRequest;
import com.ridesharingappbackend.exceptions.ResourceNotFoundException;
import com.ridesharingappbackend.models.Company;
import com.ridesharingappbackend.repository.CompanyRepository;
import com.ridesharingappbackend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final CompanyRepository companyRepo;

    public AuthController(AuthService authService, CompanyRepository companyRepo) {
        this.authService = authService;
        this.companyRepo = companyRepo;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest request,
                                               @RequestHeader("X-Company-Domain") String companyDomain) {
        Company company = companyRepo.findByDomain(companyDomain)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        request.setCompanyId(company.getId());
        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request,
                                              @RequestHeader("X-Company-Domain") String companyDomain) {
        Company company = companyRepo.findByDomain(companyDomain)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        return ResponseEntity.ok(authService.login(request, company.getId()));
    }
}
