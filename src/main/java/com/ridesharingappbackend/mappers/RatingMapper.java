package com.ridesharingappbackend.mappers;

import com.ridesharingappbackend.dto.RatingDTO;
import com.ridesharingappbackend.models.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RatingMapper {

    @Mapping(source = "ride.id", target = "rideId")
    @Mapping(source = "driver.id", target = "driverId")
    @Mapping(source = "passenger.id", target = "passengerId")
    @Mapping(source = "company.id", target = "companyId")
    RatingDTO toDTO(Rating rating);

    @Mapping(target = "ride", source = "rideId", qualifiedByName = "mapRide")
    @Mapping(target = "driver", source = "driverId", qualifiedByName = "mapDriver")
    @Mapping(target = "passenger", source = "passengerId", qualifiedByName = "mapPassenger")
    @Mapping(target = "company", source = "companyId", qualifiedByName = "mapCompany")
    Rating toEntity(RatingDTO dto);

    @Named("mapRide")
    default Ride mapRide(Integer rideId) {
        if (rideId == null) return null;
        Ride ride = new Ride();
        ride.setId(rideId);
        return ride;
    }

    @Named("mapDriver")
    default Driver mapDriver(Integer driverId) {
        if (driverId == null) return null;
        Driver driver = new Driver();
        driver.setId(driverId);
        return driver;
    }

    @Named("mapPassenger")
    default User mapPassenger(Integer passengerId) {
        if (passengerId == null) return null;
        User user = new User();
        user.setId(passengerId);
        return user;
    }

    @Named("mapCompany")
    default Company mapCompany(Integer companyId) {
        if (companyId == null) return null;
        Company company = new Company();
        company.setId(companyId);
        return company;
    }
}
