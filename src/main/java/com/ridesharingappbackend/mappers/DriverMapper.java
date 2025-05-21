package com.ridesharingappbackend.mappers;

import com.ridesharingappbackend.dto.DriverDTO;
import com.ridesharingappbackend.dto.UserDTO;
import com.ridesharingappbackend.models.Company;
import com.ridesharingappbackend.models.Driver;
import com.ridesharingappbackend.models.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface DriverMapper {

    @Mapping(source = "company.id", target = "companyId")
    DriverDTO toDTO(Driver driver);

    @Mapping(source = "user", target = "user")
    @Mapping(source = "companyId", target = "company", qualifiedByName = "mapCompany")
    Driver toEntity(DriverDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "user", target = "user")
    @Mapping(source = "companyId", target = "company", qualifiedByName = "mapCompany")
    void updateDriverFromDto(DriverDTO dto, @MappingTarget Driver driver);


}
