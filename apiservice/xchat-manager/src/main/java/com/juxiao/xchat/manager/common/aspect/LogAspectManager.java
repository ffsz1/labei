package com.juxiao.xchat.manager.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @class: LogAspectManager.java
 * @author: chenjunsheng
 * @date 2018/6/27
 */
public interface LogAspectManager {

    /**
     * @param point
     * @return
     */
    Object logAround(ProceedingJoinPoint point);
}
