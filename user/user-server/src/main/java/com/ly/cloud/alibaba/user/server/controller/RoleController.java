package com.ly.cloud.alibaba.user.server.controller;

import com.ly.cloud.alibaba.user.server.model.po.SysPermission;
import com.ly.cloud.alibaba.user.server.model.po.SysRole;
import com.ly.cloud.alibaba.user.server.model.po.SysUser;
import com.ly.cloud.alibaba.user.server.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    SysRoleService sysRoleService;

    @PostMapping
    public SysRole addRole(@RequestBody SysRole sysRole) {
        return sysRoleService.addRole(sysRole);
    }

    @PostMapping("/{roleId}/permission")
    public SysRole bindPermissionByRole(@PathVariable Long roleId, @RequestBody List<SysPermission> sysPermissionList) {
        sysRoleService.bindPermissionByRole(roleId, sysPermissionList);
        return null;
    }

    @PostMapping("/{roleId}/user")
    public SysRole bindUserByRole(@PathVariable Long roleId, @RequestBody List<SysUser> sysUsers) {
        sysRoleService.bindUserByRole(roleId, sysUsers);
        return null;
    }
}
