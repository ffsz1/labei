package com.juxiao.xchat.record.interceptor;

import com.juxiao.xchat.manager.common.aspect.LogAspectManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @class: ApiLogInterceptor.java
 * @author: chenjunsheng
 * @date 2018/6/27
 */
@Aspect
@Component
public class RecordLogInterceptor {
    @Autowired
    private LogAspectManager aspectManager;

    /**
     * 接口统一日志输出
     *
     * @param point
     * @return
     * @author: chenjunsheng
     * @date 2018年6月27日
     */
    @Around("execution(!void com.juxiao.xchat.record.controller..*Controller.*(..)))")
    public Object logAround(ProceedingJoinPoint point) {
        return aspectManager.logAround(point);
    }
}
