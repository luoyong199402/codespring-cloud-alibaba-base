package com.ly.cloud.alibaba.common.base.enums;

import lombok.Getter;

/**
 * 系统错误码枚举信息
 */
@Getter
public enum SystemErrorCoeEnum implements ErrorCode {
    // 通用异常
    SUCCESS("200", "成功", "RESPONSE.SUCCESS"),
    GENERAL_API_ERROR("00010001", "通用错误", "RESPONSE.GENERAL_API_ERROR"),
    INTERNAL_API_EXCEPTION("00010002", "内部微服务调用异常", "RESPONSE.INTERNAL_API_EXCEPTION"),
    MISSING_SERVLET_REQUEST_PARAMETER("00010003", "请求参数不完整", "RESPONSE.MISSING_SERVLET_REQUEST_PARAMETER"),
    BIND("00010004", "参数绑定失败", "RESPONSE.BIND"),
    METHOD_ARGUMENT_NOT_VALID("00010005", "方法参数校验失败", "RESPONSE.METHOD_ARGUMENT_NOT_VALID"),
    REPEAT_PRIMARY_KEY("00010006", "主键或唯一键重复", "RESPONSE.REPEAT_PRIMARY_KEY"),
    NO_HANDLER_FOUND("00010007", "没有找到指定请求", "RESPONSE.NO_HANDLER_FOUND"),

    // 认知相关
    UN_SUPPORTED_GRANT_TYPE_EXCEPTION("00020001", "不支持的认证方式", "RESPONSE.UN_SUPPORTED_GRANT_TYPE_EXCEPTION"),
    UNAUTHORIZED("00020002", "未认证", "RESPONSE.UNAUTHORIZED"),
    USERNAME_OR_PASSWORD_ERROR("00020003", "用户名密码错误", "RESPONSE.USERNAME_OR_PASSWORD_ERROR"),
    INTERNAL_AUTHENTICATION_SERVICE_EXCEPTION("00020004", "内部认知异常", "RESPONSE.INTERNAL_AUTHENTICATION_SERVICE_EXCEPTION"),

    // 权限相关
    AUTHORITY_REFUSED("00030001", "权限被拒绝", "RESPONSE.AUTHORITY_REFUSED"),

    // 其他
    UNKNOWN("99999999", "未知异常处理", "RESPONSE.UNKNOWN");

    private String code;
    private String msg;
    private String i18nCode;

    SystemErrorCoeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    SystemErrorCoeEnum(String code, String message, String i18nCode) {
        this.code = code;
        this.msg = message;
        this.i18nCode = i18nCode;
    }
}
