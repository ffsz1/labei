package com.juxiao.xchat.dao.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


/**
 * 保证该AOP在@Transactional之前执行
 *
 * @class: DynamicDataSourceAspect.java
 * @author: chenjunsheng
 * @date 2018/6/6
 */
@Aspect
@Order(-1)
@Component
public class DynamicDataSourceAspect {

    //改变数据源
    @Before("@annotation(targetDataSource)")
    public void changeDataSource(JoinPoint joinPoint, TargetDataSource targetDataSource) {
        String dbid = targetDataSource.name();
        if (DynamicDataSourceContextHolder.isContainsDataSource(dbid)) {
            DynamicDataSourceContextHolder.setDataSourceType(dbid);
        }
    }

    @After("@annotation(targetDataSource)")
    public void clearDataSource(JoinPoint joinPoint, TargetDataSource targetDataSource) {
        DynamicDataSourceContextHolder.clearDataSourceType();
    }
}
