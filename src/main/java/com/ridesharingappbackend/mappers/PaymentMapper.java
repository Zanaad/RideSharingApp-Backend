package com.ridesharingappbackend.mappers;

import com.ridesharingappbackend.dto.PaymentDTO;
import com.ridesharingappbackend.models.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(source = "ride.id", target = "rideId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "company.id", target = "companyId")
    PaymentDTO toDTO(Payment payment);

    @Mapping(target = "ride", source = "rideId", qualifiedByName = "mapRide")
    @Mapping(target = "user", source = "userId", qualifiedByName = "mapUser")
    @Mapping(target = "company", source = "companyId", qualifiedByName = "mapCompany")
    Payment toEntity(PaymentDTO dto);

    @Named("mapRide")
    default Ride mapRide(Integer id) {
        if (id == null) return null;
        Ride ride = new Ride();
        ride.setId(id);
        return ride;
    }

    @Named("mapUser")
    default User mapUser(Integer id) {
        if (id == null) return null;
        User user = new User();
        user.setId(id);
        return user;
    }

    @Named("mapCompany")
    default Company mapCompany(Integer id) {
        if (id == null) return null;
        Company company = new Company();
        company.setId(id);
        return company;
    }
}
