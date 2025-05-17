package com.ridesharingappbackend.mappers;

import com.ridesharingappbackend.dto.CachedPermissionDTO;
import com.ridesharingappbackend.dto.PermissionDTO;
import com.ridesharingappbackend.models.Permission;
import com.ridesharingappbackend.models.RolePermission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    // For caching role permissions
    @Mapping(source = "permission.verb", target = "verb")
    @Mapping(source = "permission.resource", target = "resource")
    CachedPermissionDTO toDTO(RolePermission rolePermission);

    // For full CRUD support
    PermissionDTO toDTO(Permission permission);
    Permission toEntity(PermissionDTO dto);
}
