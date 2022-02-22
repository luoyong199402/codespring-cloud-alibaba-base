package com.ly.cloud.alibaba.user.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.ly.cloud.alibaba.common.util.SnowflakeGenerator;
import com.ly.cloud.alibaba.user.api.model.dto.PermissionDTO;
import com.ly.cloud.alibaba.user.server.dao.SysPermissionRepository;
import com.ly.cloud.alibaba.user.server.dao.SysRolePermissionRepository;
import com.ly.cloud.alibaba.user.server.dao.SysRoleRepository;
import com.ly.cloud.alibaba.user.server.model.SysConstant;
import com.ly.cloud.alibaba.user.server.model.po.SysPermission;
import com.ly.cloud.alibaba.user.server.model.po.SysRole;
import com.ly.cloud.alibaba.user.server.model.po.SysRolePermission;
import com.ly.cloud.alibaba.user.server.service.SysPermissionService;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class SysPermissionServiceImpl implements SysPermissionService {
    @Autowired
    RedissonClient redissonClient;

    @Autowired
    SysPermissionRepository sysPermissionRepository;

    @Autowired
    SysRolePermissionRepository sysRolePermissionRepository;

    @Autowired
    SysRoleRepository sysRoleRepository;

    @Override
    public SysPermission addPermission(SysPermission sysPermission) {
        sysPermission.setId(SnowflakeGenerator.snowflakeId());
        sysPermission.setCreateTime(LocalDateTime.now());
        sysPermission.setUpdateTime(LocalDateTime.now());
        return sysPermissionRepository.saveAndFlush(sysPermission);
    }

    @Override
    public List<PermissionDTO> listsPermissionDTOByRoleCode(String roleCode) {
        List<SysPermission> sysPermissions;
        final RList<SysPermission> rList = redissonClient.getList(SysConstant.USER_PERMISSION_BY_ROLE_CODE_KEY_PREFIX + roleCode);
        sysPermissions = rList.readAll();
        if (CollectionUtil.isEmpty(rList)) {
            sysPermissions = flushByRoleCode(roleCode);
        }

        return sysPermissions.stream().map(sysPermission -> {
            PermissionDTO permissionDTO = new PermissionDTO();
            BeanUtil.copyProperties(sysPermission, permissionDTO);
            return permissionDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<SysPermission> flushByRoleCode(String roleCode) {
        final Optional<SysRole> sysRoleOptional = sysRoleRepository.getByCode(roleCode);
        if (!sysRoleOptional.isPresent()) {
            return null;
        }
        //  处理缓存信息
        final RList<Object> rList = redissonClient.getList(SysConstant.USER_PERMISSION_BY_ROLE_CODE_KEY_PREFIX + roleCode);
        rList.expire(30, TimeUnit.MINUTES);


        final List<SysRolePermission> sysRolePermissions = sysRolePermissionRepository.findByRoleId(sysRoleOptional.get().getId());

        final List<SysPermission> sysPermissions = sysPermissionRepository.findAllById(
                sysRolePermissions.stream().map(SysRolePermission::getPermissionId).collect(Collectors.toList()));
        if (rList.isExists()) {
            rList.clear();
        }
        rList.addAll(sysPermissions);
        return sysPermissions;
    }
}
