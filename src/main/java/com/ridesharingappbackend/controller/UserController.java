package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.ChangePasswordRequest;
import com.ridesharingappbackend.dto.UserDTO;
import com.ridesharingappbackend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDTO> getAllUsers(HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return userService.getAllUsersByCompany(companyId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        return userService.getUserById(id, companyId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Integer id, @RequestBody UserDTO updatedUserDto, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        try {
            UserDTO userDto = userService.updateUser(id, updatedUserDto, companyId);
            return ResponseEntity.ok(userDto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        boolean deleted = userService.deleteUser(id, companyId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        Integer companyId = (Integer) request.getAttribute("companyId");
        if (userId == null || companyId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return userService.getUserById(userId, companyId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/change-password")
    public ResponseEntity<String> changePassword(@PathVariable Integer id, @RequestBody ChangePasswordRequest requestBody, HttpServletRequest request) {
        Integer companyId = (Integer) request.getAttribute("companyId");
        try {
            userService.changePassword(id, companyId, requestBody.getCurrentPassword(), requestBody.getNewPassword());
            return ResponseEntity.ok("Password changed successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
