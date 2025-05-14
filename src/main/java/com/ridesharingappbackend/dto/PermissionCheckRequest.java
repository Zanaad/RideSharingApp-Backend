package com.ridesharingappbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionCheckRequest {
    private Integer roleId;
    private String verb;
    private String path;
}
