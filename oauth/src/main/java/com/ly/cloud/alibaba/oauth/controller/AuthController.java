package com.ly.cloud.alibaba.oauth.controller;

import com.ly.cloud.alibaba.common.base.annotation.CommonResponse;
import com.ly.cloud.alibaba.common.base.model.LoginVal;
import com.ly.cloud.alibaba.common.base.model.SysConstant;
import com.ly.cloud.alibaba.common.mvc.utils.OauthUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/oauth")
@Slf4j
public class AuthController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/logout")
    @CommonResponse
    public String logout() {
        LoginVal loginVal = OauthUtils.getCurrentUser();
        log.info("令牌唯一ID：{},过期时间：{}", loginVal.getJti(), loginVal.getExpireIn());
        //这个jti放入redis中，并且过期时间设置为token的过期时间
        stringRedisTemplate.opsForValue().set(SysConstant.JTI_KEY_PREFIX + loginVal.getJti(), "", loginVal.getExpireIn(), TimeUnit.SECONDS);
        return "注销成功";
    }
}
