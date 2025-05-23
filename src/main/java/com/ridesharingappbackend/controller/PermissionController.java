package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.PermissionDTO;
import com.ridesharingappbackend.service.PermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PermissionController ofron endpoint-e REST për menaxhimin e të drejtave të aksesit (permissions).
 *
 * Kjo klasë përfshin operacione për të marrë të gjitha të drejtat, për të marrë një të drejtë sipas ID-së,
 * për të krijuar, përditësuar dhe fshirë një të drejtë ekzistuese.
 *
 * Endpoint-et zakonisht përdoren për administrimin e roleve dhe të drejtave në sistemin e menaxhimit të përdoruesve.
 */
@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    /**
     * Kthen listën e të gjitha të drejtave (permissions) në sistem.
     *
     * @return një listë me PermissionDTO që përfaqësojnë të gjitha të drejtat e regjistruara.
     */
    @GetMapping
    public ResponseEntity<List<PermissionDTO>> getAllPermissions() {
        return ResponseEntity.ok(permissionService.getAllPermissions());
    }

    /**
     * Kthen një të drejtë të veçantë sipas ID-së.
     *
     * @param id ID-ja e të drejtës që kërkohet.
     * @return objekti PermissionDTO nëse gjendet, përndryshe 404 NOT_FOUND.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PermissionDTO> getPermissionById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(permissionService.getPermissionById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Krijon një të drejtë të re në sistem.
     *
     * @param dto objekti PermissionDTO që përmban të dhënat e të drejtës së re.
     * @return objekti PermissionDTO i sapo krijuar.
     */
    @PostMapping
    public ResponseEntity<PermissionDTO> createPermission(@RequestBody PermissionDTO dto) {
        return ResponseEntity.ok(permissionService.createPermission(dto));
    }

    /**
     * Përditëson një të drejtë ekzistuese sipas ID-së së dhënë.
     *
     * @param id ID-ja e të drejtës që do të përditësohet.
     * @param dto të dhënat e reja të përditësuara në formatin DTO.
     * @return objekti PermissionDTO i përditësuar.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PermissionDTO> updatePermission(@PathVariable Integer id,
                                                          @RequestBody PermissionDTO dto) {
        return ResponseEntity.ok(permissionService.updatePermission(id, dto));
    }

    /**
     * Fshin një të drejtë ekzistuese sipas ID-së së dhënë.
     *
     * @param id ID-ja e të drejtës që do të fshihet.
     * @return 204 NO_CONTENT nëse fshihet me sukses, ose 404 NOT_FOUND nëse nuk ekziston.
     */
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
