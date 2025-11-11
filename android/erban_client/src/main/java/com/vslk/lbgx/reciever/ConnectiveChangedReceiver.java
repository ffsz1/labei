package com.vslk.lbgx.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;

import com.tongdaxing.xchat_framework.util.util.NetworkUtils;
import com.tongdaxing.xchat_framework.util.util.valid.BlankUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Creator: 舒强睿
 * Date:2015/2/12
 * Time:20:21
 * <p/>
 * Description：监听网络状态改变
 */
public class ConnectiveChangedReceiver extends BroadcastReceiver {

    public static final String ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    private Context context;

    int currentState;//当前网络状态 第一次初始化
    boolean hasChanged;//网络类型是否已经通知改变（interval时间后会重置为false)
    final long interval = 2000L;//检验网络变化时间间隔为interval （毫秒）

    private static ConnectiveChangedReceiver receiver = new ConnectiveChangedReceiver();
    private IntentFilter intentFilter;
    private Handler handler;

    private List<ConnectiveChangedListener> listeners;//观察者（观察网络变化对象）列表

    private ConnectiveChangedReceiver() {
    }

    public int getCurrentState() {
        return currentState;
    }

    public void init(Context context) {
        this.context = context;
        this.currentState = NetworkUtils.getNetworkType(context);

        intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectiveChangedReceiver.ACTION);
    }

    public static ConnectiveChangedReceiver getInstance() {
        return receiver;
    }

    public void registerConnectiveChange(ConnectiveChangedListener listener) {

        if (listener != null) {

            if (listeners == null) {
                listeners = new ArrayList<ConnectiveChangedListener>();
            }

            listeners.add(listener);

            tryOpenObserver();
        }
    }

    public void unRegisterConnectiveChange(ConnectiveChangedListener listener) {

        if (listener != null && listeners != null && listeners.size() > 0) {

            listeners.remove(listener);

            tryClosedObserver();
        }
    }

    /**
     * 可能listeners 对象变成 null了，这里交给引用的地方 unRegisterConnectiveChange(..)
     */
    private void tryOpenObserver() {

        if (listeners != null && listeners.size() == 1) {
            context.registerReceiver(this, intentFilter);
        }
    }

    private void tryClosedObserver() {

        if (BlankUtil.isBlank(listeners)) {
            context.unregisterReceiver(this);
        }
    }


    @Override
    public void onReceive(final Context context, Intent intent) {

        /*int afterChanged = NetworkUtils.getNetworkType(context);//检测通知时网络链接是否真的有变化*/
        if (!hasChanged) {
            hasChanged = true;

            if (handler == null) {
                handler = new Handler();
            }

            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    hasChanged = false;
                    int networkState = NetworkUtils.getNetworkType(context);

                    if (networkState != currentState) {//通知网络发生变化
                        if (!BlankUtil.isBlank(listeners)) {

                            if (currentState == NetworkUtils.NET_WIFI) {//wifi
                                if (networkState == NetworkUtils.NET_3G
                                        || networkState == NetworkUtils.NET_2G) {
                                    int size = listeners.size();
                                    for (int i = 0; i < size; i++) {
                                        listeners.get(i).wifiChange2MobileData();
                                    }
                                } else {//wifi转无网络
                                    int size = listeners.size();
                                    for (int i = 0; i < size; i++) {
                                        listeners.get(i).change2NoConnection();
                                    }
                                }
                            } else if (currentState == NetworkUtils.NET_3G
                                    || currentState == NetworkUtils.NET_2G) { //手机流量
                                if (networkState == NetworkUtils.NET_WIFI) {//3G->wifi
                                    int size = listeners.size();
                                    for (int i = 0; i < size; i++) {
                                        listeners.get(i).mobileDataChange2Wifi();
                                    }
                                } else {//3G转无网络
                                    int size = listeners.size();
                                    for (int i = 0; i < size; i++) {
                                        listeners.get(i).change2NoConnection();
                                    }
                                }
                            } else {//无网络
                                if (networkState == NetworkUtils.NET_WIFI) {
                                    int size = listeners.size();
                                    for (int i = 0; i < size; i++) {
                                        listeners.get(i).connectiveWifi();
                                    }
                                } else if (networkState == NetworkUtils.NET_3G
                                        || networkState == NetworkUtils.NET_2G) {
                                    int size = listeners.size();
                                    for (int i = 0; i < size; i++) {
                                        listeners.get(i).connectiveMobileData();
                                    }
                                }
                            }
                        }
                        currentState = networkState;
                    }
                }
            }, interval);
        }
    }

    /**
     * 网络连接改变
     */
    public static interface ConnectiveChangedListener {
        /* public void onConnectiveChange(int previousNetType, int currentNetType);*/

        /**
         * wifi 转 2G/3G/4G
         */
        public void wifiChange2MobileData();

        /**
         * 有网络变为无网络
         */
        public void change2NoConnection();

        /**
         * 无网络连上wifi
         */
        public void connectiveWifi();

        /**
         * 无网络连上移动数据网络
         */
        public void connectiveMobileData();

        /**
         * 移动数据网络 改为连上wifi
         */
        public void mobileDataChange2Wifi();
    }
}
