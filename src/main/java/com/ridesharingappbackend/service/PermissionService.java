package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.CachedPermissionDTO;
import com.ridesharingappbackend.dto.PermissionDTO;
import com.ridesharingappbackend.mappers.PermissionMapper;
import com.ridesharingappbackend.models.Permission;
import com.ridesharingappbackend.models.RolePermission;
import com.ridesharingappbackend.repository.PermissionRepository;
import com.ridesharingappbackend.repository.RolePermissionRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionService {

    private final RolePermissionRepository rolePermissionRepository;
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    public PermissionService(RolePermissionRepository rolePermissionRepository,
                             PermissionRepository permissionRepository,
                             PermissionMapper permissionMapper) {
        this.rolePermissionRepository = rolePermissionRepository;
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
    }

    // Returns all cached permissions for a role

    @Cacheable(value = "role-permissions", key = "#roleId")
    public List<CachedPermissionDTO> getPermissionsByRole(Integer roleId) {
        System.out.println("Loading permissions from DB for roleId: " + roleId);
        return rolePermissionRepository.findAllByRoleId(roleId).stream()
                .map(permissionMapper::toDTO)
                .collect(Collectors.toList());
    }


    // Returns all permissions (for controller use)
    public List<PermissionDTO> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(permissionMapper::toDTO)
                .collect(Collectors.toList());
    }


    public PermissionDTO getPermissionById(Integer id) {
        return permissionRepository.findById(id)
                .map(permissionMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
    }


    public PermissionDTO createPermission(PermissionDTO dto) {
        Permission permission = permissionMapper.toEntity(dto);
        return permissionMapper.toDTO(permissionRepository.save(permission));
    }


    public PermissionDTO updatePermission(Integer id, PermissionDTO dto) {
        Permission existing = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found"));

        existing.setName(dto.getName());
        existing.setVerb(dto.getVerb());
        existing.setResource(dto.getResource());

        return permissionMapper.toDTO(permissionRepository.save(existing));
    }

    // Delete permission and evict cache for affected roles
    public void deletePermission(Integer id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found"));

        // Find and evict permissions cache for affected roles
        List<RolePermission> linkedRoles = rolePermissionRepository.findAllByPermissionId(id);
        for (RolePermission rp : linkedRoles) {
            evictPermissionsCache(rp.getRole().getId());
        }

        permissionRepository.delete(permission);
    }

    // Clear permissions cache for a specific role
    @CacheEvict(value = "role-permissions", key = "#roleId")
    public void evictPermissionsCache(Integer roleId) {
        System.out.println("Evicted permissions cache for roleId: " + roleId);
    }
}
