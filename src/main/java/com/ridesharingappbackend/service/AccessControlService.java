package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.CachedPermissionDTO;
import com.ridesharingappbackend.dto.PermissionCheckRequest;
import com.ridesharingappbackend.models.RolePermission;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import java.util.List;

@Service
public class AccessControlService {

    private final PermissionService permissionService;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    public AccessControlService(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    /**
     * Uses cached permissions to check access rights.
     */
    public boolean isAccessAllowed(PermissionCheckRequest req) {
        List<CachedPermissionDTO> permissions = permissionService.getPermissionsByRole(req.getRoleId());

        return permissions.stream().anyMatch(p ->
                p.getVerb().equalsIgnoreCase(req.getVerb()) &&
                        pathMatcher.match(p.getResource(), req.getPath())
        );
    }
}
