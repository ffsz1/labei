/**
 * 用于使用annotation实现监听某个client的某个回调
 */
package com.tongdaxing.xchat_framework.coremanager;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @auth zhongyongsheng
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface CoreEvent {
	
	Class<?> coreClientClass();
}
