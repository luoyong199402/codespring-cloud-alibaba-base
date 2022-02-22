package com.ly.cloud.alibaba.user.server.controller;

import com.ly.cloud.alibaba.common.base.annotation.CommonResponse;
import com.ly.cloud.alibaba.common.base.annotation.InternalApi;
import com.ly.cloud.alibaba.user.api.model.dto.UserDTO;
import com.ly.cloud.alibaba.user.server.model.po.SysUser;
import com.ly.cloud.alibaba.user.server.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    SysUserService sysUserService;

    @GetMapping("/username/{username}")
    @InternalApi
    public UserDTO getByUsername(@PathVariable String username) {
        return sysUserService.getByUsername(username);
    }

    @PostMapping
    @CommonResponse
    public SysUser addUser(@RequestBody SysUser sysUser) {
        return sysUserService.addUser(sysUser);
    }
}
