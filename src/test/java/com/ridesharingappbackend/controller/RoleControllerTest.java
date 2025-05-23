package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.RoleDTO;
import com.ridesharingappbackend.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleControllerTest {

    @InjectMocks
    private RoleController roleController;

    @Mock
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllRoles_returnsList() {
        RoleDTO role1 = new RoleDTO(1, "Admin");
        RoleDTO role2 = new RoleDTO(2, "User");

        when(roleService.getAllRoles()).thenReturn(Arrays.asList(role1, role2));

        ResponseEntity<List<RoleDTO>> response = roleController.getAllRoles();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Admin", response.getBody().get(0).getName());
    }

    @Test
    void getRoleById_found() {
        RoleDTO role = new RoleDTO(1, "Admin");

        when(roleService.getRoleById(1)).thenReturn(role);

        ResponseEntity<RoleDTO> response = roleController.getRoleById(1);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Admin", response.getBody().getName());
    }

    @Test
    void getRoleById_notFound() {
        when(roleService.getRoleById(99)).thenThrow(new RuntimeException());

        ResponseEntity<RoleDTO> response = roleController.getRoleById(99);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void createRole_success() {
        RoleDTO input = new RoleDTO(null, "Manager");
        RoleDTO created = new RoleDTO(3, "Manager");

        when(roleService.createRole(input)).thenReturn(created);

        ResponseEntity<RoleDTO> response = roleController.createRole(input);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().getId());
        assertEquals("Manager", response.getBody().getName());
    }

    @Test
    void updateRole_success() {
        RoleDTO updateInfo = new RoleDTO(null, "SuperAdmin");
        RoleDTO updated = new RoleDTO(1, "SuperAdmin");

        when(roleService.updateRole(1, updateInfo)).thenReturn(updated);

        ResponseEntity<RoleDTO> response = roleController.updateRole(1, updateInfo);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("SuperAdmin", response.getBody().getName());
    }

    @Test
    void deleteRole_success() {
        doNothing().when(roleService).deleteRole(1);

        ResponseEntity<Void> response = roleController.deleteRole(1);

        assertEquals(204, response.getStatusCodeValue());
        verify(roleService, times(1)).deleteRole(1);
    }

    @Test
    void deleteRole_notFound() {
        doThrow(new RuntimeException()).when(roleService).deleteRole(99);

        ResponseEntity<Void> response = roleController.deleteRole(99);

        assertEquals(404, response.getStatusCodeValue());
    }
}
