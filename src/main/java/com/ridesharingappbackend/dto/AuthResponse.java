package com.ridesharingappbackend.dto;

public class AuthResponse {
    public String token;
    public String role;
    public Integer companyId;

    public AuthResponse(String token, String role, Integer companyId) {
        this.token = token;
        this.role = role;
        this.companyId = companyId;
    }
}
