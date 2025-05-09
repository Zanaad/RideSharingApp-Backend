package com.ridesharingappbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {
    public String email;
    public String password;
}
