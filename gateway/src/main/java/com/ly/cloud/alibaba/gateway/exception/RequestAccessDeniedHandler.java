package com.ly.cloud.alibaba.gateway.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ly.cloud.alibaba.common.base.enums.SystemErrorCoeEnum;
import com.ly.cloud.alibaba.common.base.model.APIResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

/**
 * 自定义返回结果：没有权限访问时
 */
@Component
@Slf4j
public class RequestAccessDeniedHandler implements ServerAccessDeniedHandler {
    @Autowired
    ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        SystemErrorCoeEnum systemErrorCoeEnum = SystemErrorCoeEnum.AUTHORITY_REFUSED;
        final APIResponse<String> apiResponse = new APIResponse<>(systemErrorCoeEnum.getCode(), systemErrorCoeEnum.getMsg(), null);
        apiResponse.setTraceId(TraceContext.traceId());  // 设置请求的traceId
        String returnInfo = "";
        try {
            returnInfo = objectMapper.writeValueAsString(apiResponse);
        } catch (JsonProcessingException ex) {
            returnInfo = "{\"success\":false,\"code\":\"500\",\"message\":\"System Error\"}";
            log.error("{}", ex);
        }
        DataBuffer buffer =  response.bufferFactory().wrap(returnInfo.getBytes(Charset.forName("UTF-8")));
        return response.writeWith(Mono.just(buffer));
    }
}