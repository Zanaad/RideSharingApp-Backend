package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.AuthRequest;
import com.ridesharingappbackend.dto.AuthResponse;
import com.ridesharingappbackend.dto.SignupRequest;
import com.ridesharingappbackend.exceptions.DuplicateUserException;
import com.ridesharingappbackend.exceptions.InvalidCredentialsException;
import com.ridesharingappbackend.exceptions.InvalidOrExpiredTokenException;
import com.ridesharingappbackend.exceptions.ResourceNotFoundException;
import com.ridesharingappbackend.mappers.UserMapper;
import com.ridesharingappbackend.models.Company;
import com.ridesharingappbackend.models.PasswordResetToken;
import com.ridesharingappbackend.models.Role;
import com.ridesharingappbackend.models.User;
import com.ridesharingappbackend.repository.CompanyRepository;
import com.ridesharingappbackend.repository.PasswordResetTokenRepository;
import com.ridesharingappbackend.repository.RoleRepository;
import com.ridesharingappbackend.repository.UserRepository;
import com.ridesharingappbackend.utils.EmailJob;
import com.ridesharingappbackend.utils.EmailQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CompanyRepository companyRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private PasswordResetTokenRepository tokenRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EmailQueue emailQueue;

    public AuthResponse signup(SignupRequest request) {
        if (userRepo.existsByEmailAndCompanyId(request.getEmail(), request.getCompanyId())) {
            throw new DuplicateUserException("User already registered");
        }

        Company company = companyRepo.findById(request.getCompanyId()).orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        Role role = roleRepo.findById(request.getRoleId()).orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        User user = userMapper.fromSignupRequest(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setCompany(company);
        user.setRole(role);

        User saved = userRepo.save(user);
        String token = jwtService.generateToken(saved);

        return new AuthResponse(token, role.getName(), company.getId());
    }

    public AuthResponse login(AuthRequest request, Integer companyId) {
        User user = userRepo.findByEmailAndCompanyId(request.getEmail(), companyId).orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        String token = jwtService.generateToken(user);
        return new AuthResponse(token, user.getRole().getName(), user.getCompany().getId());
    }

    public void initiatePasswordReset(String email, String companyDomain) {
        Company company = companyRepo.findByDomain(companyDomain)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        User user = userRepo.findByEmailAndCompanyId(email, company.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Safer deletion
        tokenRepo.findByUser(user).ifPresent(tokenRepo::delete);

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(3));

        tokenRepo.save(resetToken);

        emailQueue.enqueue(new EmailJob(
                user.getEmail(),
                "Reset your password",
                "Click the link to reset: http://localhost:3000/" +
                        user.getCompany().getDomain() +
                        "/reset-password?token=" + token
        ));
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepo.findByToken(token).orElseThrow(() -> new InvalidOrExpiredTokenException("Invalid token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new InvalidOrExpiredTokenException("Reset token has expired.");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
        tokenRepo.delete(resetToken);
    }
}
