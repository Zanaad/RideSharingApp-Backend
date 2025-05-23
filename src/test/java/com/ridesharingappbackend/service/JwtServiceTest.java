package com.ridesharingappbackend.service;

import com.ridesharingappbackend.models.Company;
import com.ridesharingappbackend.models.Role;
import com.ridesharingappbackend.models.User;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        // Set secret manually since @Value won't inject in unit tests
        ReflectionTestUtils.setField(jwtService, "jwtSecret", "mysupersecurekeythatshouldbeatleast64characterslong");
    }

    private User createMockUser() {
        Role role = new Role();
        role.setId(2);

        Company company = new Company();
        company.setId(3);

        User user = new User();
        user.setId(1);
        user.setEmail("user@example.com");
        user.setRole(role);
        user.setCompany(company);

        return user;
    }

    private User createMockUserWithoutCompany() {
        Role role = new Role();
        role.setId(5);

        User user = new User();
        user.setId(99);
        user.setEmail("nocompany@example.com");
        user.setRole(role);
        user.setCompany(null);

        return user;
    }

    @Test
    void generateToken_ShouldContainCorrectClaims() {
        User user = createMockUser();

        String token = jwtService.generateToken(user);
        Claims claims = jwtService.extractClaims(token);

        assertEquals(user.getEmail(), claims.getSubject());
        assertEquals(user.getId(), claims.get("userId", Integer.class));
        assertEquals(user.getRole().getId(), claims.get("roleId", Integer.class));
        assertEquals(user.getCompany().getId(), claims.get("companyId", Integer.class));
        assertNotNull(claims.getExpiration());
        assertTrue(claims.getExpiration().after(new Date()));
    }

    @Test
    void generateToken_ShouldHandleNullCompanyId() {
        User user = createMockUserWithoutCompany();

        String token = jwtService.generateToken(user);
        Claims claims = jwtService.extractClaims(token);

        assertEquals(user.getEmail(), claims.getSubject());
        assertEquals(user.getId(), claims.get("userId", Integer.class));
        assertEquals(user.getRole().getId(), claims.get("roleId", Integer.class));
        assertNull(claims.get("companyId")); // Should be null if no company
    }

    @Test
    void extractClaims_ShouldReturnCorrectClaims() {
        User user = createMockUser();
        String token = jwtService.generateToken(user);

        Claims claims = jwtService.extractClaims(token);

        assertEquals("user@example.com", claims.getSubject());
        assertEquals(1, claims.get("userId", Integer.class));
        assertEquals(2, claims.get("roleId", Integer.class));
        assertEquals(3, claims.get("companyId", Integer.class));
    }

    @Test
    void getCompanyIdFromToken_ShouldReturnCorrectCompanyId() {
        User user = createMockUser();
        String token = jwtService.generateToken(user);

        Integer companyId = jwtService.getCompanyIdFromToken(token);

        assertEquals(3, companyId);
    }

    @Test
    void getCompanyIdFromToken_ShouldReturnNull_WhenCompanyIdMissing() {
        User user = createMockUserWithoutCompany();
        String token = jwtService.generateToken(user);

        Integer companyId = jwtService.getCompanyIdFromToken(token);

        assertNull(companyId);
    }
}
