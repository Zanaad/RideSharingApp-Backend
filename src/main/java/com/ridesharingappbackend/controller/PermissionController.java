package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.PermissionDTO;
import com.ridesharingappbackend.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@Tag(name = "Permissions", description = "Manage permissions")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Operation(summary = "Get all permissions")
    @ApiResponse(responseCode = "200", description = "List of permissions returned")
    @GetMapping
    public ResponseEntity<List<PermissionDTO>> getAllPermissions() {
        return ResponseEntity.ok(permissionService.getAllPermissions());
    }

    @Operation(summary = "Get a permission by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Permission returned"),
            @ApiResponse(responseCode = "404", description = "Permission not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PermissionDTO> getPermissionById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(permissionService.getPermissionById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create a new permission")
    @ApiResponse(responseCode = "200", description = "Permission created successfully")
    @PostMapping
    public ResponseEntity<PermissionDTO> createPermission(@RequestBody PermissionDTO dto) {
        return ResponseEntity.ok(permissionService.createPermission(dto));
    }

    @Operation(summary = "Update an existing permission")
    @ApiResponse(responseCode = "200", description = "Permission updated successfully")
    @PutMapping("/{id}")
    public ResponseEntity<PermissionDTO> updatePermission(@PathVariable Integer id, @RequestBody PermissionDTO dto) {
        return ResponseEntity.ok(permissionService.updatePermission(id, dto));
    }

    @Operation(summary = "Delete a permission by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Permission deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Permission not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable Integer id) {
        try {
            permissionService.deletePermission(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
