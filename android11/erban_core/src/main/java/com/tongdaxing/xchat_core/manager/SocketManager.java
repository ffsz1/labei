package com.tongdaxing.xchat_core.manager;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.AppUtils;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_framework.im.ICommonListener;
import com.tongdaxing.xchat_framework.im.IConnectListener;
import com.tongdaxing.xchat_framework.im.IMCallBack;
import com.tongdaxing.xchat_framework.im.IMError;
import com.tongdaxing.xchat_framework.im.IMErrorBean;
import com.tongdaxing.xchat_framework.im.IMKey;
import com.tongdaxing.xchat_framework.im.IMModelFactory;
import com.tongdaxing.xchat_framework.im.IMReportRoute;
import com.tongdaxing.xchat_framework.im.IMSendRoute;
import com.tongdaxing.xchat_framework.util.config.BasicConfig;
import com.tongdaxing.xchat_framework.util.util.Json;
import com.tongdaxing.xchat_framework.util.util.LogUtil;
import com.tongdaxing.xchat_framework.util.util.StringUtils;
import com.yingtao.ndklib.JniUtils;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ServerHandshake;

import java.lang.ref.WeakReference;
import java.net.Proxy;
import java.net.URI;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class SocketManager {
    private final String TAG = "SocketManager";
    //当前网络状态
    private int status = NET_TYPE_CLOSED;
    private boolean has_destroy = false;
    //socket断开的原因
    private int closeReason = CALL_BACK_CODE_DISCONNECT;


    //网络状态
    public static final int NET_TYPE_CONNECTED = 1;
    public static final int NET_TYPE_CONNECTING = 2;
    public static final int NET_TYPE_CLOSED = 0;

    private WebSocketClient webSocketClient = null;

    public static final long TIMEOUT_TIME = 8000;

    //断开原因
    public static final int CALL_BACK_CODE_REASON_TIMEOUT = 408;
    public static final int CALL_BACK_CODE_DISCONNECT = 409;
    public static final int CALL_BACK_CODE_SELFCLOSE = 410; // 手动主动关闭

    //发送
    public static final int SEND_SUCCESS = 1;
    public static final int SEND_ERROR = 0;

    //请求超时
    public static final int CHECK_CALL_BACK_TIMEOUT = 0;

    //链接回调
    public static final int IM_CONNECT_SUCCESS = 1;
    public static final int IM_CONNECT_ERROR = 2;

    //接受到IM信息通知
    public static final int IM_CONNECT_MSG = 3;
    //WebSocketClient断开
    public static final int IM_CONNECT_DISCONNECT = 4;

    private IConnectListener iConnectListener;
    private ICommonListener iCommonListener;
    private SocketActionHandler socketActionHandler;

    public SocketManager() {
        socketActionHandler = new SocketActionHandler(this);
    }


    private void log(String content) {
//        LogUtil.e("socket_action", content);
    }

    private static class SocketActionHandler extends Handler {
        private WeakReference<SocketManager> weakReference;

        public SocketActionHandler(SocketManager weakReference) {
            this.weakReference = new WeakReference<>(weakReference);
        }

        @Override
        public void handleMessage(Message msg) {
            if (weakReference == null || weakReference.get() == null) {
                return;
            }
            SocketManager socketManager = weakReference.get();
            if (socketManager.has_destroy) {
                //销毁的时候把消息清掉，防止内存泄漏
                socketManager.socketActionHandler.removeCallbacksAndMessages(null);
                return;
            }
            //超时移除
            if (msg.what == CHECK_CALL_BACK_TIMEOUT) {
                int eventId = (Integer) msg.obj;
                IMCallBack remove = callBackHashtable.remove(eventId);
                if (remove != null)
                    remove.onError(CALL_BACK_CODE_REASON_TIMEOUT, "timeout");
            } else if (msg.what == IM_CONNECT_SUCCESS) {
                socketManager.status = NET_TYPE_CONNECTED;
                if (socketManager.iConnectListener != null) {
                    socketManager.iConnectListener.onSuccess((ServerHandshake) msg.obj);
                    socketManager.iConnectListener = null;
                }
            } else if (msg.what == IM_CONNECT_ERROR) {
                socketManager.status = NET_TYPE_CLOSED;
                if (socketManager.iConnectListener != null) {
                    socketManager.iConnectListener.onError((Exception) msg.obj);
                    socketManager.iConnectListener = null;
                }
            } else if (msg.what == IM_CONNECT_MSG) {
                String message = (String) msg.obj;
                //成功回调
                socketManager.callBackResponse(msg.arg1, message);
            } else if (msg.what == IM_CONNECT_DISCONNECT) {
                socketManager.status = NET_TYPE_CLOSED;
                socketManager.clearMap();
                if (socketManager.iCommonListener != null) {
                    socketManager.iCommonListener.onDisconnectCallBack((IMErrorBean) msg.obj);
                }
            }
        }
    }

    private static ConcurrentHashMap<Integer, IMCallBack> callBackHashtable = new ConcurrentHashMap<>();

    private String getImUrl() {
        String url = UriProvider.ROOM_IM_URL;
        Log.e(UriProvider.TAG, "房间公聊屏IM地址: " + url);
        return url;
    }

    private Runnable connectRunnable = new Runnable() {
        @Override
        public void run() {

            new Thread() {
                @Override
                public void run() {
                    if (has_destroy) {
                        return;
                    }
                    status = NET_TYPE_CONNECTING;
                    try {
                        webSocketClient = new WebSocketClient(new URI(getImUrl()), new Draft_6455(), null, 15000) {
                            @Override
                            public void onOpen(ServerHandshake handshakedata) {
                                log("onOpen");
                                onConnect(handshakedata);
                            }

                            @Override
                            public void onMessage(String message) {
                                Json result = null;
                                try {
                                    Json json = Json.parse(message);
                                    message = JniUtils.decryptAes(BasicConfig.INSTANCE.getAppContext(), json.str("ed"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                result = Json.parse(message);
                                onMsg(result == null ? -1 : result.num("id"), getImEventCode(result), result);
//                                log("onNoticeMessage:" + message);
                            }

                            @Override
                            public void onClose(int code, String reason, boolean remote) {
                                status = NET_TYPE_CLOSED;
                                log("onClose   " + code + "  " + reason);
                                onDisconnect(code, reason, remote);
                            }

                            @Override
                            public void onError(Exception ex) {
                                status = NET_TYPE_CLOSED;
                                onErr(ex);
                                log("onError:" + ex);
                            }

                        };
                        if(!AppUtils.isAppDebug()){
                            webSocketClient.setProxy(Proxy.NO_PROXY);//代理设置要在连接之前
                        }
                        webSocketClient.connect();
                    } catch (Exception e) {

                    }
                }
            }.start();
        }
    };


    public static final int IM_CODE_KICK_OFF = -100;
    public static final int IM_CODE_NO_NEED = -101;
    public static final int IM_CODE_HEARTBEAT = -102;

    private int getImEventCode(Json json) {
        // TODO: 2018/9/28 获取数据类型
        String route = json.str(IMKey.route);
        if (route.equals(IMReportRoute.kickoff))
            return IM_CODE_KICK_OFF;
        if (route.equals("notciefromsvr"))
            return IM_CODE_NO_NEED;
        if (route.equals(IMSendRoute.heartbeat))
            return IM_CODE_HEARTBEAT;
        return 0;
    }


    private void onErr(Exception ex) {
        Message message = new Message();
        message.what = IM_CONNECT_ERROR;
        message.obj = ex;
        socketActionHandler.sendMessage(message);
    }


    public static final int CLOSE_HEART_BEAT_TIME_OUT = 777;
    public static final int CLOSE_STOP_SOCKET = 778;

    private void onDisconnect(int code, String reason, boolean remote) {
        Message message = new Message();
        message.what = IM_CONNECT_DISCONNECT;
        IMErrorBean errorBean = new IMErrorBean();
        errorBean.setCode(code);
        errorBean.setReason(reason);
        errorBean.setRemote(remote);
        errorBean.setCloseReason(closeReason);
        message.obj = errorBean;
        socketActionHandler.sendMessage(message);
    }

    private boolean hasStartHeartBeat = false;

    private void clearMap() {
        if (callBackHashtable == null || callBackHashtable.size() == 0) {
            return;
        }

        Iterator<IMCallBack> iterator = callBackHashtable.values().iterator();
        while (iterator.hasNext()) {
            iterator.next().onError(CALL_BACK_CODE_REASON_TIMEOUT, "heartBeatTimeOut");
        }
        callBackHashtable.clear();
    }

    private void onConnect(ServerHandshake handshakedata) {
        Message message = new Message();
        message.what = IM_CONNECT_SUCCESS;
        message.obj = handshakedata;
        socketActionHandler.sendMessage(message);

        if (!hasStartHeartBeat) {
            hasStartHeartBeat = true;
            socketActionHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);  // 连接成功才启动心跳  只启动一次
        }
    }


    private void onMsg(int callBackId, int imEventCode, Json message) {
        if (status == NET_TYPE_CONNECTING || status == NET_TYPE_CLOSED) return;

        //im事件
        imEvent(imEventCode, message, callBackId);


    }


    public interface IMNoticeMsgListener {

        void onNotice(Json json);

        //断网重连进入聊天室成功
        void onDisConnectEnterRoomSuc();

        void onDisConnection(boolean isCloseSelf);

        void onLoginError(int err_code, String reason);

        //断网重连登录IM成功
        void onDisConntectIMLoginSuc();

    }


    private void imEvent(int imEventCode, final Json message, int callBackId) {


        if (imEventCode == IM_CODE_KICK_OFF) {
            log("kickoff");
            socketActionHandler.post(new Runnable() {//强制被踢的通知
                @Override
                public void run() {
                    if (iCommonListener != null) {
                        iCommonListener.onNoticeMessage(message == null ? "kickoff" : message.toString());
                    }
                }
            });
            disconnect();
            return;
        } else if (imEventCode == IM_CODE_NO_NEED) {
            return;
        }
        // TODO: 2018/10/30 要判断是否需要跑回调

        Message m = new Message();
        m.what = IM_CONNECT_MSG;
        m.obj = message.toString();
        m.arg1 = callBackId;
        socketActionHandler.sendMessage(m);


    }


    private void callBackResponse(int eventId, String message) {

        if (message == null)
            return;


        boolean isRes = message.contains("res_data");
        IMCallBack iimCallBack = null;
        //还有res_data是回调通知
        if (isRes)
            iimCallBack = callBackHashtable.remove(eventId);
        if (iimCallBack != null) {
            iimCallBack.onSuccess(message);
        } else { // 没找到处理的回调  说明可能是主动推消息需要外部处理(也可能超时后收到的消息 也外部处理)
            if (iCommonListener != null) {
                iCommonListener.onNoticeMessage(message);
            }
        }


    }


    private void heartBeatTimeOut() {
        log("heartBeatTimeOut");
        status = NET_TYPE_CLOSED;
        closeReason = CALL_BACK_CODE_REASON_TIMEOUT;
        closeSocket(CLOSE_HEART_BEAT_TIME_OUT);
    }

    private void closeSocket(int closeCode) {
        switch (closeCode) {
            case CLOSE_HEART_BEAT_TIME_OUT:
                break;
            case CLOSE_STOP_SOCKET:
                break;
            default:
        }
        try {
//            webSocketClient.close();
            webSocketClient.closeConnection(CloseFrame.ABNORMAL_CLOSE, "abnormal close");
        } catch (Exception e) {

        }
    }


    private String getHeartBeatData() {

        return IMModelFactory.get().createRequestData(IMSendRoute.heartbeat).toString();
    }

    private static final long HEART_BEAT_RATE = 5 * 1000;//每隔5秒进行一次对长连接的心跳检测

    private long sendTime = 0L;
    // 发送心跳包
    private Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            if (has_destroy) return;
//            if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {

            if (isConnect()) {
                //链接状态发心跳包，报错就断开链接
                send(getHeartBeatData(), new IMCallBack() {
                    @Override
                    public void onSuccess(String data) {
                        if (TextUtils.isEmpty(data))
                            heartBeatTimeOut();
                    }

                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        heartBeatTimeOut();
                    }
                });
            } else {
                //重连写到业务逻辑
            }
//                sendTime = System.currentTimeMillis();
//            }
            //手动或者服务器断开要停掉

            socketActionHandler.postDelayed(this, HEART_BEAT_RATE);//每隔一定的时间，对长连接进行一次心跳检测
        }
    };

    //-------------------------------------------对外开放的方法-------------------------------------------------------------------


    /**
     * 注册断开回调函数  告诉业务层断开( code 告诉我们时超时导致还是网络导致断开还是手动断开)
     * 注册服务器单向推消息处理回调
     *
     * @param iCommonListener
     */
    public void setiCommonListener(ICommonListener iCommonListener) {
        this.iCommonListener = iCommonListener;
    }

    /**
     * 手动断开链接
     */
    public void disconnect() {

        status = NET_TYPE_CLOSED;
        closeReason = CALL_BACK_CODE_SELFCLOSE;
        closeSocket(CLOSE_STOP_SOCKET);
    }


    public void destroy() {
        has_destroy = true;
        status = NET_TYPE_CLOSED;
        closeReason = CALL_BACK_CODE_SELFCLOSE;

        clearMap();
        closeSocket(CLOSE_STOP_SOCKET);
    }


    /**
     * @return 链接状态
     */
    public boolean isConnect() {
        return status == NET_TYPE_CONNECTED;

    }

    private boolean connect(int delay) {
        status = NET_TYPE_CONNECTING;
        //socketActionHandler.post(connectRunnable);
        this.closeSocket(CLOSE_STOP_SOCKET);
        socketActionHandler.postDelayed(connectRunnable, delay);
        return true;
    }

    /**
     * 链接socket
     *
     * @param iConnectListener 链接结果回调
     * @return
     */
    public boolean connect(IConnectListener iConnectListener) {
        return this.connect(iConnectListener, 0);
    }

    /**
     * @param iConnectListener
     * @param delay            延迟connect 毫秒
     * @return
     */
    public boolean connect(IConnectListener iConnectListener, int delay) {
        if (status == NET_TYPE_CONNECTING || status == NET_TYPE_CONNECTED) {
            if (iConnectListener != null)
                iConnectListener.onError(new Exception("Dubble connect!"));
            return false;
        }
        closeReason = CALL_BACK_CODE_DISCONNECT;// 重置当前断开原因
        this.iConnectListener = iConnectListener;
        return connect(delay);
    }

    /**
     * 对服务器发送消息
     *
     * @param content    发送内容
     * @param imCallBack 结果的回调
     * @return
     */
    public void send(String content, @NonNull IMCallBack imCallBack) {
        Json json = Json.parse(content);
        json.set("id", imCallBack.getCallbackId());
        String result = "";
        try {
            result = JniUtils.encryptAes(BasicConfig.INSTANCE.getAppContext(), json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (StringUtils.isEmpty(result)) {
            imCallBack.onError(-1, IMError.IM_MS_SEND_ERROR);
            return;
        }
        Json sendContent = new Json();
        sendContent.set("ed", result);
        content = sendContent.toString();
        if (status == NET_TYPE_CONNECTED && webSocketClient != null) {
            if (!webSocketClient.isOpen()) {
                imCallBack.onError(-1, IMError.IM_MS_SEND_ERROR);
                return;
            }
            try {
                //发送消息
                webSocketClient.send(content);
                //注册回调
            } catch (Exception e) {
                imCallBack.onError(-1, IMError.IM_MS_SEND_ERROR);
                return;
            }
            callBackHashtable.put(imCallBack.getCallbackId(), imCallBack);
            //超时判断
            Message message = new Message();
            message.what = CHECK_CALL_BACK_TIMEOUT;
            message.obj = imCallBack.getCallbackId();
            socketActionHandler.sendMessageDelayed(message, TIMEOUT_TIME);
        } else {
            imCallBack.onError(-1, IMError.IM_MS_SEND_ERROR);
        }
    }

    private Proxy proxy = Proxy.NO_PROXY;

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }
}
