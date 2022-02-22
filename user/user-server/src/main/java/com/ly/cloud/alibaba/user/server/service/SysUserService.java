package com.ly.cloud.alibaba.user.server.service;

import com.ly.cloud.alibaba.user.api.model.dto.UserDTO;
import com.ly.cloud.alibaba.user.server.model.po.SysUser;

public interface SysUserService {
    UserDTO getByUsername(String username);

    SysUser addUser(SysUser sysUser);
}
