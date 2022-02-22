package com.ly.cloud.alibaba.producer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("consumer")
public interface ConsumerFeignController {
    @GetMapping("/consumer")
    String port();
}
