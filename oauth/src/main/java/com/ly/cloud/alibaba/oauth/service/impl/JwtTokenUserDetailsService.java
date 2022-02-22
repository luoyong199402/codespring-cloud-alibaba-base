package com.ly.cloud.alibaba.oauth.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.alibaba.nacos.api.utils.StringUtils;
import com.ly.cloud.alibaba.oauth.model.SecurityUser;
import com.ly.cloud.alibaba.user.api.feign.UserFeignClient;
import com.ly.cloud.alibaba.user.api.model.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * UserDetailsService的实现类，从数据库加载用户的信息，比如密码、角色。。。。
 */
@Service
@Slf4j
public class JwtTokenUserDetailsService implements UserDetailsService {
    @Autowired
    UserFeignClient userFeignClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final UserDTO userDTO = userFeignClient.getByUsername(username);
        if (userDTO == null || StringUtils.isEmpty(username)) {
            throw new UsernameNotFoundException("username not found");
        }
        return SecurityUser.builder()
                .userId(String.valueOf(userDTO.getId()))
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .authorities(AuthorityUtils.createAuthorityList(ArrayUtil.toArray(userDTO.getRoleList(), String.class)))
                .build();
    }
}

