package com.ridesharingappbackend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCompanyRequest {
    @NotBlank
    private String name;
    private String address;
    private String contactEmail;
    private String contactPhone;
    private String domain;
}
