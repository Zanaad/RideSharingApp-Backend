package com.ridesharingappbackend.repository;

import com.ridesharingappbackend.models.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Integer> {
    List<RolePermission> findAllByRoleId(Integer roleId);
    List<RolePermission> findAllByPermissionId(Integer permissionId);
}
