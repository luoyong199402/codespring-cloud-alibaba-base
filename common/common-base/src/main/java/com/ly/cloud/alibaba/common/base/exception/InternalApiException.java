package com.ly.cloud.alibaba.common.base.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class InternalApiException extends RuntimeException {
    public InternalApiException(Throwable cause) {
        super(cause);
    }
}
