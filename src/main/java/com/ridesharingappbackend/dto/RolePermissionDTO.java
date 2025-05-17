package com.ridesharingappbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissionDTO {
    private Integer id;
    private Integer roleId;
    private Integer permissionId;
}
