package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.CachedPermissionDTO;
import com.ridesharingappbackend.dto.PermissionDTO;
import com.ridesharingappbackend.mappers.PermissionMapper;
import com.ridesharingappbackend.models.Permission;
import com.ridesharingappbackend.models.Role;
import com.ridesharingappbackend.models.RolePermission;
import com.ridesharingappbackend.repository.PermissionRepository;
import com.ridesharingappbackend.repository.RolePermissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PermissionServiceTest {

    @InjectMocks
    private PermissionService permissionService;

    @Mock
    private RolePermissionRepository rolePermissionRepository;

    @Mock
    private PermissionRepository permissionRepository;

    @Mock
    private PermissionMapper permissionMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getPermissionsByRole_ReturnsMappedCachedPermissions() {
        Integer roleId = 1;
        RolePermission rolePermission = new RolePermission();
        List<RolePermission> mockList = List.of(rolePermission);
        CachedPermissionDTO dto = new CachedPermissionDTO();
        when(rolePermissionRepository.findAllByRoleId(roleId)).thenReturn(mockList);
        when(permissionMapper.toDTO(rolePermission)).thenReturn(dto);

        List<CachedPermissionDTO> result = permissionService.getPermissionsByRole(roleId);

        assertEquals(1, result.size());
        verify(rolePermissionRepository).findAllByRoleId(roleId);
        verify(permissionMapper).toDTO(rolePermission);
    }

    @Test
    void getAllPermissions_ReturnsMappedPermissionDTOs() {
        Permission permission = new Permission();
        List<Permission> permissions = List.of(permission);
        PermissionDTO dto = new PermissionDTO();
        when(permissionRepository.findAll()).thenReturn(permissions);
        when(permissionMapper.toDTO(permission)).thenReturn(dto);

        List<PermissionDTO> result = permissionService.getAllPermissions();

        assertEquals(1, result.size());
        verify(permissionRepository).findAll();
        verify(permissionMapper).toDTO(permission);
    }

    @Test
    void getPermissionById_Exists_ReturnsDTO() {
        int id = 10;
        Permission permission = new Permission();
        PermissionDTO dto = new PermissionDTO();
        when(permissionRepository.findById(id)).thenReturn(Optional.of(permission));
        when(permissionMapper.toDTO(permission)).thenReturn(dto);

        PermissionDTO result = permissionService.getPermissionById(id);

        assertNotNull(result);
        verify(permissionRepository).findById(id);
        verify(permissionMapper).toDTO(permission);
    }

    @Test
    void getPermissionById_NotFound_ThrowsException() {
        int id = 10;
        when(permissionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> permissionService.getPermissionById(id));
    }

    @Test
    void createPermission_MapsAndSaves_ReturnsDTO() {
        PermissionDTO dto = new PermissionDTO();
        Permission permission = new Permission();
        when(permissionMapper.toEntity(dto)).thenReturn(permission);
        when(permissionRepository.save(permission)).thenReturn(permission);
        when(permissionMapper.toDTO(permission)).thenReturn(dto);

        PermissionDTO result = permissionService.createPermission(dto);

        assertEquals(dto, result);
        verify(permissionRepository).save(permission);
    }

    @Test
    void updatePermission_ValidId_UpdatesAndReturnsDTO() {
        int id = 5;
        PermissionDTO dto = new PermissionDTO();
        dto.setName("read");
        dto.setVerb(Permission.HttpVerb.GET);
        dto.setResource("resource");

        Permission permission = new Permission();
        when(permissionRepository.findById(id)).thenReturn(Optional.of(permission));
        when(permissionRepository.save(permission)).thenReturn(permission);
        when(permissionMapper.toDTO(permission)).thenReturn(dto);

        PermissionDTO result = permissionService.updatePermission(id, dto);

        assertEquals(dto, result);
        assertEquals("read", permission.getName());
        assertEquals(Permission.HttpVerb.GET, result.getVerb());
        assertEquals("resource", permission.getResource());
    }

    @Test
    void updatePermission_NotFound_ThrowsException() {
        int id = 100;
        PermissionDTO dto = new PermissionDTO();
        when(permissionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> permissionService.updatePermission(id, dto));
    }

    @Test
    void deletePermission_ValidId_EvictsAndDeletes() {
        int id = 10;
        Permission permission = new Permission();
        permission.setId(id);

        Role role = new Role();
        role.setId(99);
        RolePermission rp = new RolePermission();
        rp.setRole(role);

        when(permissionRepository.findById(id)).thenReturn(Optional.of(permission));
        when(rolePermissionRepository.findAllByPermissionId(id)).thenReturn(List.of(rp));

        // Use spy to verify evictPermissionsCache call
        PermissionService spyService = Mockito.spy(permissionService);
        doNothing().when(spyService).evictPermissionsCache(99);
        spyService.deletePermission(id);

        verify(spyService).evictPermissionsCache(99);
        verify(permissionRepository).delete(permission);
    }

    @Test
    void deletePermission_NotFound_ThrowsException() {
        int id = 10;
        when(permissionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> permissionService.deletePermission(id));
    }

    @Test
    void evictPermissionsCache_ShouldNotThrow() {
        assertDoesNotThrow(() -> permissionService.evictPermissionsCache(42));
    }
}
