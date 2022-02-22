package com.ly.cloud.alibaba.user.server.controller;

import com.ly.cloud.alibaba.common.base.annotation.InternalApi;
import com.ly.cloud.alibaba.user.api.model.dto.PermissionDTO;
import com.ly.cloud.alibaba.user.server.model.po.SysPermission;
import com.ly.cloud.alibaba.user.server.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
public class PermissionController {
    @Autowired
    SysPermissionService sysPermissionService;

    @GetMapping("/role-code/{roleCode}")
    @InternalApi
    public List<PermissionDTO> listsPermissionDTOByRoleCode(@PathVariable("roleCode") String roleCode) {
        return sysPermissionService.listsPermissionDTOByRoleCode(roleCode);
    }

    @PostMapping
    public SysPermission addRole(@RequestBody SysPermission sysPermission) {
        return sysPermissionService.addPermission(sysPermission);
    }
}
