package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.RolePermissionDTO;
import com.ridesharingappbackend.service.RolePermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role-permissions")
@Tag(name = "Role Permissions", description = "Manage roles and permissions")
public class RolePermissionController {

    private final RolePermissionService service;

    public RolePermissionController(RolePermissionService service) {
        this.service = service;
    }

    @Operation(summary = "Get all role-permissions")
    @ApiResponse(responseCode = "200", description = "List of role-permissions returned")
    @GetMapping
    public ResponseEntity<List<RolePermissionDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(summary = "Get role-permissions by role ID")
    @ApiResponse(responseCode = "200", description = "Role-permissions returned")
    @GetMapping("/role/{roleId}")
    public ResponseEntity<List<RolePermissionDTO>> getByRoleId(@PathVariable Integer roleId) {
        return ResponseEntity.ok(service.getByRoleId(roleId));
    }

    @Operation(summary = "Create a new role-permission")
    @ApiResponse(responseCode = "200", description = "Role-permission created successfully")
    @PostMapping
    public ResponseEntity<RolePermissionDTO> create(@RequestBody RolePermissionDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @Operation(summary = "Delete a role-permission by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Role-permission deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Role-permission not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
