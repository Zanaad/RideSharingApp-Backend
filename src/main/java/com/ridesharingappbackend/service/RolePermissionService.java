package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.RolePermissionDTO;
import com.ridesharingappbackend.mappers.RolePermissionMapper;
import com.ridesharingappbackend.models.RolePermission;
import com.ridesharingappbackend.repository.RolePermissionRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RolePermissionService {

    private final RolePermissionRepository repository;
    private final RolePermissionMapper mapper;

    public RolePermissionService(RolePermissionRepository repository, RolePermissionMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<RolePermissionDTO> getAll() {
        return repository.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public List<RolePermissionDTO> getByRoleId(Integer roleId) {
        return repository.findAllByRoleId(roleId).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }


    // Clear role-permissions cache for the role when a new permission is assigned
    @CacheEvict(value = "role-permissions", key = "#dto.roleId")
    public RolePermissionDTO create(RolePermissionDTO dto) {
        RolePermission entity = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(entity));
    }

    // Evict cache for the role when a permission is removed
    @CacheEvict(value = "role-permissions", key = "#result")
    public Integer  delete(Integer id) {
        RolePermission rp = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("RolePermission not found"));
        Integer roleId = rp.getRole().getId();
        repository.deleteById(id);
        return roleId;
    }
}
