package com.ly.cloud.alibaba.user.server.service;

import com.ly.cloud.alibaba.user.api.model.dto.PermissionDTO;
import com.ly.cloud.alibaba.user.server.model.po.SysPermission;

import java.util.List;

public interface SysPermissionService {
    SysPermission addPermission(SysPermission sysPermission);

    List<PermissionDTO> listsPermissionDTOByRoleCode(String roleCode);

    List<SysPermission> flushByRoleCode(String roleCode);
}
