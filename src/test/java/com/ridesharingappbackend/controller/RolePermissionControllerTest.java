package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.RolePermissionDTO;
import com.ridesharingappbackend.service.RolePermissionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RolePermissionControllerTest {

    @InjectMocks
    private RolePermissionController controller;

    @Mock
    private RolePermissionService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAll_returnsList() {
        RolePermissionDTO rp1 = new RolePermissionDTO(1, 1, 101);
        RolePermissionDTO rp2 = new RolePermissionDTO(2, 1, 104);

        when(service.getAll()).thenReturn(Arrays.asList(rp1, rp2));

        ResponseEntity<List<RolePermissionDTO>> response = controller.getAll();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(101, response.getBody().get(0).getPermissionId());

    }

    @Test
    void getByRoleId_returnsList() {
        RolePermissionDTO rp = new RolePermissionDTO(1, 1, 101);

        when(service.getByRoleId(1)).thenReturn(List.of(rp));

        ResponseEntity<List<RolePermissionDTO>> response = controller.getByRoleId(1);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(101, response.getBody().get(0).getPermissionId());

    }

    @Test
    void create_returnsCreated() {
        RolePermissionDTO input = new RolePermissionDTO(null, 1, 3);
        RolePermissionDTO created = new RolePermissionDTO(3, 1, 3);

        when(service.create(input)).thenReturn(created);

        ResponseEntity<RolePermissionDTO> response = controller.create(input);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().getId());
        assertEquals(3, response.getBody().getPermissionId());


    }

    @Test
    void delete_existingId_returnsNoContent() {
        when(service.delete(1)).thenReturn(1); // duhet të përdorësh .thenReturn sepse metoda kthen Integer

        ResponseEntity<Void> response = controller.delete(1);

        assertEquals(204, response.getStatusCodeValue());
        verify(service, times(1)).delete(1);
    }


    @Test
    void delete_nonExistingId_returnsNotFound() {
        doThrow(new RuntimeException()).when(service).delete(99);

        ResponseEntity<Void> response = controller.delete(99);

        assertEquals(404, response.getStatusCodeValue());
    }
}
