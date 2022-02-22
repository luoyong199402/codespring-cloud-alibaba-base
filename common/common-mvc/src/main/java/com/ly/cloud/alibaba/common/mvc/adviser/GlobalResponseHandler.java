package com.ly.cloud.alibaba.common.mvc.adviser;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.support.spring.MappingFastJsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ly.cloud.alibaba.common.base.annotation.CommonResponse;
import com.ly.cloud.alibaba.common.base.enums.SystemErrorCoeEnum;
import com.ly.cloud.alibaba.common.base.model.APIResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Resource;

/**
 * 增加全局数据访问
 * @author ly
 */
@Slf4j
@RestControllerAdvice
@ConfigurationProperties("enrising.response.ignore")
@Data
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {
	@Autowired
	private ObjectMapper objectMapper;

	/**
	 * spring 提供的国际化处理
	 */
	@Resource
	MessageSource messageSource;

	@Override
	public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
		final CommonResponse annotation = methodParameter.getExecutable().getAnnotation(CommonResponse.class);
		if (annotation != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
		if (o instanceof MappingFastJsonValue) {
			MappingFastJsonValue mappingFastJsonValue = (MappingFastJsonValue) o;
			o = mappingFastJsonValue.getValue();
		}
		// 字符串单独处理。 不然过滤器报错。
		if (o instanceof String) {
			try {
				serverHttpResponse.getHeaders().setContentType(MediaType.APPLICATION_JSON);
				return objectMapper.writeValueAsString(ok(o));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		return ok(o);
	}

	private String getI18nMessage(SystemErrorCoeEnum systemErrorCoeEnum) {
		if (StrUtil.isNotEmpty(systemErrorCoeEnum.getI18nCode())) {
			return messageSource.getMessage(systemErrorCoeEnum.getI18nCode(), null, LocaleContextHolder.getLocale());
		} else {
			return systemErrorCoeEnum.getMsg();
		}
	}

	private APIResponse ok(Object data) {
		return APIResponse.builder()
				.code(SystemErrorCoeEnum.SUCCESS.getCode())
				.msg(SystemErrorCoeEnum.SUCCESS.getMsg())
				.traceId(TraceContext.traceId())
				.data(data).build();
	}
}
