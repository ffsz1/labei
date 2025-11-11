package com.tongdaxing.xchat_core.manager;

import android.support.annotation.NonNull;

import com.blankj.utilcode.util.AppUtils;
import com.google.gson.Gson;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.tongdaxing.xchat_core.bean.ChatRoomMessage;
import com.tongdaxing.xchat_core.bean.IMChatRoomMember;
import com.tongdaxing.xchat_core.bean.RoomMicInfo;
import com.tongdaxing.xchat_core.bean.RoomQueueInfo;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_framework.im.ICommonListener;
import com.tongdaxing.xchat_framework.im.IConnectListener;
import com.tongdaxing.xchat_framework.im.IMCallBack;
import com.tongdaxing.xchat_framework.im.IMError;
import com.tongdaxing.xchat_framework.im.IMErrorBean;
import com.tongdaxing.xchat_framework.im.IMKey;
import com.tongdaxing.xchat_framework.im.IMModelFactory;
import com.tongdaxing.xchat_framework.im.IMProCallBack;
import com.tongdaxing.xchat_framework.im.IMReportBean;
import com.tongdaxing.xchat_framework.im.IMSendRoute;
import com.tongdaxing.xchat_framework.util.util.Json;
import com.tongdaxing.xchat_framework.util.util.LogUtils;
import com.tongdaxing.xchat_framework.util.util.StringUtils;
import com.tongdaxing.xchat_framework.util.util.json.JsonParser;

import org.java_websocket.handshake.ServerHandshake;

import java.net.Proxy;
import java.util.List;


/**
 *
 */
public class ReUsedSocketManager {

    public static String TAG = "ReUsedSocketManager";
    private ICommonListener iCommonListener = null;
    private SocketManager socketManager = null;
    private static ReUsedSocketManager reUsedSocketManager = null;
    //发送
    public static final int SEND_ERROR = 0;
    private IConnectListener connectListener;
    private SocketManager.IMNoticeMsgListener imNoticeMsgListener;

    private Proxy proxy = Proxy.NO_PROXY;

