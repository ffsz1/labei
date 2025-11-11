/**
 * 
 */
package com.tongdaxing.xchat_core.utils;


import com.tongdaxing.xchat_framework.coremanager.IBaseCore;

/**
 * @author daixiang
 *
 */
public interface IConnectivityCore extends IBaseCore {

	public enum ConnectivityState {
		NetworkUnavailable,
		ConnectedViaMobile,
		ConnectedViaWifi,
		ConnectedViaOther
	}
	
	public ConnectivityState getConnectivityState();
}
