package com.ly.cloud.alibaba.producer.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RefreshScope
public class TestConfig {

    public TestConfig() {
        log.info("TestConfig tringer dynamic refresh");
    }

    @Value("${student.name}")
    private String name;

    @Bean
    @RefreshScope
    public Test test() {
        log.info("动态加载刷新 name = {}", name);
        final Test test = new Test();
        test.setName(name);
        return test;
    }
}