package com.ridesharingappbackend.mappers;

import com.ridesharingappbackend.dto.CachedPermissionDTO;
import com.ridesharingappbackend.models.RolePermission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    @Mapping(source = "permission.verb", target = "verb")
    @Mapping(source = "permission.resource", target = "resource")
    CachedPermissionDTO toDTO(RolePermission rolePermission);
}

