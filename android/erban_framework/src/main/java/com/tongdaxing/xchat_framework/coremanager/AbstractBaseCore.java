/**
 * 每个core实现类都应该继承此类
 * 提供一些基础设施给子类使用
 */
package com.tongdaxing.xchat_framework.coremanager;

import android.content.Context;

import com.tongdaxing.xchat_framework.util.config.BasicConfig;


/**
 * @author daixiang
 *
 */
public abstract class AbstractBaseCore implements IBaseCore {

	public AbstractBaseCore() {
		// 确保有默认构造函数
	}

	protected Context getContext() {
		return BasicConfig.INSTANCE.getAppContext();
	}


	protected void notifyClients(Class<? extends ICoreClient> clientClass, String methodName, Object... args) {
		CoreManager.notifyClients(clientClass, methodName, args);
	}

	protected void notifyClients(Class<? extends ICoreClient> clientClass, String methodName) {
		CoreManager.notifyClients(clientClass, methodName);
	}


}
