package com.ridesharingappbackend.dto;

import com.ridesharingappbackend.models.Permission.HttpVerb;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PermissionDTO {
    private Integer id;
    private String name;
    private HttpVerb verb;
    private String resource;
}
