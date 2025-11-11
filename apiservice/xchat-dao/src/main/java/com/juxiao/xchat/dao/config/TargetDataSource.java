package com.juxiao.xchat.dao.config;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2017/9/7.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {

    String name() default "";

}
