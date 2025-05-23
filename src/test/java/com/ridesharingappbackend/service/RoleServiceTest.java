package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.RoleDTO;
import com.ridesharingappbackend.mappers.RoleMapper;
import com.ridesharingappbackend.models.Role;
import com.ridesharingappbackend.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServiceTest {

    @InjectMocks
    private RoleService roleService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleMapper roleMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllRoles_ReturnsListOfRoleDTOs() {
        // Arrange
        Role role1 = new Role(); role1.setId(1); role1.setName("Admin");
        Role role2 = new Role(); role2.setId(2); role2.setName("Driver");

        RoleDTO dto1 = new RoleDTO(); dto1.setId(1); dto1.setName("Admin");
        RoleDTO dto2 = new RoleDTO(); dto2.setId(2); dto2.setName("Driver");

        List<Role> roles = Arrays.asList(role1, role2);
        when(roleRepository.findAll()).thenReturn(roles);
        when(roleMapper.toDTO(role1)).thenReturn(dto1);
        when(roleMapper.toDTO(role2)).thenReturn(dto2);

        // Act
        List<RoleDTO> result = roleService.getAllRoles();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Admin", result.get(0).getName());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void getRoleById_ValidId_ReturnsRoleDTO() {
        // Arrange
        Role role = new Role(); role.setId(1); role.setName("Passenger");
        RoleDTO dto = new RoleDTO(); dto.setId(1); dto.setName("Passenger");

        when(roleRepository.findById(1)).thenReturn(Optional.of(role));
        when(roleMapper.toDTO(role)).thenReturn(dto);

        // Act
        RoleDTO result = roleService.getRoleById(1);

        // Assert
        assertNotNull(result);
        assertEquals("Passenger", result.getName());
    }

    @Test
    void getRoleById_InvalidId_ThrowsException() {
        // Arrange
        when(roleRepository.findById(99)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> roleService.getRoleById(99));
        assertEquals("Role not found", exception.getMessage());
    }

    @Test
    void createRole_SavesAndReturnsRoleDTO() {
        // Arrange
        RoleDTO inputDTO = new RoleDTO(); inputDTO.setName("Manager");
        Role savedRole = new Role(); savedRole.setId(3); savedRole.setName("Manager");
        RoleDTO resultDTO = new RoleDTO(); resultDTO.setId(3); resultDTO.setName("Manager");

        when(roleRepository.save(any(Role.class))).thenReturn(savedRole);
        when(roleMapper.toDTO(savedRole)).thenReturn(resultDTO);

        // Act
        RoleDTO result = roleService.createRole(inputDTO);

        // Assert
        assertEquals("Manager", result.getName());
        assertEquals(3, result.getId());
    }

    @Test
    void updateRole_ValidId_UpdatesAndReturnsDTO() {
        // Arrange
        Role existingRole = new Role(); existingRole.setId(1); existingRole.setName("Old");
        RoleDTO updatedDTO = new RoleDTO(); updatedDTO.setName("New");

        Role savedRole = new Role(); savedRole.setId(1); savedRole.setName("New");
        RoleDTO resultDTO = new RoleDTO(); resultDTO.setId(1); resultDTO.setName("New");

        when(roleRepository.findById(1)).thenReturn(Optional.of(existingRole));
        when(roleRepository.save(existingRole)).thenReturn(savedRole);
        when(roleMapper.toDTO(savedRole)).thenReturn(resultDTO);

        // Act
        RoleDTO result = roleService.updateRole(1, updatedDTO);

        // Assert
        assertEquals("New", result.getName());
    }

    @Test
    void updateRole_InvalidId_ThrowsException() {
        // Arrange
        when(roleRepository.findById(99)).thenReturn(Optional.empty());
        RoleDTO updatedDTO = new RoleDTO(); updatedDTO.setName("New");

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> roleService.updateRole(99, updatedDTO));
        assertEquals("Role not found", exception.getMessage());
    }

    @Test
    void deleteRole_ValidId_DeletesSuccessfully() {
        // Arrange
        when(roleRepository.existsById(1)).thenReturn(true);

        // Act
        roleService.deleteRole(1);

        // Assert
        verify(roleRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteRole_InvalidId_ThrowsException() {
        // Arrange
        when(roleRepository.existsById(99)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> roleService.deleteRole(99));
        assertEquals("Role not found", exception.getMessage());
    }
}
