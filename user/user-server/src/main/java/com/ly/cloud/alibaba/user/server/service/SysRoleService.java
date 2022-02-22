package com.ly.cloud.alibaba.user.server.service;

import com.ly.cloud.alibaba.user.server.model.po.SysPermission;
import com.ly.cloud.alibaba.user.server.model.po.SysRole;
import com.ly.cloud.alibaba.user.server.model.po.SysUser;

import java.util.List;

public interface SysRoleService {
    SysRole addRole(SysRole sysRole);

    void bindPermissionByRole(Long roleId, List<SysPermission> sysPermissionList);

    void bindUserByRole(Long roleId, List<SysUser> sysUsers);
}
