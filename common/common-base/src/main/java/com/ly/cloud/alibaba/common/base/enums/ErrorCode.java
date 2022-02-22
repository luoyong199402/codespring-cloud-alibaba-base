package com.ly.cloud.alibaba.common.base.enums;

/**
 * 错误码接口
 */
public interface ErrorCode {
    /**
     * 获取错误码
     * @return 错误码
     */
    String getCode();

    /**
     * 获取错误信息
     * @return 错误信息
     */
    String getMsg();

    /**
     * 获取错误码国际化描述
     * @return 错误码国际化描述
     */
    default String getI18nCode() {
        return null;
    }
}
