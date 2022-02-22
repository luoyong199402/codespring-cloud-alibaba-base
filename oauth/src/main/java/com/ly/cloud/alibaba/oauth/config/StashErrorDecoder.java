package com.ly.cloud.alibaba.oauth.config;

import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ly.cloud.alibaba.common.base.exception.ApiException;
import com.ly.cloud.alibaba.common.base.exception.InternalApiException;
import com.ly.cloud.alibaba.common.base.model.APIResponse;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

@Configuration
@Slf4j
public class StashErrorDecoder implements ErrorDecoder {
    @Autowired
    ObjectMapper objectMapper;

    @Override
    public Exception decode(String methodKey, Response response) {
        String message = null;
        try {
            message = Util.toString(response.body().asReader(Util.UTF_8));
            final Exception exception = objectMapper.readValue(message, Exception.class);
            return exception;
        } catch (Throwable e) {
            e.printStackTrace();
            return new RuntimeException(e);
        }
    }
}