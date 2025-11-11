package com.vslk.lbgx.ui.common.server;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;

public class NetworkService extends Service {

	// Class that answers queries about the state of network connectivity.
	// 系统网络连接相关的操作管理类.

	private ConnectivityManager connectivityManager;
	// Describes the status of a network interface.
	// 网络状态信息的实例
	private NetworkInfo info;

	/**
	 * 当前处于的网络 0 ：null 1 ：2G/3G 2 ：wifi
	 */
	public static int netState;

	public static final String ACTION_NETWORK_STATE_CHANGE_SUCCESS = "ACTION_NETWORK_STATE_CHANGE_SUCCESS"; // An
																									// action
	public static final String ACTION_NETWORK_STATE_CHANGE_FAILED = "ACTION_NETWORK_STATE_CHANGE_FAILED"; // An
	private String TAG = "NetworkService";

	private int count = -1;
	

	/**
	 * 广播实例
	 */
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			System.out.println("收到网络改变广播！");
			// The action of this intent or null if none is specified.
			// action是行动的意思，也许是我水平问题无法理解为什么叫行动，我一直理解为标识（现在理解为意图）
			String action = intent.getAction(); // 当前接受到的广播的标识(行动/意图)

			// 当当前接受到的广播的标识(意图)为网络状态的标识时做相应判断
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				// 获取网络连接管理器
				connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

				// 获取当前网络状态信息
				info = connectivityManager.getActiveNetworkInfo();

				if (info != null && info.isAvailable()) {

					// 当NetworkInfo不为空且是可用的情况下，获取当前网络的Type状态
					// 根据NetworkInfo.getTypeName()判断当前网络
					String name = info.getTypeName();

					// 更改NetworkService的静态变量，之后只要在Activity中进行判断就好了
					if (name.equals("WIFI")) {
						netState = 2;
					} else {
						netState = 1;
					}

				} else {
					// NetworkInfo为空或者是不可用的情况下
					netState = 0;

//					Toast.makeText(context, "没有可用网络!\n请连接网络后刷新本界面", Toast.LENGTH_SHORT).show();

					/**
					 * 这里推荐使用本地广播的方式发送:
					 * LocalBroadcastManager.getInstance(getApplicationContext()
					 * ).sendBroadcast(intent);
					 */
				}
				
				if(netState == 0){
					sendNetBroadCast(ACTION_NETWORK_STATE_CHANGE_FAILED,netState);
					Log.d(TAG, "网络已断开");
				}else{
//					if (count != -1) {
//						if (!ConnectionManager.getInstance().getConnection().isConnected()) {
//							Log.d(TAG, "启动重连服务器！切换网络计数:" + count);
//							ChatManager.getInstance().reconnect(ChatManager.getInstance().getWorkHandler());// 重连服务器
//						}
//						Log.d(TAG, "网络已链接");
//					}
					count++;
				}

//				switch (netState) {
//				case 2:// WIFI
//					Log.d(TAG, "已切换为wifi");
//					if (count != -1) {
//						if (!ConnectionManager.getInstance().getConnection().isConnected()) {
//							Log.d(TAG, "启动重连服务器！切换网络计数:" + count);
//							ChatManager.getInstance().reconnect(ChatManager.getInstance().getWorkHandler());// 重连服务器
//						}
//
//					}
//					count++;
//					break;
//				case 1:// other
//					Log.d(TAG, "已切换为手机网络");
//					if (count != -1) {
//						if (!ConnectionManager.getInstance().getConnection().isConnected()) {
//							Log.d(TAG, "启动重连服务器！切换网络计数:" + count);
//							ChatManager.getInstance().reconnect(ChatManager.getInstance().getWorkHandler());// 重连服务器
//						}
//
//					}
//					count++;
//					break;
//				case 0:// no net
//					Log.d(TAG, "网络已断开");
//					break;
//
//				default:
//					break;
//				}
			
				
			}
		}
	};
	
	/**
	 * 网络链接上的广播
	 */
	public void sendNetBroadCast(String action, int netState){
		Intent it = new Intent();
		it.putExtra("networkStatus", netState);
		it.setAction(action);
		sendBroadcast(it); // 发送无网络广播给注册了当前服务广播的Activity
	}
	
//	private ChatMsgFactory msgFactory;
//	private ChatManager chatManager;

	// /**
	// * 发送文字消息
	// */
	// private void sendTextReConnected(ChatMsg msg) {
	// SendMsg sendMsg = msgFactory.creatSendMsgForGroupText(msg.getToUserId(),
	// msg.getChatId(), msg.getContent());
	// chatManager.sendText(sendMsg);
	// }

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		System.out.println("服务被创建");
//		msgFactory = ChatMsgFactory.getInstance();
//		chatManager = ChatManager.getInstance();

		// 注册网络状态的广播，绑定到mReceiver
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mReceiver, mFilter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println("服务被销毁");
		// 注销接收
		unregisterReceiver(mReceiver);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 判断网络是否可用
	 */
	public static boolean isNetworkAvailable(Context context) {
		// 获取网络连接管理器
		ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		// 获取当前网络状态信息
		NetworkInfo[] info = mgr.getAllNetworkInfo();
		if (info != null) {
			for (int i = 0; i < info.length; i++) {
				if (info[i].getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}

		return false;
	}
}