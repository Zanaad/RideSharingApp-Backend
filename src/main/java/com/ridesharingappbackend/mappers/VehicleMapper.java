package com.ridesharingappbackend.mappers;

import com.ridesharingappbackend.dto.VehicleDTO;
import com.ridesharingappbackend.models.Company;
import com.ridesharingappbackend.models.Driver;
import com.ridesharingappbackend.models.Vehicle;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    @Mapping(source = "driver.id", target = "driverId")
    @Mapping(source = "company.id", target = "companyId")
    VehicleDTO toDTO(Vehicle vehicle);

    @Mapping(target = "driver", source = "driverId", qualifiedByName = "mapDriver")
    @Mapping(target = "company", source = "companyId", qualifiedByName = "mapCompany")
    Vehicle toEntity(VehicleDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "driver", source = "driverId", qualifiedByName = "mapDriver")
    @Mapping(target = "company", source = "companyId", qualifiedByName = "mapCompany")
    void updateVehicleFromDto(VehicleDTO dto, @MappingTarget Vehicle vehicle);

    @Named("mapCompany")
    default Company mapCompany(Integer companyId) {
        if (companyId == null) return null;
        Company company = new Company();
        company.setId(companyId);
        return company;
    }

    @Named("mapDriver")
    default Driver mapDriver(Integer driverId) {
        if (driverId == null) return null;
        Driver driver = new Driver();
        driver.setId(driverId);
        return driver;
    }
}
