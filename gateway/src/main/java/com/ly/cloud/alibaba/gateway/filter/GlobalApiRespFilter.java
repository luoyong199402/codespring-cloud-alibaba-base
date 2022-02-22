package com.ly.cloud.alibaba.gateway.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ly.cloud.alibaba.common.base.model.APIResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 全局数据返回有一定的问题。 现在网关仅仅做的 用户鉴权和路由转发。
 * 主要是考虑到  skywalking 中 traceId 无法获取。 在网上找了很多资料。 没有解决这问题。  现在数据统一返回由CommonMVC 模块统一实现了。 后期可以考虑优化。
 * 还有涉及一些流是数据不好处理。 如文件下载。
 * 由于有以上这些问题。 所有这个功能暂时没做。
 */
@Slf4j
//@Component
//@Order(value = Integer.MIN_VALUE)
public class GlobalApiRespFilter implements GlobalFilter, Ordered {

//    @Autowired
    ObjectMapper objectMapper;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        if (path.contains("websocket")) {
            return chain.filter(exchange);
        }
        //获取response的 返回数据
        ServerHttpResponse response = exchange.getResponse();

        HttpStatus statusCode = response.getStatusCode();
        if (statusCode == HttpStatus.OK) {

            ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(response) {
                @Override
                public boolean setStatusCode(HttpStatus status) {
                    return super.setStatusCode(HttpStatus.OK);
                }

                @Override
                public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                    ServerHttpResponse delegateResponse = this.getDelegate();
                    MediaType contentType = delegateResponse.getHeaders().getContentType();
                    log.debug("contentType:{}", contentType);

                    if (null == contentType) {
                        String resp = "{\"success\":true,\"code\":\"200\",\"message\":\"成功\"}";
                        byte[] newRs = resp.getBytes(StandardCharsets.UTF_8);
                        delegateResponse.setStatusCode(HttpStatus.OK);
                        delegateResponse.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
                        delegateResponse.getHeaders().setContentLength(newRs.length);
                        DataBuffer buffer = delegateResponse.bufferFactory().wrap(newRs);
                        return delegateResponse.writeWith(Flux.just(buffer));
                    }

                    Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                    return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                        DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                        DataBuffer join = dataBufferFactory.join(dataBuffers);
                        byte[] content = new byte[join.readableByteCount()];
                        join.read(content);
                        DataBufferUtils.release(join);

                        Charset contentCharset = contentType.getCharset();
                        contentCharset = (null == contentCharset) ? StandardCharsets.UTF_8 : contentCharset;
                        if (contentType.equalsTypeAndSubtype(MediaType.TEXT_HTML)) {
                            String responseData = new String(content, contentCharset);
                            log.debug("响应内容:{}", responseData);
                            final String redirectCommand = "redirect:";
                            int redirectCommandIndex = responseData.indexOf(redirectCommand);
                            if (redirectCommandIndex >= 0) {
                                String redirectUri = responseData.substring(redirectCommandIndex + redirectCommand.length()).trim();

                                delegateResponse.setStatusCode(HttpStatus.SEE_OTHER);
                                delegateResponse.getHeaders().set(HttpHeaders.LOCATION, redirectUri);
                            }
                        }
                        if (contentType.includes(MediaType.APPLICATION_JSON) || contentType.includes(MediaType.APPLICATION_JSON_UTF8)) {
                            String responseData = new String(content, contentCharset);
                            log.debug("响应内容:{}", responseData);
                            try {
                                final APIResponse apiResponse = objectMapper.readValue(responseData, APIResponse.class);
                                apiResponse.setTraceId(TraceContext.traceId());  // 重新处理下TraceId
                                byte[] newRs = objectMapper.writeValueAsBytes(apiResponse);
                                delegateResponse.getHeaders().setContentLength(newRs.length);
                                return delegateResponse.bufferFactory().wrap(newRs);
                            } catch (IOException e) {
                                log.error("\n", e);
                                String errResp = "{\"success\":false,\"code\":\"500\",\"message\":\"System Error\"}";
                                byte[] newRs = errResp.getBytes(contentCharset);
                                delegateResponse.getHeaders().setContentLength(newRs.length);
                                return delegateResponse.bufferFactory().wrap(newRs);
                            }
                        }

                        return delegateResponse.bufferFactory().wrap(content);
                    }));
                }
            };
            return chain.filter(exchange.mutate().response(decoratedResponse).build());
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -2;
    }
}