    private void imLogin(final String uid, final String ticket) {
        LogUtil.d(AvRoomDataManager.TAG, "imLogin ---> uid = " + uid);
        IMProCallBack imProCallBack = new IMProCallBack() {
            @Override
            public void onSuccessPro(IMReportBean imReportBean) {
                int errno = imReportBean.getReportData().errno;
                LogUtil.d(AvRoomDataManager.TAG, "imLogin ---> errno = " + errno);
                if (errno != 0) {
                    if (!ReUsedSocketManager.get().isConnect()) {
                        ReUsedSocketManager.get().connect(connectListener, 3000); //登录失败而且socket断开状态重连
                    } else {
                        if (errno != IMError.IM_ERROR_LOGIN_AUTH_FAIL && errno != IMError.IM_ERROR_GET_USER_INFO_FAIL) {
                            imLogin(uid, ticket);
                        }
                        //TODO 暂定上面这两个原因直接退出登录
                        if (imNoticeMsgListener != null)
                            imNoticeMsgListener.onLoginError(errno, imReportBean.getReportData().errmsg);
                    }
                } else {
                    if (imNoticeMsgListener != null) {
                        imNoticeMsgListener.onDisConntectIMLoginSuc();
                    }
                    onImLoginRenewEnterRoom();
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                LogUtil.d(AvRoomDataManager.TAG, "imLogin ---> errorCode = " + errorCode);
            }
        };


        ReUsedSocketManager.get().send(IMModelFactory.get().createLoginModel(ticket, uid), imProCallBack);
    }

    /**
     * im登录之后如果再房间，需要重新进入房间
     */
    private void onImLoginRenewEnterRoom() {
        LogUtil.d(AvRoomDataManager.TAG, "onImLogin ---> enterRoom");
        if (AvRoomDataManager.get().mCurrentRoomInfo != null) {
            enterRoom(AvRoomDataManager.get().mCurrentRoomInfo, new IMProCallBack() {
                @Override
                public void onSuccessPro(IMReportBean imReportBean) {
                    LogUtil.d(AvRoomDataManager.TAG, "onImLogin ---> onReconnection");
                    if (imNoticeMsgListener != null)
                        imNoticeMsgListener.onDisConnectEnterRoomSuc();
                }

                @Override
                public void onError(int errorCode, String errorMsg) {
                    LogUtil.d(AvRoomDataManager.TAG, "onImLogin ---> enterRoom");
                }
            });
        }
    }

    private void setServerMicInfo(String room_info, String member, List<Json> queueList) {
        Gson gson = new Gson();
        RoomInfo extRoomInfo = gson.fromJson(room_info, RoomInfo.class);
        IMChatRoomMember chatRoomMember = gson.fromJson(member, IMChatRoomMember.class);
        if (chatRoomMember != null)
            AvRoomDataManager.get().mOwnerMember = chatRoomMember;
        //同步房间信息
        AvRoomDataManager.get().mCurrentRoomInfo = extRoomInfo;
        //同步麦序
        for (int i = 0; i < queueList.size(); i++) {
            Json json = queueList.get(i);
            int key = json.num("key");
            Json value = json.json_ok("value");
            IMChatRoomMember imChatRoomMember = null;
            if (value.has("member")) {
                imChatRoomMember = gson.fromJson(value.str("member"), IMChatRoomMember.class);
            }
            RoomMicInfo micInfo = gson.fromJson(value.str("mic_info"), RoomMicInfo.class);
            RoomQueueInfo roomQueueInfo = new RoomQueueInfo(micInfo, imChatRoomMember);
            AvRoomDataManager.get().mMicQueueMemberMap.put(key, roomQueueInfo);
        }
    }


    //-------------------------------------------对外开放的方法-------------------------------------------------------------------


    public static ReUsedSocketManager get() {
        if (reUsedSocketManager == null) {
            synchronized (ReUsedSocketManager.class) {
                if (reUsedSocketManager == null) {
                    reUsedSocketManager = new ReUsedSocketManager();
                }
            }
        }
        return reUsedSocketManager;
    }


    /**
     * 注册断开回调函数  告诉业务层断开( code 告诉我们时超时导致还是网络导致断开还是手动断开)
     * 注册服务器单向推消息处理回调
     *
     * @param iCommonListener
     */
    public void setiCommonListener(ICommonListener iCommonListener) {
        this.iCommonListener = iCommonListener;
        if (iCommonListener != null && socketManager != null)
            socketManager.setiCommonListener(iCommonListener);
    }

    /**
     * 手动断开链接
     */
    public void disconnect() {
        if (socketManager != null) socketManager.disconnect();
    }

    public void destroy() {
        if (socketManager != null) socketManager.destroy();
    }


    /**
     * @return 链接状态
     */
    public boolean isConnect() {
        if (socketManager != null && socketManager.isConnect()) return true;
        return false;

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
        if (socketManager != null)
            socketManager.destroy();
        socketManager = new SocketManager();
        if (!AppUtils.isAppDebug()){
            socketManager.setProxy(proxy);//设置代理要在connect之前
        }
        if (iCommonListener != null) socketManager.setiCommonListener(iCommonListener);
        return socketManager.connect(iConnectListener, delay);
    }


    public void setImNoticeMsgListener(SocketManager.IMNoticeMsgListener imNoticeMsgListener) {


        this.imNoticeMsgListener = imNoticeMsgListener;

    }

    /**
     * 对服务器发送消息
     *
     * @param content    发送内容
     * @param imCallBack 结果的回调
     * @return
     */
    public void send(String content, @NonNull IMCallBack imCallBack) {
        if (socketManager != null) {
            socketManager.send(content, imCallBack);
        } else {
            imCallBack.onError(-1, IMError.IM_MS_SEND_ERROR);
        }
    }


    /**
     * 对服务器发送消息
     *
     * @param content    发送内容
     * @param imCallBack 结果的回调
     * @return
     */
    public void send(Json content, @NonNull IMCallBack imCallBack) {
        if (!content.has("id")) {
            content.set("id", imCallBack.getCallbackId());
        }
        LogUtils.d("request_info_im_send", content.toString());
        if (socketManager != null) {
            socketManager.send(content.toString(), imCallBack);
        } else {
            imCallBack.onError(-1, IMError.IM_MS_SEND_ERROR);
        }
    }


    /**
     * 登录回调之后初始化im，并登录
     *
     * @param uid
     * @param ticket
     */
    public void initIM(final String uid, final String ticket) {
        final int reConnectTIme = 3000;
        connectListener = new IConnectListener() {
            @Override
            public void onSuccess(ServerHandshake serverHandshake) {
                LogUtil.d(AvRoomDataManager.TAG, "initIM ---> onOpen ---> HttpStatus = " + serverHandshake.getHttpStatus() + " HttpStatusMessage = " + serverHandshake.getHttpStatusMessage());
                imLogin(uid, ticket);
            }

            @Override
            public void onError(Exception e) {
                LogUtil.d(AvRoomDataManager.TAG, "initIM ---> onError ---> Exception = " + e.getMessage());
                LogUtil.d(AvRoomDataManager.TAG, e.getMessage());
                if (e.getMessage() == "Dubble connect!")
                    return;
                ReUsedSocketManager.get().connect(connectListener, reConnectTIme); //断线重连延迟3秒执行
            }
        };
        ReUsedSocketManager.get().connect(connectListener, 0);
        ReUsedSocketManager.get().setiCommonListener(new ICommonListener() {
            @Override
            public void onDisconnectCallBack(IMErrorBean err) {
                boolean isCloseSelf = err.getCloseReason() == SocketManager.CALL_BACK_CODE_SELFCLOSE;
                LogUtil.d(AvRoomDataManager.TAG, "initIM ---> onDisconnectCallBack ---> isCloseSelf = " + isCloseSelf + " err_code = " + err.getCode() + " reason = " + err.getReason());
                if (!isCloseSelf) { // 非手动关闭自动重连
                    ReUsedSocketManager.get().connect(connectListener, reConnectTIme); // 断线重连延迟5秒执行
                }
                if (imNoticeMsgListener != null)
                    imNoticeMsgListener.onDisConnection(isCloseSelf);

            }

            @Override
            public void onNoticeMessage(String message) {
                if (imNoticeMsgListener != null)
                    imNoticeMsgListener.onNotice(Json.parse(message));
            }
        });

    }


    /**
     * 进入房间
     *
     * @param roomInfo
     * @param imProCallBack 传到这里的闭包的成功回调不需要再判断请求是否成功
     */
    public void enterRoom(RoomInfo roomInfo, final IMProCallBack imProCallBack) {
        //我们自己服务端信息
        AvRoomDataManager.get().mCurrentRoomInfo = roomInfo;
        AvRoomDataManager.get().mServiceRoominfo = roomInfo;
        LogUtil.d(AvRoomDataManager.TAG, "enterRoom ---> send ---> roomId = " + roomInfo.getRoomId());
        ReUsedSocketManager.get().send(IMModelFactory.get().createJoinAvRoomModel(roomInfo.getRoomId()), new IMProCallBack() {
            @Override
            public void onSuccessPro(IMReportBean imReportBean) {
                IMNetEaseManager.get().setImRoomConnection(true);
                IMReportBean.ReportData reportData = imReportBean.getReportData();
                if (reportData.errno != 0) {
                    LogUtil.d(AvRoomDataManager.TAG, "enterRoom ---> send ---> onError --- > errno = " + reportData.errno);
                    imProCallBack.onError(reportData.errno, reportData.errmsg);
                    return;
                }
                Json data = reportData.data;
                String room_info = data.str("room_info");
                String member = data.str("member");
                List<Json> queueList = data.jlist("queue_list");
                //可能会解析错误
                try {
                    setServerMicInfo(room_info, member, queueList);
                } catch (Exception e) {
                    imProCallBack.onError(-1101, "");
                    return;

                }
                LogUtil.d(AvRoomDataManager.TAG, "enterRoom ---> send ---> onSuccessPro");
                imProCallBack.onSuccessPro(imReportBean);
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                LogUtil.d(AvRoomDataManager.TAG, "enterRoom ---> send ---> onError ---> errorCode = " + errorCode);
                imProCallBack.onError(errorCode, errorMsg);
            }
        });


    }

    /**
     * 更新麦序操作：加入队列  如果已经其他麦序位置上，会自动更新位置
     *
     * @param roomId
     * @param micPosition
     * @param uid
     * @param imCallBack
     */
    public void updateQueue(String roomId, int micPosition, long uid, final IMCallBack imCallBack) {
        LogUtil.d(AvRoomDataManager.TAG, "updateQueue ---> roomId = " + roomId + " --- micPosition = " + micPosition + " --- uid = " + uid);
        ReUsedSocketManager.get().send(IMModelFactory.get().createUpdateQueue(roomId, micPosition, uid), imCallBack);
    }

    /**
     * 退出房间
     *
     * @param roomId
     * @param imProCallBack
     */
    public void exitRoom(long roomId, IMProCallBack imProCallBack) {
        LogUtil.d(AvRoomDataManager.TAG, "exitRoom ---> roomId = " + roomId);
        send(IMModelFactory.get().createExitRoom(roomId), imProCallBack);
    }


    /**
     * 退出公聊房间
     *
     * @param roomId
     * @param imProCallBack
     */
    public void exitPublicRoom(long roomId, IMProCallBack imProCallBack) {
        LogUtil.d(AvRoomDataManager.TAG, "exitPublicRoom ---> roomId = " + roomId);
        send(IMModelFactory.get().createExitPublicRoom(roomId), imProCallBack);
    }

    /**
     * 更新麦序操作：加入队列  如果已经其他麦序位置上，会自动更新位置
     *
     * @param roomId
     * @param micPosition
     * @param imSendCallBack
     */
    public void pollQueue(String roomId, int micPosition, final IMSendCallBack imSendCallBack) {
        LogUtil.d(AvRoomDataManager.TAG, "updateQueue ---> roomId = " + roomId + " --- micPosition = " + micPosition);
        send(IMModelFactory.get().createPollQueue(roomId, micPosition), imSendCallBack);
    }


    /**
     * 发送文本消息
     *
     * @param roomId
     * @param message
     * @param imSendCallBack
     */
    public void sendTxtMessage(String roomId, ChatRoomMessage message, IMCallBack imSendCallBack) {
        Json json = new Json();
        json.set("room_id", roomId);
        json.set(IMKey.member, JsonParser.toJson(message.getImChatRoomMember()));
        if (StringUtils.isNotEmpty(message.getContent()))
            json.set(IMKey.content, message.getContent());
        send(IMModelFactory.get().createRequestData(IMSendRoute.sendText, json), imSendCallBack);
    }

    /**
     * 发送自定义消息
     *
     * @param roomId
     * @param message
     * @param imSendCallBack
     */
    public void sendCustomMessage(String roomId, ChatRoomMessage message, IMCallBack imSendCallBack) {
        Json json = new Json();
        json.set("room_id", roomId);
        json.set(IMKey.custom, message.getAttachment().toJson(true));
        if (message.getImChatRoomMember() != null)
            json.set(IMKey.member, JsonParser.toJson(message.getImChatRoomMember()));
        send(IMModelFactory.get().createRequestData(IMSendRoute.sendMessage, json), imSendCallBack);
    }

    /**
     * 发送自定义消息
     *
     * @param roomId
     * @param message
     * @param imSendCallBack
     */
    public void sendPulicMessage(String roomId, ChatRoomMessage message, IMCallBack imSendCallBack) {
        Json json = new Json();
        json.set("room_id", roomId);
        json.set(IMKey.custom, message.getAttachment().toJson());
//        if (message.getImChatRoomMember() != null)
//            json.set(IMKey.member, message.getImChatRoomMember());
        send(IMModelFactory.get().createRequestData(IMSendRoute.sendPublicMsg, json), imSendCallBack);
    }


    /**
     * 进入公聊大厅
     *
     * @param roomId
     * @param imSendCallBack
     */
    public void enterChatHallMessage(String roomId, IMCallBack imSendCallBack) {
        Json json = new Json();
        json.set("room_id", roomId);//todo 1.1.0版本会加个server_msg_id字段
        send(IMModelFactory.get().createRequestData(IMSendRoute.enterPublicRoom, json), imSendCallBack);
    }
}