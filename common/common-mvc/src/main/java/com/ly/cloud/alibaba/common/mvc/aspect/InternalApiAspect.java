package com.ly.cloud.alibaba.common.mvc.aspect;

import com.ly.cloud.alibaba.common.base.exception.InternalApiException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class InternalApiAspect {

    @Pointcut("@annotation(com.ly.cloud.alibaba.common.base.annotation.InternalApi) || @within(com.ly.cloud.alibaba.common.base.annotation.InternalApi)")
    public void internalApiPointCut() {}

    @Around("internalApiPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) {
        try {
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            final InternalApiException internalApiException = new InternalApiException(throwable);
            throw internalApiException;
        }
    }
}
