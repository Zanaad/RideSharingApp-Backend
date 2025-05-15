package com.ridesharingappbackend.mappers;

import com.ridesharingappbackend.dto.SignupRequest;
import com.ridesharingappbackend.dto.UserDTO;
import com.ridesharingappbackend.models.Company;
import com.ridesharingappbackend.models.Role;
import com.ridesharingappbackend.models.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "role.id", target = "roleId")
    UserDTO toDTO(User user);

    @Mapping(target = "company", source = "companyId", qualifiedByName = "mapCompany")
    @Mapping(target = "role", source = "roleId", qualifiedByName = "mapRole")
    User toEntity(UserDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "company", source = "companyId", qualifiedByName = "mapCompany")
    @Mapping(target = "role", source = "roleId", qualifiedByName = "mapRole")
    void updateUserFromDto(UserDTO dto, @MappingTarget User entity);

    @Named("mapCompany")
    default Company mapCompany(Integer companyId) {
        if (companyId == null) return null;
        Company company = new Company();
        company.setId(companyId);
        return company;
    }

    @Named("mapRole")
    default Role mapRole(String roleId) {
        if (roleId == null) return null;
        Role role = new Role();
        role.setId(Integer.valueOf(roleId));
        return role;
    }

    @Mapping(target = "company", source = "companyId", qualifiedByName = "mapCompany")
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    User fromSignupRequest(SignupRequest request);
}
