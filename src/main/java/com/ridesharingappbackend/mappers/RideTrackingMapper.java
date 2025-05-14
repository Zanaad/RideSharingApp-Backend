package com.ridesharingappbackend.mappers;

import com.ridesharingappbackend.dto.RideTrackingDTO;
import com.ridesharingappbackend.models.Company;
import com.ridesharingappbackend.models.Ride;
import com.ridesharingappbackend.models.RideTracking;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RideTrackingMapper {

    @Mapping(source = "ride.id", target = "rideId")
    @Mapping(source = "company.id", target = "companyId")
    RideTrackingDTO toDTO(RideTracking entity);

    @Mapping(target = "ride", source = "rideId", qualifiedByName = "mapRide")
    @Mapping(target = "company", source = "companyId", qualifiedByName = "mapCompany")
    RideTracking toEntity(RideTrackingDTO dto);

    @Named("mapRide")
    default Ride mapRide(Integer id) {
        if (id == null) return null;
        Ride ride = new Ride();
        ride.setId(id);
        return ride;
    }

    @Named("mapCompany")
    default Company mapCompany(Integer id) {
        if (id == null) return null;
        Company company = new Company();
        company.setId(id);
        return company;
    }
}
