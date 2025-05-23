package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.RolePermissionDTO;
import com.ridesharingappbackend.mappers.RolePermissionMapper;
import com.ridesharingappbackend.models.Role;
import com.ridesharingappbackend.models.RolePermission;
import com.ridesharingappbackend.repository.RolePermissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RolePermissionServiceTest {

    @InjectMocks
    private RolePermissionService service;

    @Mock
    private RolePermissionRepository repository;

    @Mock
    private RolePermissionMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAll_ReturnsListOfDTOs() {
        // Arrange
        RolePermission rp1 = new RolePermission(); rp1.setId(1);
        RolePermission rp2 = new RolePermission(); rp2.setId(2);
        RolePermissionDTO dto1 = new RolePermissionDTO(); dto1.setId(1);
        RolePermissionDTO dto2 = new RolePermissionDTO(); dto2.setId(2);

        when(repository.findAll()).thenReturn(List.of(rp1, rp2));
        when(mapper.toDTO(rp1)).thenReturn(dto1);
        when(mapper.toDTO(rp2)).thenReturn(dto2);

        // Act
        List<RolePermissionDTO> result = service.getAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        verify(repository, times(1)).findAll();
    }

    @Test
    void getByRoleId_ReturnsDTOsForGivenRoleId() {
        // Arrange
        int roleId = 5;

        RolePermission rp1 = new RolePermission(); rp1.setId(1);
        RolePermission rp2 = new RolePermission(); rp2.setId(2);
        RolePermissionDTO dto1 = new RolePermissionDTO(); dto1.setId(1);
        RolePermissionDTO dto2 = new RolePermissionDTO(); dto2.setId(2);

        when(repository.findAllByRoleId(roleId)).thenReturn(List.of(rp1, rp2));
        when(mapper.toDTO(rp1)).thenReturn(dto1);
        when(mapper.toDTO(rp2)).thenReturn(dto2);

        // Act
        List<RolePermissionDTO> result = service.getByRoleId(roleId);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(dto -> dto.getId() == 1));
        verify(repository).findAllByRoleId(roleId);
    }

    @Test
    void create_SavesAndReturnsDTO() {
        // Arrange
        RolePermissionDTO inputDTO = new RolePermissionDTO();
        inputDTO.setRoleId(3);

        RolePermission entity = new RolePermission();
        RolePermission savedEntity = new RolePermission(); savedEntity.setId(10);
        RolePermissionDTO resultDTO = new RolePermissionDTO(); resultDTO.setId(10);

        when(mapper.toEntity(inputDTO)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(savedEntity);
        when(mapper.toDTO(savedEntity)).thenReturn(resultDTO);

        // Act
        RolePermissionDTO result = service.create(inputDTO);

        // Assert
        assertNotNull(result);
        assertEquals(10, result.getId());
        verify(repository).save(entity);
    }

    @Test
    void delete_ValidId_DeletesAndReturnsRoleId() {
        // Arrange
        int id = 99;
        Role role = new Role(); role.setId(7);
        RolePermission rp = new RolePermission(); rp.setId(id); rp.setRole(role);

        when(repository.findById(id)).thenReturn(Optional.of(rp));

        // Act
        Integer returnedRoleId = service.delete(id);

        // Assert
        assertEquals(7, returnedRoleId);
        verify(repository).deleteById(id);
    }

    @Test
    void delete_InvalidId_ThrowsException() {
        // Arrange
        when(repository.findById(123)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.delete(123));
        assertEquals("RolePermission not found", exception.getMessage());
        verify(repository, never()).deleteById(any());
    }
}
