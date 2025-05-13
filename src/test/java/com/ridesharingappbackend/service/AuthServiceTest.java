package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.AuthRequest;
import com.ridesharingappbackend.dto.AuthResponse;
import com.ridesharingappbackend.dto.SignupRequest;
import com.ridesharingappbackend.enums.UserType;
import com.ridesharingappbackend.exceptions.DuplicateUserException;
import com.ridesharingappbackend.exceptions.InvalidCredentialsException;
import com.ridesharingappbackend.exceptions.ResourceNotFoundException;
import com.ridesharingappbackend.models.Company;
import com.ridesharingappbackend.models.User;
import com.ridesharingappbackend.repository.CompanyRepository;
import com.ridesharingappbackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class AuthServiceTest {

    @Mock
    private UserRepository userRepo;
    @Mock
    private CompanyRepository companyRepo;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void signup_WhenValidRequest_ReturnsAuthResponse() {
        SignupRequest req = new SignupRequest();
        req.setEmail("test@example.com");
        req.setPassword("123");
        req.setCompanyId(1);
        req.setFirstName("John");
        req.setLastName("Doe");
        req.setPhoneNumber("12345");
        req.setUserType(UserType.DRIVER);

        Company company = new Company();
        company.setId(1);
        User user = new User();
        user.setId(1);
        user.setCompany(company);
        user.setUserType(UserType.DRIVER);

        when(companyRepo.findById(1)).thenReturn(Optional.of(company));
        when(userRepo.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("123")).thenReturn("encoded123");
        when(userRepo.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("mocked-jwt");

        AuthResponse response = authService.signup(req);

        assertNotNull(response);
        assertEquals("mocked-jwt", response.getToken());
        assertEquals("DRIVER", response.getRole());
        assertEquals(1, response.getCompanyId());
    }

    @Test
    void signup_WhenEmailExists_ThrowsDuplicateUserException() {
        SignupRequest req = new SignupRequest();
        req.setEmail("test@example.com");
        req.setCompanyId(1);

        Company company = new Company();
        company.setId(1);

        when(companyRepo.findById(1)).thenReturn(Optional.of(company));
        when(userRepo.existsByEmail("test@example.com")).thenReturn(true);

        assertThrows(DuplicateUserException.class, () -> authService.signup(req));
    }

    @Test
    void signup_WhenCompanyNotFound_ThrowsResourceNotFoundException() {
        SignupRequest req = new SignupRequest();
        req.setCompanyId(999);

        when(companyRepo.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authService.signup(req));
    }

    @Test
    void login_WhenValid_ReturnsAuthResponse() {
        AuthRequest req = new AuthRequest();
        req.setEmail("user@example.com");
        req.setPassword("raw123");

        Company company = new Company();
        company.setId(10);
        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword("encoded123");
        user.setUserType(UserType.ADMIN);
        user.setCompany(company);

        when(userRepo.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("raw123", "encoded123")).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("jwt-token");

        AuthResponse response = authService.login(req);

        assertNotNull(response);
        assertEquals("ADMIN", response.getRole());
        assertEquals("jwt-token", response.getToken());
        assertEquals(10, response.getCompanyId());
    }

    @Test
    void login_WhenEmailNotFound_ThrowsInvalidCredentials() {
        AuthRequest req = new AuthRequest();
        req.setEmail("noone@example.com");

        when(userRepo.findByEmail("noone@example.com")).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> authService.login(req));
    }

    @Test
    void login_WhenPasswordWrong_ThrowsInvalidCredentials() {
        AuthRequest req = new AuthRequest();
        req.setEmail("user@example.com");
        req.setPassword("wrong123");

        User user = new User();
        user.setPassword("encodedPassword");

        when(userRepo.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong123", "encodedPassword")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> authService.login(req));
    }
}
