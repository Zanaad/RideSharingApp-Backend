package com.ridesharingappbackend.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SignupRequest {
    public String firstName;
    public String lastName;
    public String email;
    public String password;
    public String phoneNumber;
    private Integer roleId;
    private Integer companyId;
}
