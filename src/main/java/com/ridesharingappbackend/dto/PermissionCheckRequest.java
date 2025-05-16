package com.ridesharingappbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionCheckRequest {
    private Integer roleId;
    private String verb; //HTTP method (GET, POST, PUT, DELETE)
    private String path; // The exact request path (e.g., /api/users)
}
