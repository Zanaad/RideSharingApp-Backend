package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.PermissionDTO;
import com.ridesharingappbackend.service.PermissionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import com.ridesharingappbackend.models.Permission.HttpVerb;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PermissionControllerTest {

    @InjectMocks
    private PermissionController permissionController;

    @Mock
    private PermissionService permissionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private PermissionDTO createSamplePermission() {
        return new PermissionDTO(1, "VIEW_RIDES", HttpVerb.GET, "/api/rides");
    }


    @Test
    void getAllPermissions_success() {
        List<PermissionDTO> permissions = List.of(createSamplePermission());
        when(permissionService.getAllPermissions()).thenReturn(permissions);

        ResponseEntity<List<PermissionDTO>> response = permissionController.getAllPermissions();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("VIEW_RIDES", response.getBody().get(0).getName());
    }

    @Test
    void getPermissionById_found() {
        PermissionDTO permission = createSamplePermission();
        when(permissionService.getPermissionById(1)).thenReturn(permission);

        ResponseEntity<PermissionDTO> response = permissionController.getPermissionById(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(permission, response.getBody());
    }

    @Test
    void getPermissionById_notFound() {
        when(permissionService.getPermissionById(999)).thenThrow(new RuntimeException("Not found"));

        ResponseEntity<PermissionDTO> response = permissionController.getPermissionById(999);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void createPermission_success() {
        PermissionDTO input = createSamplePermission();
        when(permissionService.createPermission(input)).thenReturn(input);

        ResponseEntity<PermissionDTO> response = permissionController.createPermission(input);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(input, response.getBody());
    }

    @Test
    void updatePermission_success() {
        PermissionDTO updated = new PermissionDTO(1, "EDIT_RIDES", HttpVerb.PUT, "/api/rides/edit");

        when(permissionService.updatePermission(1, updated)).thenReturn(updated);

        ResponseEntity<PermissionDTO> response = permissionController.updatePermission(1, updated);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("EDIT_RIDES", response.getBody().getName());
    }

    @Test
    void deletePermission_success() {
        doNothing().when(permissionService).deletePermission(1);

        ResponseEntity<Void> response = permissionController.deletePermission(1);

        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void deletePermission_notFound() {
        doThrow(new RuntimeException("Not found")).when(permissionService).deletePermission(999);

        ResponseEntity<Void> response = permissionController.deletePermission(999);

        assertEquals(404, response.getStatusCodeValue());
    }
}
