package com.ly.cloud.alibaba.gateway.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ly.cloud.alibaba.common.base.enums.SystemErrorCoeEnum;
import com.ly.cloud.alibaba.common.base.model.APIResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 用于处理没有登录或token过期时的自定义返回结果
 */
@Component
@Slf4j
public class RequestAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
    @Autowired
    ObjectMapper objectMapper;

    @Override
    @Trace
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        SystemErrorCoeEnum systemErrorCoeEnum = SystemErrorCoeEnum.UNAUTHORIZED;
        final APIResponse<String> apiResponse = new APIResponse<>(systemErrorCoeEnum.getCode(), systemErrorCoeEnum.getMsg(), null);
        apiResponse.setTraceId(TraceContext.traceId());  // 设置请求的traceId
        DataBuffer buffer = null;
        try {
            final byte[] bytes = objectMapper.writeValueAsBytes(apiResponse);
            buffer =  response.bufferFactory().wrap(bytes);
        } catch (JsonProcessingException ex) {
            log.error("{}", ex);
            String errResp = "{\"success\":false,\"code\":\"500\",\"message\":\"System Error\"}";
            buffer =  response.bufferFactory().wrap(errResp.getBytes(StandardCharsets.UTF_8));
        }
        return response.writeWith(Mono.just(buffer));
    }
}