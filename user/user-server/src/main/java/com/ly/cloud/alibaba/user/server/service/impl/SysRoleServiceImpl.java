package com.ly.cloud.alibaba.user.server.service.impl;

import com.ly.cloud.alibaba.common.util.SnowflakeGenerator;
import com.ly.cloud.alibaba.user.server.dao.SysPermissionRepository;
import com.ly.cloud.alibaba.user.server.dao.SysRolePermissionRepository;
import com.ly.cloud.alibaba.user.server.dao.SysRoleRepository;
import com.ly.cloud.alibaba.user.server.dao.SysUserRoleRepository;
import com.ly.cloud.alibaba.user.server.model.po.*;
import com.ly.cloud.alibaba.user.server.service.SysPermissionService;
import com.ly.cloud.alibaba.user.server.service.SysRoleService;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SysRoleServiceImpl implements SysRoleService {
    @Autowired
    RedissonClient redissonClient;

    @Autowired
    SysRoleRepository sysRoleRepository;

    @Autowired
    SysPermissionRepository sysPermissionRepository;

    @Autowired
    SysRolePermissionRepository sysRolePermissionRepository;

    @Autowired
    SysUserRoleRepository sysUserRoleRepository;

    @Autowired
    SysPermissionService sysPermissionService;


    @Override
    public SysRole addRole(SysRole sysRole) {
        sysRole.setId(SnowflakeGenerator.snowflakeId());
        sysRole.setCreateTime(LocalDateTime.now());
        sysRole.setUpdateTime(LocalDateTime.now());
        sysRole.setStatus(1);
        return sysRoleRepository.saveAndFlush(sysRole);
    }

    @Override
    public void bindPermissionByRole(Long roleId, List<SysPermission> sysPermissionList) {
        final Optional<SysRole> sysRoleOptional = sysRoleRepository.findById(roleId);
        if (!sysRoleOptional.isPresent()) {
            return ;
        }
        // 处理持久化数据
        sysRolePermissionRepository.deleteSysRolePermissionByRoleId(roleId);

        final List<SysRolePermission> sysRolePermissions = sysPermissionList.stream().map(sysPermission -> SysRolePermission.builder()
                .permissionId(sysPermission.getId())
                .roleId(roleId)
                .id(SnowflakeGenerator.snowflakeId())
                .build()).collect(Collectors.toList());
        sysRolePermissionRepository.saveAll(sysRolePermissions);

        // 刷新缓存
        sysPermissionService.flushByRoleCode(sysRoleOptional.get().getCode());
    }

    @Override
    public void bindUserByRole(Long roleId, List<SysUser> sysUsers) {
        sysUserRoleRepository.deleteAllByRoleId(roleId);

        final List<SysUserRole> sysUserRoles = sysUsers.stream().map(sysUser ->
                SysUserRole.builder()
                        .userId(sysUser.getId())
                        .roleId(roleId)
                        .id(SnowflakeGenerator.snowflakeId())
                        .build())
                .collect(Collectors.toList());
        sysUserRoleRepository.saveAll(sysUserRoles);
    }
}
