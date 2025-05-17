package com.ridesharingappbackend.mappers;

import com.ridesharingappbackend.dto.RoleDTO;
import com.ridesharingappbackend.models.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDTO toDTO(Role role);
}
