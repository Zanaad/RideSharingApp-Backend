package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.RolePermissionDTO;
import com.ridesharingappbackend.service.RolePermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role-permissions")
public class RolePermissionController {

    private final RolePermissionService service;

    public RolePermissionController(RolePermissionService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<RolePermissionDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/role/{roleId}")
    public ResponseEntity<List<RolePermissionDTO>> getByRoleId(@PathVariable Integer roleId) {
        return ResponseEntity.ok(service.getByRoleId(roleId));
    }


    @PostMapping
    public ResponseEntity<RolePermissionDTO> create(@RequestBody RolePermissionDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

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
