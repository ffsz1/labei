package com.tongdaxing.xchat_core.room.publicchatroom;

import android.text.TextUtils;

import com.netease.nimlib.sdk.NIMChatRoomSDK;
import com.tongdaxing.xchat_core.bean.ChatRoomMessage;
import com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachParser;
import com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.PublicChatRoomAttachment;
import com.tongdaxing.xchat_core.manager.IMNetEaseManager;
import com.tongdaxing.xchat_core.manager.ReUsedSocketManager;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.im.IMProCallBack;
import com.tongdaxing.xchat_framework.im.IMReportBean;
import com.tongdaxing.xchat_framework.im.IMReportRoute;
import com.tongdaxing.xchat_framework.util.config.BasicConfig;
import com.tongdaxing.xchat_framework.util.util.Json;
import com.tongdaxing.xchat_framework.util.util.LogUtils;
import com.tongdaxing.xchat_framework.util.util.SpUtils;

import java.util.List;

import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_SUB_PUBLIC_CHAT_ROOM;


/**
 * Created by Administrator on 2018/3/20.
 */

public class PublicChatRoomController extends AbstractBaseCore {

    public static long devRoomId = 2;//测试公聊
    public static long formalRoomId = 4;//线上
    public static long checkDevRoomId = 1;//线下审核
    public static long checkRoomId = 3;//线上审核
    private long roomId = formalRoomId;

//    public List<ChatRoomMessage> messages;

    public long cacheTime = 0;
    private String cacheNameKey = "cacheNameKey";

    public PublicChatRoomController() {
//        messages = new ArrayList<>();
        if (BasicConfig.INSTANCE.isDebuggable()) {
            roomId = devRoomId;
        }

    }

    public void enterRoom(final ActionCallBack actionCallBack) {
        //适配云信乱发下麦消息的坑
//        ReUsedSocketManager.get().send()
        ReUsedSocketManager.get().enterChatHallMessage(roomId + "", new IMProCallBack() {
            @Override
            public void onSuccessPro(IMReportBean imReportBean) {
                if (imReportBean != null && imReportBean.getReportData().errno == 0){
                    actionCallBack.success();
                    Json str = imReportBean.getReportData().data;
                    List<Json> his_list = str.jlist("his_list");
                    for (int i = 0; i< his_list.size();i++){
                        ChatRoomMessage message = new ChatRoomMessage();
                        message.setRoute(IMReportRoute.sendPublicMsgNotice);
                        String custom = his_list.get(i).toString();
                        IMCustomAttachment IMCustomAttachment = IMCustomAttachParser.parse(custom);
                        if (IMCustomAttachment == null) return;
                        message.setAttachment(IMCustomAttachment);
                        IMNetEaseManager.get().addMessagesImmediately(message);
                    }
                }else {
                    // 登录失败
                    actionCallBack.error();
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                // 登录失败
                actionCallBack.error();
            }
        });
    }


    public void leaveRoom() {
        LogUtils.d("nim_sdk", "exitChatRoom_3");
        NIMChatRoomSDK.getChatRoomService().exitChatRoom(roomId + "");
    }

    public void inintCacheTime() {
        cacheTime = (long) SpUtils.get(getContext(), cacheNameKey, 0L);

    }


    public void refreshTime() {
        cacheTime = System.currentTimeMillis();
        SpUtils.put(getContext(), cacheNameKey, cacheTime);
    }


    public interface ActionCallBack {
        void success();

        void error();
    }


    public void sendMsg(String content) {
        if (TextUtils.isEmpty(content)) return;
        UserInfo userInfo = CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo();
        PublicChatRoomAttachment publicChatRoomAttachment = new PublicChatRoomAttachment(CUSTOM_MSG_SUB_PUBLIC_CHAT_ROOM, CUSTOM_MSG_SUB_PUBLIC_CHAT_ROOM);
        publicChatRoomAttachment.setMsg(content);
        Json json = new Json();
        json.set("avatar", userInfo.getAvatar());
        json.set("uid", userInfo.getUid());
        json.set("charmLevel", userInfo.getCharmLevel());
        json.set("nick", userInfo.getNick());
        final ChatRoomMessage message = new ChatRoomMessage();
        message.setRoom_id(roomId+"");
        message.setAttachment(publicChatRoomAttachment);
        ReUsedSocketManager.get().sendPulicMessage(roomId+"", message, new IMProCallBack() {
            @Override
            public void onSuccessPro(IMReportBean imReportBean) {
                if (imReportBean.getReportData().errno == 0){
//                    onSendRoomMessageSuccess(message);
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {

            }
        });
    }

    private void onSendRoomMessageSuccess(ChatRoomMessage chatRoomMessage) {
        IMNetEaseManager.get().addMessagesImmediately(chatRoomMessage);
    }

    public void setRoomId(boolean checkMode){
        if (!checkMode) {
            if (BasicConfig.INSTANCE.isDebuggable()) {
                roomId = devRoomId;
            } else {
                roomId = formalRoomId;
            }
        }else {
            if (BasicConfig.INSTANCE.isDebuggable()) {
                roomId = checkDevRoomId;
            }else {
               roomId = checkRoomId;
            }
        }
    }

}
