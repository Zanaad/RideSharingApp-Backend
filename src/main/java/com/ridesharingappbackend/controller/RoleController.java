package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.RoleDTO;
import com.ridesharingappbackend.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * RoleController ofron endpoint-e REST për menaxhimin e roleve të përdoruesve
 * në sistemin e ride-sharing.
 *
 * Përfshin funksionalitete për:
 * - Listimin e të gjitha roleve
 * - Kërkimin e një roli sipas ID-së
 * - Krijimin, përditësimin dhe fshirjen e roleve
 */
@RestController
@RequestMapping("/api/roles")
@Tag(name = "Roles", description = "Manage roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * Kthen një listë të të gjitha roleve ekzistuese në sistem.
     *
     * @return lista e RoleDTO-ve të mbështjellë me ResponseEntity.
     */
    @Operation(summary = "Get all roles")
    @ApiResponse(responseCode = "200", description = "List of roles returned")
    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    /**
     * Kthen një rol sipas ID-së së dhënë.
     *
     * @param id ID e rolit që kërkohet.
     * @return roli përkatës nëse ekziston, përndryshe përgjigje 404.
     */
    @Operation(summary = "Get a role by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Role returned"),
            @ApiResponse(responseCode = "404", description = "Role not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(roleService.getRoleById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Krijon një rol të ri në sistem me të dhënat e dhëna.
     *
     * @param dto objekt RoleDTO që përmban të dhënat e rolit.
     * @return roli i krijuar i mbështjellë me ResponseEntity.
     */
    @Operation(summary = "Create a new role")
    @ApiResponse(responseCode = "200", description = "Role created successfully")
    @PostMapping
    public ResponseEntity<RoleDTO> createRole(@RequestBody RoleDTO dto) {
        return ResponseEntity.ok(roleService.createRole(dto));
    }

    /**
     * Përditëson një rol ekzistues me ID-në e dhënë.
     *
     * @param id ID e rolit që do përditësohet.
     * @param dto objekt RoleDTO me të dhënat e përditësuara.
     * @return roli i përditësuar i mbështjellë me ResponseEntity.
     */
    @Operation(summary = "Update an existing role")
    @ApiResponse(responseCode = "200", description = "Role updated successfully")
    @PutMapping("/{id}")
    public ResponseEntity<RoleDTO> updateRole(@PathVariable Integer id, @RequestBody RoleDTO dto) {
        return ResponseEntity.ok(roleService.updateRole(id, dto));
    }

    /**
     * Fshin një rol sipas ID-së së dhënë.
     *
     * @param id ID e rolit që do fshihet.
     * @return 204 No Content nëse fshihet me sukses, përndryshe 404 Not Found.
     */
    @Operation(summary = "Delete a role by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Role deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Role not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Integer id) {
        try {
            roleService.deleteRole(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
