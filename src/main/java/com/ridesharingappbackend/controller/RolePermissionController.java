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

/**
 * RolePermissionController ofron endpoint-e REST për menaxhimin
 * e lidhjeve ndërmjet roleve dhe lejeve (permissions) në sistemin e ride-sharing.
 *
 * Përfshin funksionalitete për:
 * - Listimin e të gjitha lidhjeve role-permission
 * - Marrjen e lidhjeve sipas ID-së së rolit
 * - Krijimin dhe fshirjen e lidhjeve
 */
@RestController
@RequestMapping("/api/role-permissions")
@Tag(name = "Role Permissions", description = "Manage roles and permissions")
public class RolePermissionController {

    private final RolePermissionService service;

    public RolePermissionController(RolePermissionService service) {
        this.service = service;
    }

    /**
     * Kthen një listë me të gjitha lidhjet role-permission në sistem.
     *
     * @return lista e RolePermissionDTO-ve të mbështjellë me ResponseEntity.
     */
    @Operation(summary = "Get all role-permissions")
    @ApiResponse(responseCode = "200", description = "List of role-permissions returned")
    @GetMapping
    public ResponseEntity<List<RolePermissionDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    /**
     * Kthen të gjitha lidhjet role-permission për një rol të caktuar.
     *
     * @param roleId ID e rolit për të cilin kërkohen lejet.
     * @return lista e RolePermissionDTO-ve për rolin e dhënë.
     */
    @Operation(summary = "Get role-permissions by role ID")
    @ApiResponse(responseCode = "200", description = "Role-permissions returned")
    @GetMapping("/role/{roleId}")
    public ResponseEntity<List<RolePermissionDTO>> getByRoleId(@PathVariable Integer roleId) {
        return ResponseEntity.ok(service.getByRoleId(roleId));
    }

    /**
     * Krijon një lidhje të re ndërmjet një roli dhe një leje.
     *
     * @param dto objekti RolePermissionDTO që përmban të dhënat e lidhjes.
     * @return lidhja e krijuar e mbështjellë me ResponseEntity.
     */
    @Operation(summary = "Create a new role-permission")
    @ApiResponse(responseCode = "200", description = "Role-permission created successfully")
    @PostMapping
    public ResponseEntity<RolePermissionDTO> create(@RequestBody RolePermissionDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    /**
     * Fshin një lidhje role-permission sipas ID-së së dhënë.
     *
     * @param id ID e lidhjes që do fshihet.
     * @return 204 No Content nëse fshihet me sukses, përndryshe 404 Not Found.
     */
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
