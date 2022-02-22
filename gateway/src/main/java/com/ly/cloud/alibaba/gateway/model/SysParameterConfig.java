package com.ly.cloud.alibaba.gateway.model;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 系统参数配置
 */
@Component
@ConfigurationProperties(prefix = "oauth2.cloud.sys.parameter")
@Data
public class SysParameterConfig {
    /**
     * 白名单
     */
    private List<String> ignoreUrls;
}
