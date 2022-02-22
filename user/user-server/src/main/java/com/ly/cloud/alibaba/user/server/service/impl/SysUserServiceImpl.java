package com.ly.cloud.alibaba.user.server.service.impl;

import com.ly.cloud.alibaba.common.base.enums.ErrorCode;
import com.ly.cloud.alibaba.common.base.enums.SystemErrorCoeEnum;
import com.ly.cloud.alibaba.common.base.exception.ApiException;
import com.ly.cloud.alibaba.common.util.SnowflakeGenerator;
import com.ly.cloud.alibaba.user.api.model.dto.UserDTO;
import com.ly.cloud.alibaba.user.server.dao.SysRoleRepository;
import com.ly.cloud.alibaba.user.server.dao.SysUserRepository;
import com.ly.cloud.alibaba.user.server.dao.SysUserRoleRepository;
import com.ly.cloud.alibaba.user.server.model.po.SysRole;
import com.ly.cloud.alibaba.user.server.model.po.SysUser;
import com.ly.cloud.alibaba.user.server.model.po.SysUserRole;
import com.ly.cloud.alibaba.user.server.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    SysUserRepository sysUserRepository;

    @Autowired
    SysUserRoleRepository sysUserRoleRepository;

    @Autowired
    SysRoleRepository sysRoleRepository;

    @Override
    public UserDTO getByUsername(String username) {
        final SysUser sysUser = sysUserRepository.findByUsername(username);
        if (sysUser == null) {
            return null;
        }
        final List<SysUserRole> sysUserRoles = sysUserRoleRepository.findByUserId(sysUser.getId());
        final List<SysRole> sysRoles = sysRoleRepository.findAllById(sysUserRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList()));
        return UserDTO.builder()
                .id(sysUser.getId())
                .username(sysUser.getUsername())
                .password(sysUser.getPassword())
                .roleList(sysRoles.stream().map(SysRole::getCode).collect(Collectors.toList()))
                .build();
    }

    @Override
    public SysUser addUser(SysUser sysUser) {
        final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        final SysUser querySysUser = sysUserRepository.findByUsername(sysUser.getUsername());
//        if (querySysUser != null) {
//            throw new ApiException("code-001", "用户已存在", null);
//        }

        sysUser.setId(SnowflakeGenerator.snowflakeId());
        sysUser.setCreateTime(LocalDateTime.now());
        sysUser.setUpdateTime(LocalDateTime.now());
        sysUser.setStatus(1);
        sysUser.setPassword(bCryptPasswordEncoder.encode(sysUser.getPassword()));
        return sysUserRepository.save(sysUser);
    }
}
