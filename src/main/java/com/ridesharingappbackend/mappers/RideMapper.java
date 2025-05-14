package com.ridesharingappbackend.mappers;

import com.ridesharingappbackend.dto.RideDTO;
import com.ridesharingappbackend.models.Company;
import com.ridesharingappbackend.models.Driver;
import com.ridesharingappbackend.models.Ride;
import com.ridesharingappbackend.models.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RideMapper {

    @Mapping(source = "passenger.id", target = "passengerId")
    @Mapping(source = "driver.id", target = "driverId")
    @Mapping(source = "company.id", target = "companyId")
    RideDTO toDTO(Ride ride);

    @Mapping(target = "passenger", source = "passengerId", qualifiedByName = "mapPassenger")
    @Mapping(target = "driver", source = "driverId", qualifiedByName = "mapDriver")
    @Mapping(target = "company", source = "companyId", qualifiedByName = "mapCompany")
    Ride toEntity(RideDTO dto);

    @Named("mapPassenger")
    default User mapPassenger(Integer passengerId) {
        if (passengerId == null) return null;
        User user = new User();
        user.setId(passengerId);
        return user;
    }

    @Named("mapDriver")
    default Driver mapDriver(Integer driverId) {
        if (driverId == null) return null;
        Driver driver = new Driver();
        driver.setId(driverId);
        return driver;
    }

    @Named("mapCompany")
    default Company mapCompany(Integer companyId) {
        if (companyId == null) return null;
        Company company = new Company();
        company.setId(companyId);
        return company;
    }
}
