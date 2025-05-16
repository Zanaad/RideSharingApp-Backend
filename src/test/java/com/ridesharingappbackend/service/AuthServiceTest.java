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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock private UserRepository userRepo;
    @Mock private CompanyRepository companyRepo;
    @Mock private RoleRepository roleRepo;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @Mock private UserMapper userMapper;

    @InjectMocks private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void signup_successful() {
        SignupRequest request = new SignupRequest();
        request.setEmail("test@example.com");
        request.setPassword("secret");
        request.setCompanyId(1);
        request.setRoleId(2);

        Company company = new Company();
        company.setId(1);

        Role role = new Role();
        role.setId(2);
        role.setName("ADMIN");

        User user = new User();
        User savedUser = new User();
        savedUser.setCompany(company);
        savedUser.setRole(role);

        when(userRepo.existsByEmailAndCompanyId("test@example.com", 1)).thenReturn(false);
        when(companyRepo.findById(1)).thenReturn(Optional.of(company));
        when(roleRepo.findById(2)).thenReturn(Optional.of(role));
        when(userMapper.fromSignupRequest(request)).thenReturn(user);
        when(passwordEncoder.encode("secret")).thenReturn("encodedSecret");
        when(userRepo.save(user)).thenReturn(savedUser);
        when(jwtService.generateToken(savedUser)).thenReturn("testToken");

        AuthResponse response = authService.signup(request);

        assertEquals("testToken", response.getToken());
        assertEquals("ADMIN", response.getRole());
        assertEquals(1, response.getCompanyId());
    }

    @Test
    void signup_duplicateEmailAndCompany_throws() {
        SignupRequest request = new SignupRequest();
        request.setEmail("test@example.com");
        request.setCompanyId(1);

        when(userRepo.existsByEmailAndCompanyId("test@example.com", 1)).thenReturn(true);

        assertThrows(DuplicateUserException.class, () -> authService.signup(request));
    }

    @Test
    void signup_companyNotFound_throws() {
        SignupRequest request = new SignupRequest();
        request.setEmail("test@example.com");
        request.setCompanyId(10);

        when(userRepo.existsByEmailAndCompanyId("test@example.com", 10)).thenReturn(false);
        when(companyRepo.findById(10)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authService.signup(request));
    }

    @Test
    void signup_roleNotFound_throws() {
        SignupRequest request = new SignupRequest();
        request.setEmail("test@example.com");
        request.setCompanyId(1);
        request.setRoleId(99);

        when(userRepo.existsByEmailAndCompanyId("test@example.com", 1)).thenReturn(false);
        when(companyRepo.findById(1)).thenReturn(Optional.of(new Company()));
        when(roleRepo.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authService.signup(request));
    }

    @Test
    void login_successful() {
        AuthRequest request = new AuthRequest();
        request.setEmail("test@example.com");
        request.setPassword("secret");
        Integer companyId = 5;

        Role role = new Role();
        role.setName("PASSENGER");

        Company company = new Company();
        company.setId(companyId);

        User user = new User();
        user.setPassword("encodedSecret");
        user.setCompany(company);
        user.setRole(role);

        when(userRepo.findByEmailAndCompanyId("test@example.com", companyId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("secret", "encodedSecret")).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("loginToken");

        AuthResponse response = authService.login(request, companyId);

        assertEquals("loginToken", response.getToken());
        assertEquals("PASSENGER", response.getRole());
        assertEquals(5, response.getCompanyId());
    }

    @Test
    void login_wrongPassword_throws() {
        AuthRequest request = new AuthRequest();
        request.setEmail("test@example.com");
        request.setPassword("wrong");
        Integer companyId = 1;

        User user = new User();
        user.setPassword("correctEncoded");

        when(userRepo.findByEmailAndCompanyId("test@example.com", companyId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "correctEncoded")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> authService.login(request, companyId));
    }

    @Test
    void login_userNotFound_throws() {
        AuthRequest request = new AuthRequest();
        request.setEmail("nonexistent@example.com");
        Integer companyId = 2;

        when(userRepo.findByEmailAndCompanyId("nonexistent@example.com", companyId)).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> authService.login(request, companyId));
    }
}
