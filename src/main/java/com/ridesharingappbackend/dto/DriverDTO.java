package com.ridesharingappbackend.dto;

import com.ridesharingappbackend.enums.DriverStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriverDTO {

    private Integer id;
    private UserDTO user;
    private float rating;
    private DriverStatus status;
    private Integer companyId;


}
