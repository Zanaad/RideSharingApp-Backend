package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.RoleDTO;
import com.ridesharingappbackend.mappers.RoleMapper;
import com.ridesharingappbackend.models.Role;
import com.ridesharingappbackend.repository.RoleRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleService(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }


    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(roleMapper::toDTO)
                .collect(Collectors.toList());
    }


    public RoleDTO getRoleById(Integer id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        return roleMapper.toDTO(role);
    }

    public RoleDTO createRole(RoleDTO roleDTO) {
        Role role = new Role();
        role.setName(roleDTO.getName());
        return roleMapper.toDTO(roleRepository.save(role));
    }


    public RoleDTO updateRole(Integer id, RoleDTO updatedRoleDTO) {
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        existingRole.setName(updatedRoleDTO.getName());
        return roleMapper.toDTO(roleRepository.save(existingRole));
    }

    @CacheEvict(value = "role-permissions", key = "#id")
    public void deleteRole(Integer id) {
        if (!roleRepository.existsById(id)) {
            throw new RuntimeException("Role not found");
        }
        roleRepository.deleteById(id);
    }
}
