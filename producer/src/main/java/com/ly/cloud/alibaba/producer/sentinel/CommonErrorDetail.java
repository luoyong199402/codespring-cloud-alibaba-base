package com.ly.cloud.alibaba.producer.sentinel;

import com.alibaba.csp.sentinel.slots.block.BlockException;

public class CommonErrorDetail {
    /**
     * 这里的参数数量必须和错误方法保持一致。 挺坑的。
     */
    public static String globalHandler(String p1, BlockException blockException){
        blockException.printStackTrace();
        return "统一处理";
    }
}
