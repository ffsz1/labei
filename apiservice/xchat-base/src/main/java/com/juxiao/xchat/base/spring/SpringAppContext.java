package com.juxiao.xchat.base.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @class: SpringAppContext.java
 * @author: chenjunsheng
 * @date 2018/5/15
 */
@Component
public final class SpringAppContext implements ApplicationContextAware {
	/**
	 * 应用上下文
	 */
	private static ApplicationContext applicationContext;

	private SpringAppContext() {
	}

	/**
	 * @see ApplicationContextAware#setApplicationContext(ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringAppContext.applicationContext = applicationContext;
	}

	public static <T> T getBean(Class<T> clazz) {
		if (SpringAppContext.applicationContext == null) {
			return null;
		}
		return applicationContext.getBean(clazz);
	}

	public static <T> T getBean(String beanName, Class<T> clazz) {
		if (applicationContext == null) {
			return null;
		}
		return applicationContext.getBean(beanName, clazz);
	}
}
