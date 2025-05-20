package com.ridesharingappbackend.mappers;

import com.ridesharingappbackend.dto.DriverDashboardDTO;
import com.ridesharingappbackend.models.Driver;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DriverDashboardMapper {

    @Mapping(source = "id", target = "driverId")
    @Mapping(target = "fullName", expression = "java(driver.getUser().getFirstName() + \" \" + driver.getUser().getLastName())")
    @Mapping(source = "rating", target = "rating")
    @Mapping(source = "status", target = "status")
    DriverDashboardDTO toDTO(Driver driver);

    // Remove toEntity and update method because DriverDashboardDTO has no userId or companyId
}
