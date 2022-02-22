package com.ly.cloud.alibaba.common.base.exception;

import com.ly.cloud.alibaba.common.base.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * api调用异常
 * @author ly
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiException extends RuntimeException {
	private String code;
	private String msg;
	private String i18nCode;

	public ApiException(ErrorCode errorCode) {
		this.code = errorCode.getCode();
		this.msg = errorCode.getMsg();
		this.i18nCode = errorCode.getI18nCode();
	}
}
