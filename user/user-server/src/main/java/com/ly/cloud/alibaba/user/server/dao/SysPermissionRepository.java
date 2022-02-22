package com.ly.cloud.alibaba.user.server.dao;

import com.ly.cloud.alibaba.user.server.model.po.SysPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysPermissionRepository extends JpaRepository<SysPermission, Long> {
}
