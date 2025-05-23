package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.*;
import com.ridesharingappbackend.exceptions.BadRequestException;
import com.ridesharingappbackend.exceptions.ResourceNotFoundException;
import com.ridesharingappbackend.models.Company;
import com.ridesharingappbackend.models.Role;
import com.ridesharingappbackend.models.User;
import com.ridesharingappbackend.repository.CompanyRepository;
import com.ridesharingappbackend.repository.UserRepository;
import com.ridesharingappbackend.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @Mock
    private CompanyRepository companyRepo;

    @Mock
    private UserRepository userRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void signup_success() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("password");

        Company company = new Company();
        company.setId(1);
        company.setDomain("company.com");

        when(companyRepo.findByDomain("company.com")).thenReturn(Optional.of(company));
        when(authService.signup(any(SignupRequest.class))).thenReturn(new AuthResponse("token", "someValue", 1, 1));


        ResponseEntity<AuthResponse> response = authController.signup(signupRequest, "company.com");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("token", response.getBody().getToken());
        assertEquals(company.getId(), signupRequest.getCompanyId());
    }

    @Test
    void signup_companyNotFound_throwsException() {
        SignupRequest signupRequest = new SignupRequest();

        when(companyRepo.findByDomain("unknown.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            authController.signup(signupRequest, "unknown.com");
        });
    }

    @Test
    void login_superAdmin_success() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail("admin@example.com");

        Role superAdminRole = new Role();
        superAdminRole.setId(4);

        User user = new User();
        user.setEmail("admin@example.com");
        user.setRole(superAdminRole);

        when(userRepo.findByEmail("admin@example.com")).thenReturn(Optional.of(user));
        when(authService.login(any(AuthRequest.class), eq(null)))
                .thenReturn(new AuthResponse("admin-token", "adminUser", 4, 1));

        ResponseEntity<AuthResponse> response = authController.login(authRequest, null);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("admin-token", response.getBody().getToken());
    }


    @Test
    void login_nonSuperAdmin_missingCompanyDomain_throwsException() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail("user@example.com");

        Role userRole = new Role();
        userRole.setId(2);

        User user = new User();
        user.setEmail("user@example.com");
        user.setRole(userRole);

        when(userRepo.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> {
            authController.login(authRequest, null);
        });
    }

    @Test
    void login_nonSuperAdmin_success() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail("user@example.com");

        Role userRole = new Role();
        userRole.setId(1);

        User user = new User();
        user.setEmail("user@example.com");
        user.setRole(userRole);

        Company company = new Company();
        company.setId(1);
        company.setDomain("company.com");

        when(userRepo.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(companyRepo.findByDomain("company.com")).thenReturn(Optional.of(company));
        when(authService.login(any(AuthRequest.class), eq(1))).thenReturn(new AuthResponse("user-token", "user", 1, 1));

        ResponseEntity<AuthResponse> response = authController.login(authRequest, "company.com");

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("user-token", response.getBody().getToken());
    }


    @Test
    void login_userNotFound_throwsException() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail("nouser@example.com");

        when(userRepo.findByEmail("nouser@example.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            authController.login(authRequest, "company.com");
        });
    }

    @Test
    void forgotPassword_success() {
        ForgotPasswordRequest forgotRequest = new ForgotPasswordRequest();
        forgotRequest.setEmail("user@example.com");
        forgotRequest.setCompanyDomain("company.com");

        doNothing().when(authService).initiatePasswordReset("user@example.com", "company.com");

        ResponseEntity<?> response = authController.forgotPassword(forgotRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Reset link sent", response.getBody());
    }

    @Test
    void resetPassword_success() {
        ResetPasswordRequest resetRequest = new ResetPasswordRequest();
        resetRequest.setToken("reset-token");
        resetRequest.setNewPassword("newPassword123");

        doNothing().when(authService).resetPassword("reset-token", "newPassword123");

        ResponseEntity<?> response = authController.resetPassword(resetRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Password successfully changed", response.getBody());
    }
}
