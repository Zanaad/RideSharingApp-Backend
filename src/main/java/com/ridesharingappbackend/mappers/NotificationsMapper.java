package com.ridesharingappbackend.mappers;

import com.ridesharingappbackend.dto.NotificationsDTO;
import com.ridesharingappbackend.models.Company;
import com.ridesharingappbackend.models.Notifications;
import com.ridesharingappbackend.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface NotificationsMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "type", target = "notificationType")
    NotificationsDTO toDTO(Notifications notification);

    @Mapping(target = "user", source = "userId", qualifiedByName = "mapUser")
    @Mapping(target = "company", source = "companyId", qualifiedByName = "mapCompany")
    @Mapping(target = "type", source = "notificationType")
    Notifications toEntity(NotificationsDTO dto);

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
