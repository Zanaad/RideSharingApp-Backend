package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.CachedPermissionDTO;
import com.ridesharingappbackend.mappers.PermissionMapper;
import com.ridesharingappbackend.models.RolePermission;
import com.ridesharingappbackend.repository.RolePermissionRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionService {

    private final RolePermissionRepository rolePermissionRepository;
    private final PermissionMapper permissionMapper;

    public PermissionService(RolePermissionRepository rolePermissionRepository,
                             PermissionMapper permissionMapper) {
        this.rolePermissionRepository = rolePermissionRepository;
        this.permissionMapper = permissionMapper;
    }

    @Cacheable(value = "role-permissions", key = "#roleId")
    public List<CachedPermissionDTO> getPermissionsByRole(Integer roleId) {
        System.out.println("Loading permissions from DB for roleId: " + roleId);
        return rolePermissionRepository.findAllByRoleId(roleId).stream()
                .map(permissionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @CacheEvict(value = "role-permissions", key = "#roleId")
    public void evictPermissionsCache(Integer roleId) {
        System.out.println("Evicted permissions cache for roleId: " + roleId);
    }
}
