/**
 * 
 */
package com.tongdaxing.xchat_core.utils;


import com.tongdaxing.xchat_framework.coremanager.ICoreClient;

/**
 * @author daixiang
 *
 */
public interface IConnectivityClient extends ICoreClient {

	public void onConnectivityChange(IConnectivityCore.ConnectivityState previousState, IConnectivityCore.ConnectivityState currentState);
}
