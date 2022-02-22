package com.ly.cloud.alibaba.gateway.config.security;

import cn.hutool.core.util.StrUtil;
import com.ly.cloud.alibaba.gateway.model.SysConstant;
import com.ly.cloud.alibaba.user.api.feign.PermissionFeignClient;
import com.ly.cloud.alibaba.user.api.model.dto.PermissionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

/**
 * 鉴权管理器 用于认证成功之后对用户的权限进行鉴权
 */
@Slf4j
@Component
public class JwtAccessManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    @Autowired
    PermissionFeignClient feignClient;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
        //匹配url
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        //从Redis中获取当前路径可访问角色列表
        URI uri = authorizationContext.getExchange().getRequest().getURI();
        //请求方法 POST,GET
        String method = authorizationContext.getExchange().getRequest().getMethodValue();
        /**
         * TODO 为了适配restful接口，比如 GET:/api/.... POST:/api/....  *:/api/.....  星号匹配所有
         */
        String restFulPath = method + SysConstant.METHOD_SUFFIX + uri.getPath();
        //认证通过且角色匹配的用户可访问当前路径
        return mono
                //判断是否认证成功
                .filter(Authentication::isAuthenticated)
                //获取认证后的全部权限
                .flatMapIterable(Authentication::getAuthorities)
                .map(GrantedAuthority::getAuthority)
                //如果权限包含则判断为true
                .any(authority -> {
                    // 超级管理员直接放行
                    if (StrUtil.equals(SysConstant.ROLE_ROOT_CODE, authority)) {
                        return true;
                    }
                    // 退出接口不需要权限。 但需要token。
                    if (StrUtil.equals(restFulPath, "POST:/gateway-oauth/oauth/logout")) {
                        return true;
                    }
                    final List<PermissionDTO> permissionDTOS = feignClient.listByRoleCode(authority);
                    final boolean isAnyMatch = permissionDTOS.stream().anyMatch(permissionDTO -> {
                        if (antPathMatcher.match(permissionDTO.getUrl(), restFulPath)) {
                            return true;
                        }
                        return false;
                    });
                    return isAnyMatch;
                })
                .map(AuthorizationDecision::new)
                .defaultIfEmpty(new AuthorizationDecision(false));
    }

}