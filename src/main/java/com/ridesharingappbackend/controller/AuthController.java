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

/**
 * AuthController menaxhon të gjitha kërkesat e lidhura me autentikimin dhe menaxhimin e llogarive.
 * Përfshin funksionalitete si regjistrimi i përdoruesve, autentikimi (login),
 * si dhe rikuperimi dhe ndryshimi i fjalëkalimit.
 *
 * Endpoint-et janë të ndara sipas skemës:
 *  - POST /api/auth/signup
 *  - POST /api/auth/login
 *  - POST /api/auth/forgot-password
 *  - POST /api/auth/reset-password
 */

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

    /**
     * Regjistron një përdorues të ri në sistem për një kompani të caktuar.
     *
     * @param request        të dhënat e regjistrimit si emri, email, fjalëkalimi, etj.
     * @param companyDomain  domain-i i kompanisë (vjen nga header-i "X-Company-Domain").
     * @return AuthResponse që përmban token-in JWT dhe të dhënat e përdoruesit.
     * @throws ResourceNotFoundException nëse kompania nuk gjendet në bazën e të dhënave.
     */
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest request, @RequestHeader("X-Company-Domain") String companyDomain) {
        Company company = companyRepo.findByDomain(companyDomain).orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        request.setCompanyId(company.getId());
        return ResponseEntity.ok(authService.signup(request));
    }

    /**
     * Ky endpoint autentifikon përdoruesin duke përdorur email dhe fjalëkalim.
     * Nëse përdoruesi është SuperAdmin (roleId == 4), kompania nuk kërkohet.
     *
     * @param request        kredencialet e përdoruesit.
     * @param companyDomain  (opsional) domain-i i kompanisë për përdoruesit jo-SuperAdmin.
     * @return AuthResponse me token-in JWT dhe të dhënat e përdoruesit.
     * @throws ResourceNotFoundException nëse përdoruesi ose kompania nuk gjenden.
     * @throws BadRequestException nëse mungon companyDomain për përdoruesit që nuk janë SuperAdmin.
     */
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

    /**
     * Nis procesin e rikuperimit të fjalëkalimit duke dërguar një email me link për reset.
     *
     * @param request përmban email-in e përdoruesit dhe domain-in e kompanisë.
     * @return përgjigje e suksesshme nëse linku u dërgua me sukses.
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        authService.initiatePasswordReset(request.getEmail(), request.getCompanyDomain());
        return ResponseEntity.ok("Reset link sent");
    }

    /**
     * Reseton fjalëkalimin e përdoruesit duke përdorur token-in e marrë nga email-i.
     *
     * @param request përmban token-in dhe fjalëkalimin e ri.
     * @return mesazh suksesi nëse fjalëkalimi u ndryshua me sukses.
     */
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok("Password successfully changed");
    }

}
