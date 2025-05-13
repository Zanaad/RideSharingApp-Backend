package com.ridesharingappbackend.dto;

import lombok.Getter;

@Getter
public class AuthResponse {
    private String token;
    private String role;
    private Integer companyId;

    public AuthResponse(String token, String role, Integer companyId) {
        this.token = token;
        this.role = role;
        this.companyId = companyId;
    }
}
