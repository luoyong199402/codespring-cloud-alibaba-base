package com.ly.cloud.alibaba.producer.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.ly.cloud.alibaba.producer.config.Test;
import com.ly.cloud.alibaba.producer.dao.UserRepository;
import com.ly.cloud.alibaba.producer.entity.User;
import com.ly.cloud.alibaba.producer.feign.ConsumerFeignController;
import com.ly.cloud.alibaba.producer.sentinel.CommonErrorDetail;
import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.toolkit.trace.ActiveSpan;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("producer")
@Slf4j
public class ProducerController {
    @Autowired
    DiscoveryClient discoveryClient;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ConsumerFeignController consumerFeignController;

    @GetMapping("/1")
    // CommonErrorDetail.globalHandler 这里的参数数量必须和错误方法保持一致。 挺坑的。
    @SentinelResource(value = "hot-key", blockHandlerClass = CommonErrorDetail.class, blockHandler = "globalHandler")
    public String test(@RequestParam(value = "p1",required = false) String p1) throws InterruptedException {
        log.info("i = {}", p1);
        String url = "http://consumer/consumer";
        final ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
        final String body = forEntity.getBody();
        return body;
    }

    @GetMapping("/2")
    public String test2() {
        final String port = consumerFeignController.port();
        return port;
    }

    @GetMapping("/list")
    public List<User> list() {
        final List<User> all = userRepository.findAll();
        return all;
    }

    @GetMapping("/trace-id")
    public String traceId() {
        log.warn("trace id go!!!");
        ActiveSpan.info(" ActiveSpan trace id go!!");
        ActiveSpan.error("this is test error");
        return TraceContext.traceId();
    }
}
