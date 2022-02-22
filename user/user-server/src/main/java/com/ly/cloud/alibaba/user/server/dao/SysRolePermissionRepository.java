package com.ly.cloud.alibaba.user.server.dao;

import com.ly.cloud.alibaba.user.server.model.po.SysRolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysRolePermissionRepository extends JpaRepository<SysRolePermission, Long> {
    List<SysRolePermission> findByPermissionId(Long permissionId);

    List<SysRolePermission> findByRoleId(Long roleId);

    List<SysRolePermission> deleteSysRolePermissionByRoleId(Long roleId);
}
