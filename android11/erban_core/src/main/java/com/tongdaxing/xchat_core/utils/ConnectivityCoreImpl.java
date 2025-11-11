/**
 * 
 */
package com.tongdaxing.xchat_core.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;
import com.tongdaxing.xchat_framework.util.util.log.MLog;


/**
 * @author daixiang
 *
 */
public class ConnectivityCoreImpl extends AbstractBaseCore implements IConnectivityCore {
	private static final String TAG = ConnectivityCoreImpl.class.getSimpleName();

	private ConnectivityReceiver receiver;
	private ConnectivityState state = ConnectivityState.NetworkUnavailable;
	
	public ConnectivityCoreImpl() {
		super();
		
		Context ctx = getContext();
		if (ctx != null) {
			receiver = new ConnectivityReceiver();

			IntentFilter filter = new IntentFilter(
					ConnectivityManager.CONNECTIVITY_ACTION);
			ctx.registerReceiver(receiver, filter);

			checkConnectivity(ctx);
		}
	}
	
	private void checkConnectivity(Context ctx) {
		try{
            ConnectivityManager manager = (ConnectivityManager) ctx
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager != null) {
                NetworkInfo net = manager.getActiveNetworkInfo();
                if (net == null || !net.isConnected()) {
                    state = ConnectivityState.NetworkUnavailable;
                } else {
                    int type = net.getType();
                    switch (type) {
                        case ConnectivityManager.TYPE_MOBILE:
                            state = ConnectivityState.ConnectedViaMobile;
                            break;
                        case ConnectivityManager.TYPE_WIFI:
                            state = ConnectivityState.ConnectedViaWifi;
                            break;
                        default:
                            state = ConnectivityState.ConnectedViaOther;
                    }
                }

                if (net != null) {
                    MLog.info(TAG, "networt type " + net.getType() + " state " + net.getState() + " isAvailable " + net.isAvailable() + " isConnected " + net.isConnected());
                }

            } else {
                MLog.warn(TAG, "unable to get ConnectivityManager");
            }
        }catch (Throwable throwable){
            MLog.error(TAG, "checkConnectivity error! ", throwable);
        }
	}
	
	private class ConnectivityReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			ConnectivityState lastState = state;
			checkConnectivity(context);
			MLog.info(TAG, "ConnectivityCoreImpl onReceive prev:%s, current:%s", lastState, state);
			if (state != lastState) {
				notifyClients(IConnectivityClient.class, "onConnectivityChange", lastState, state);
			}
		}
	}

	@Override
	public ConnectivityState getConnectivityState() {
		return state;
	}
}
