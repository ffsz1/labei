package com.juxiao.xchat.manager.cache.redis.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author chris
 * @Title:
 * @date 2019-05-21
 * @time 12:45
 */
@Slf4j
@Aspect
@Component
public class RedisManagerAspect {

    private final Logger redislow = LoggerFactory.getLogger("com.juxiao.xchat.redislow");

    /**
     * 环绕通知
     *
     * @param point 可用于执行切点的类
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.juxiao.xchat.manager.cache.redis.impl.RedisManagerImpl.*(..)))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            return point.proceed();
        } finally {
            long time = System.currentTimeMillis() - startTime;
            if (time > 50) {
                MethodSignature methodSignature = (MethodSignature) point.getSignature();
                String methodName = methodSignature.getMethod().getName();
                redislow.info("{} {} 耗时:>{}", methodName.toUpperCase(), StringUtils.join(point.getArgs(), " "), time);
            }
        }
    }
}
