package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.PermissionCheckRequest;
import com.ridesharingappbackend.models.Permission;
import com.ridesharingappbackend.models.RolePermission;
import com.ridesharingappbackend.repository.RolePermissionRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccessControlService {

    private final RolePermissionRepository rolePermissionRepository;

    public AccessControlService(RolePermissionRepository rolePermissionRepository) {
        this.rolePermissionRepository = rolePermissionRepository;
    }

    //Cashes permissions
    @Cacheable(value = "role-permissions", key = "#roleId")
    public List<RolePermission> getPermissionsByRole(Integer roleId) {
        System.out.println("Premissions");
        System.out.println("roleId " + roleId);
        return rolePermissionRepository.findAllByRoleId(roleId);
    }


     // Uses cached permissions to check access rights.
    public boolean isAccessAllowed(PermissionCheckRequest req) {
        List<RolePermission> permissions = getPermissionsByRole(req.getRoleId());

        return permissions.stream().anyMatch(rp ->
                rp.getPermission().getVerb().name().equalsIgnoreCase(req.getVerb()) &&
                        rp.getPermission().getResource().equalsIgnoreCase(req.getPath())
        );
    }

    @CacheEvict(value = "role-permissions", key = "#roleId")
    public void evictRolePermissionCache(Integer roleId) {
        System.out.println("Evicted cached permissions for role: " + roleId);
    }
}
