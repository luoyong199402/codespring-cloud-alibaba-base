package com.ly.cloud.alibaba.user.server.dao;

import com.ly.cloud.alibaba.user.server.model.po.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SysRoleRepository extends JpaRepository<SysRole, Long> {
    Optional<SysRole> getByCode(String roleCode);
}
