package com.ridesharingappbackend.dto;

import com.ridesharingappbackend.enums.UserType;
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
    public UserType userType;
    private Integer companyId;
}
