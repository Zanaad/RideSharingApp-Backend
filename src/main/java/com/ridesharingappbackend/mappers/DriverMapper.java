package com.ridesharingappbackend.mappers;

import com.ridesharingappbackend.dto.DriverDTO;
import com.ridesharingappbackend.models.Company;
import com.ridesharingappbackend.models.Driver;
import com.ridesharingappbackend.models.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface DriverMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "company.id", target = "companyId")
    DriverDTO toDTO(Driver driver);

    @Mapping(target = "user", source = "userId", qualifiedByName = "mapUser")
    @Mapping(target = "company", source = "companyId", qualifiedByName = "mapCompany")
    Driver toEntity(DriverDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "user", source = "userId", qualifiedByName = "mapUser")
    @Mapping(target = "company", source = "companyId", qualifiedByName = "mapCompany")
    void updateDriverFromDto(DriverDTO dto, @MappingTarget Driver driver);

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
