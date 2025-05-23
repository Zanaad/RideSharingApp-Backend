package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.ChangePasswordRequest;
import com.ridesharingappbackend.dto.UserDTO;
import com.ridesharingappbackend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest httpServletRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsers_returnsList() {
        when(httpServletRequest.getAttribute("companyId")).thenReturn(1);

        UserDTO user = new UserDTO(1, null, null, "user1@example.com", null, null, 1, null);
        UserDTO user2 = new UserDTO(1, null, null, "user2@example.com", null, null, 1, null);

        when(userService.getAllUsersByCompany(1)).thenReturn(List.of(user, user2));

        List<UserDTO> result = userController.getAllUsers(httpServletRequest);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("user1@example.com", result.get(0).getEmail());
    }

    @Test
    void getUserById_existingUser_returnsUser() {
        when(httpServletRequest.getAttribute("companyId")).thenReturn(1);

        UserDTO user = new UserDTO();
        user.setId(1);
        user.setEmail("user@example.com");
        user.setCompanyId(1);

        when(userService.getUserById(1, 1)).thenReturn(Optional.of(user));

        ResponseEntity<UserDTO> response = userController.getUserById(1, httpServletRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.hasBody());
        assertEquals("user@example.com", response.getBody().getEmail());
    }


    @Test
    void getUserById_nonExistingUser_returnsNotFound() {
        when(httpServletRequest.getAttribute("companyId")).thenReturn(1);

        when(userService.getUserById(99, 1)).thenReturn(Optional.empty());

        ResponseEntity<UserDTO> response = userController.getUserById(99, httpServletRequest);

        assertEquals(404, response.getStatusCodeValue());
        assertFalse(response.hasBody());
    }

    @Test
    void updateUser_existingUser_returnsUpdatedUser() {
        when(httpServletRequest.getAttribute("companyId")).thenReturn(1);

        UserDTO updatedUser = new UserDTO();
        updatedUser.setId(1);
        updatedUser.setEmail("updated@example.com");
        updatedUser.setCompanyId(1);

        when(userService.updateUser(eq(1), any(UserDTO.class), eq(1))).thenReturn(updatedUser);

        ResponseEntity<UserDTO> response = userController.updateUser(1, updatedUser, httpServletRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("updated@example.com", response.getBody().getEmail());
    }


    @Test
    void updateUser_nonExistingUser_returnsNotFound() {
        when(httpServletRequest.getAttribute("companyId")).thenReturn(1);

        UserDTO updatedUser = new UserDTO(99, null, null, "updated@example.com", null, null, 1, null);


        when(userService.updateUser(eq(99), any(UserDTO.class), eq(1))).thenThrow(new RuntimeException());

        ResponseEntity<UserDTO> response = userController.updateUser(99, updatedUser, httpServletRequest);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void deleteUser_existingUser_returnsNoContent() {
        when(httpServletRequest.getAttribute("companyId")).thenReturn(1);
        when(userService.deleteUser(1, 1)).thenReturn(true);

        ResponseEntity<Void> response = userController.deleteUser(1, httpServletRequest);

        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void deleteUser_nonExistingUser_returnsNotFound() {
        when(httpServletRequest.getAttribute("companyId")).thenReturn(1);
        when(userService.deleteUser(99, 1)).thenReturn(false);

        ResponseEntity<Void> response = userController.deleteUser(99, httpServletRequest);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void getCurrentUser_withValidIds_returnsUser() {
        when(httpServletRequest.getAttribute("userId")).thenReturn(1);
        when(httpServletRequest.getAttribute("companyId")).thenReturn(1);

        UserDTO user = new UserDTO(1, null, null, "user@example.com", null, null, 1, null);

        when(userService.getUserById(1, 1)).thenReturn(Optional.of(user));

        ResponseEntity<UserDTO> response = userController.getCurrentUser(httpServletRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("user@example.com", response.getBody().getEmail());
    }

    @Test
    void getCurrentUser_missingIds_returnsUnauthorized() {
        when(httpServletRequest.getAttribute("userId")).thenReturn(null);
        when(httpServletRequest.getAttribute("companyId")).thenReturn(1);

        ResponseEntity<UserDTO> response = userController.getCurrentUser(httpServletRequest);

        assertEquals(401, response.getStatusCodeValue());
    }

    @Test
    void changePassword_success() {
        when(httpServletRequest.getAttribute("companyId")).thenReturn(1);

        ChangePasswordRequest req = new ChangePasswordRequest();
        req.setCurrentPassword("oldPass");
        req.setNewPassword("newPass");

        doNothing().when(userService).changePassword(1, 1, "oldPass", "newPass");

        ResponseEntity<String> response = userController.changePassword(1, req, httpServletRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Password changed successfully", response.getBody());
    }

    @Test
    void changePassword_failure() {
        when(httpServletRequest.getAttribute("companyId")).thenReturn(1);

        ChangePasswordRequest req = new ChangePasswordRequest();
        req.setCurrentPassword("oldPass");
        req.setNewPassword("newPass");

        doThrow(new RuntimeException("Invalid current password"))
                .when(userService).changePassword(1, 1, "oldPass", "newPass");

        ResponseEntity<String> response = userController.changePassword(1, req, httpServletRequest);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid current password", response.getBody());
    }
}
