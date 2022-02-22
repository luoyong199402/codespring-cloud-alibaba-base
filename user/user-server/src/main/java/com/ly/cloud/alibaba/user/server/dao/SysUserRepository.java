package com.ly.cloud.alibaba.user.server.dao;

import com.ly.cloud.alibaba.user.server.model.po.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysUserRepository extends JpaRepository<SysUser, Long> {
    SysUser findByUsername(String username);
}
