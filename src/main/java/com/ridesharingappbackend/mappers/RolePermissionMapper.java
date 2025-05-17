package com.ridesharingappbackend.mappers;

import com.ridesharingappbackend.dto.RolePermissionDTO;
import com.ridesharingappbackend.models.Permission;
import com.ridesharingappbackend.models.Role;
import com.ridesharingappbackend.models.RolePermission;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RolePermissionMapper {

    @Mapping(source = "role.id", target = "roleId")
    @Mapping(source = "permission.id", target = "permissionId")
    RolePermissionDTO toDTO(RolePermission rp);

    @Mapping(target = "role", source = "roleId", qualifiedByName = "mapRole")
    @Mapping(target = "permission", source = "permissionId", qualifiedByName = "mapPermission")
    RolePermission toEntity(RolePermissionDTO dto);

    @Named("mapRole")
    default Role mapRole(Integer id) {
        Role role = new Role();
        role.setId(id);
        return role;
    }

    @Named("mapPermission")
    default Permission mapPermission(Integer id) {
        Permission permission = new Permission();
        permission.setId(id);
        return permission;
    }
}
