package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.PermissionCheckRequest;
import com.ridesharingappbackend.models.RolePermission;
import com.ridesharingappbackend.repository.RolePermissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import java.util.List;

@Service
public class AccessControlService {

    private final RolePermissionRepository rolePermissionRepository;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public AccessControlService(RolePermissionRepository rolePermissionRepository) {
        this.rolePermissionRepository = rolePermissionRepository;
    }

    /**
     * Checks whether a role has permission for a given HTTP verb and exact path.
     *
     * @param roleId The ID of the role assigned to the user
     * @param verb   HTTP method (GET, POST, PUT, DELETE)
     * @param path   The exact request path (e.g., /api/v1/users/1)
     * @return true if access is allowed, false otherwise
     */

    public boolean isAccessAllowed(PermissionCheckRequest req) {
        List<RolePermission> permissions = rolePermissionRepository.findAllByRoleId(req.getRoleId());

        return permissions.stream().anyMatch(rp ->
                rp.getPermission().getVerb().name().equalsIgnoreCase(req.getVerb()) &&
                        pathMatcher.match(rp.getPermission().getResource(), req.getPath())
        );
    }

}
