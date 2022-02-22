package com.ly.cloud.alibaba.common.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SnowflakeGenerator {
    private static Long workerId; // 为终端ID
    private static Long datacenterId; // 数据中心ID
    private static Snowflake snowflake; // 雪花id生成器

    static {
        final String workerIdProperty = System.getProperty("common.snowflake.workerId");
        if (StrUtil.isNotEmpty(workerIdProperty)) {
            workerId = Long.valueOf(workerIdProperty);
        } else {
            workerId = 0L;
        }

        final String datacenterIdProperty = System.getProperty("common.snowflake.datacenterId");
        if (StrUtil.isNotEmpty(datacenterIdProperty)) {
            datacenterId = Long.valueOf(datacenterIdProperty);
        } else {
            datacenterId = 0L;
        }

        snowflake = IdUtil.createSnowflake(workerId, datacenterId);
        log.info("SnowflakeGenerator init success! workerId = {}, datacenterId = {}", workerId, datacenterId);
    }

    private SnowflakeGenerator() {}

    public static synchronized long snowflakeId() {
        return snowflake.nextId();
    }
}
