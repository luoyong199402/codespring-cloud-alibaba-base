package com.ly.cloud.alibaba.common.mvc.adviser;


import cn.hutool.core.util.StrUtil;
import com.ly.cloud.alibaba.common.base.enums.SystemErrorCoeEnum;
import com.ly.cloud.alibaba.common.base.exception.ApiException;
import com.ly.cloud.alibaba.common.base.exception.InternalApiException;
import com.ly.cloud.alibaba.common.base.model.APIResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 全局异常拦截器
 * @author ly
 */
@Slf4j
@RestController
@ControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * spring 提供的国际化处理
	 */
	@Resource
	MessageSource messageSource;

	@ExceptionHandler({MissingServletRequestParameterException.class})
	@ResponseBody
	public APIResponse processRequestParameterException(HttpServletRequest request,
														HttpServletResponse response,
														MissingServletRequestParameterException e) {
		return APIResponse.builder()
				.code(SystemErrorCoeEnum.MISSING_SERVLET_REQUEST_PARAMETER.getCode())
				.msg(getI18nMessage(SystemErrorCoeEnum.MISSING_SERVLET_REQUEST_PARAMETER))
				.data(e)
				.build();
	}

	@ExceptionHandler(ApiException.class)
	@ResponseBody
	public APIResponse processApiException(HttpServletResponse response,
										   ApiException e) {
		response.setContentType("application/json;charset=UTF-8");
		APIResponse apiResponse = APIResponse.builder()
				.code(e.getCode())
				.data(e)
				.msg(e.getMsg()).build();
		return apiResponse;
	}

	/**
	 * 内部微服务异常统一处理方法
	 */
	@ExceptionHandler(InternalApiException.class)
	@ResponseBody
	public Throwable processMicroServiceException(HttpServletResponse response,
													InternalApiException e) {
		response.setContentType("application/json;charset=UTF-8");
		final Throwable cause = e.getCause();
		if (
				cause instanceof ApiException
				|| cause instanceof MissingServletRequestParameterException
				|| cause instanceof DuplicateKeyException
				|| cause instanceof MethodArgumentNotValidException
				|| cause instanceof BindException
				|| cause instanceof NoHandlerFoundException) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		} else {
			response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
		}
		return cause;
	}

	/**
	 * 参数校验失败异常处理
	 */
	@ExceptionHandler(DuplicateKeyException.class)
	@ResponseBody
	public APIResponse processDuplicateKeyException(HttpServletResponse response,
													DuplicateKeyException e) {
		response.setContentType("application/json;charset=UTF-8");
		return APIResponse.builder()
				.code(SystemErrorCoeEnum.REPEAT_PRIMARY_KEY.getCode())
				.msg(getI18nMessage(SystemErrorCoeEnum.REPEAT_PRIMARY_KEY))
				.data(e)
				.build();
	}

	/**
	 * 全局未知异常统一处理
	 */
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public APIResponse processException(HttpServletResponse response,
													Exception e) {
		response.setContentType("application/json;charset=UTF-8");
		log.info("{}", e);
		return APIResponse.builder()
				.code(SystemErrorCoeEnum.UNKNOWN.getCode())
				.msg(getI18nMessage(SystemErrorCoeEnum.UNKNOWN))
				.data(e)
				.build();
	}

	/**
	 * 参数校验失败异常处理
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	public APIResponse processMethodArgumentNotValidException(HttpServletResponse response,
				  MethodArgumentNotValidException e) {
		response.setContentType("application/json;charset=UTF-8");
		return APIResponse.builder()
				.code(SystemErrorCoeEnum.METHOD_ARGUMENT_NOT_VALID.getCode())
				.msg(getI18nMessage(SystemErrorCoeEnum.METHOD_ARGUMENT_NOT_VALID))
				.data(e)
				.build();
	}

	/**
	 * 参数校验失败异常处理
	 */
	@ExceptionHandler(BindException.class)
	@ResponseBody
	public APIResponse processBindException(HttpServletResponse response,
							   BindException e) {
		response.setContentType("application/json;charset=UTF-8");
		return APIResponse.builder()
				.code(SystemErrorCoeEnum.BIND.getCode())
				.msg(getI18nMessage(SystemErrorCoeEnum.BIND))
				.data(e)
				.build();
	}

	/**
	 * 处理404异常
	 */
	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseBody
	public APIResponse processNoHandlerFoundException(HttpServletResponse response,
													  NoHandlerFoundException e) {
		response.setContentType("application/json;charset=UTF-8");
		return APIResponse.builder()
				.code(SystemErrorCoeEnum.NO_HANDLER_FOUND.getCode())
				.msg(getI18nMessage(SystemErrorCoeEnum.NO_HANDLER_FOUND))
				.data(e)
				.build();
	}

	private String getI18nMessage(SystemErrorCoeEnum systemErrorCoeEnum) {
		if (StrUtil.isNotEmpty(systemErrorCoeEnum.getI18nCode())) {
			return messageSource.getMessage(systemErrorCoeEnum.getI18nCode(), null, LocaleContextHolder.getLocale());
		} else {
			return systemErrorCoeEnum.getMsg();
		}
	}
}
