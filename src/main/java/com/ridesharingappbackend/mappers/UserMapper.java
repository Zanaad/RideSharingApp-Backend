package com.ridesharingappbackend.mappers;

import com.ridesharingappbackend.dto.UserDTO;
import com.ridesharingappbackend.models.Company;
import com.ridesharingappbackend.models.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {


    @Mapping(source = "company.id", target = "companyId")
    UserDTO toDTO(User user);


    @Mapping(target = "company", source = "companyId", qualifiedByName = "mapCompany")
    User toEntity(UserDTO dto);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "company", source = "companyId", qualifiedByName = "mapCompany")
    void updateUserFromDto(UserDTO dto, @MappingTarget User entity);


    @Named("mapCompany")
    default Company mapCompany(Integer companyId) {
        if (companyId == null) return null;
        Company company = new Company();
        company.setId(companyId);
        return company;
    }
}
