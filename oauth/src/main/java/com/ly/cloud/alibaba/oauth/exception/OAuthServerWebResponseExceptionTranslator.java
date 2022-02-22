package com.ly.cloud.alibaba.oauth.exception;

import com.ly.cloud.alibaba.common.base.enums.SystemErrorCoeEnum;
import com.ly.cloud.alibaba.common.base.exception.ApiException;
import com.ly.cloud.alibaba.common.base.model.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.UnsupportedGrantTypeException;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;

/**
 * oauth异常处理
 */
public class OAuthServerWebResponseExceptionTranslator implements WebResponseExceptionTranslator {
    /**
     * 业务处理方法，重写这个方法返回客户端信息
     */
    @Override
    public ResponseEntity<APIResponse<String>> translate(Exception e) {
        APIResponse<String> resultMsg = doTranslateHandler(e);
        return new ResponseEntity<>(resultMsg, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 根据异常定制返回信息
     * TODO 自己根据业务封装
     */
    private APIResponse<String> doTranslateHandler(Exception e) {
        // 初始值，系统错误，
        SystemErrorCoeEnum systemErrorCoeEnum = SystemErrorCoeEnum.UNAUTHORIZED;
        // 判断异常，不支持的认证方式
        if (e instanceof UnsupportedGrantTypeException) {
            systemErrorCoeEnum = SystemErrorCoeEnum.UN_SUPPORTED_GRANT_TYPE_EXCEPTION;
            //用户名或密码异常
        } else if (e instanceof InvalidGrantException) {
            systemErrorCoeEnum = SystemErrorCoeEnum.USERNAME_OR_PASSWORD_ERROR;
        } else if (e instanceof InternalAuthenticationServiceException) {
            if (e.getCause() instanceof ApiException) {
                ApiException apiException = (ApiException) e.getCause();
                return new APIResponse<String>(apiException.getCode(), apiException.getMsg(), null);
            } else {
                systemErrorCoeEnum = SystemErrorCoeEnum.INTERNAL_AUTHENTICATION_SERVICE_EXCEPTION;
            }
        }
        return new APIResponse<String>(systemErrorCoeEnum.getCode(), systemErrorCoeEnum.getMsg(), null);
    }
}
