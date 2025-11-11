package com.tongdaxing.xchat_core.room;

import android.os.Handler;
import android.os.Message;

import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomKickOutEvent;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomNotificationAttachment;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.AccountInfo;
import com.tongdaxing.xchat_core.auth.IAuthClient;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.bean.ChatRoomMessage;
import com.tongdaxing.xchat_core.home.HomeRoom;
import com.tongdaxing.xchat_core.home.TabInfo;
import com.tongdaxing.xchat_core.im.avroom.IAVRoomCore;
import com.tongdaxing.xchat_core.im.avroom.IAVRoomCoreClient;
import com.tongdaxing.xchat_core.im.room.IIMRoomCoreClient;
import com.tongdaxing.xchat_core.im.state.IPhoneCallStateClient;
import com.tongdaxing.xchat_core.im.state.PhoneCallStateCoreImpl;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.redpacket.bean.ActionDialogInfo;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.room.queue.bean.RoomConsumeInfo;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_core.utils.Logger;
import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.data.BaseConstants;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.tongdaxing.xchat_framework.util.config.BasicConfig;
import com.tongdaxing.xchat_framework.util.util.StringUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.agora.rtc.Constants;

/**
 * Created by zhouxiangfeng on 2017/5/27.
 */

public class RoomCoreImpl extends AbstractBaseCore implements IRoomCore {

    private static final String TAG = "RoomCoreImpl";
    private List<ChatRoomMessage> messages;
    private List<ChatRoomMessage> cacheMessages;
    private List<ActionDialogInfo> dialogInfo;
    private RoomInfo curRoomInfo;
    private boolean modifyMuteState;
    private int retryCount;


    private RoomCoreHandler handler = new RoomCoreHandler(this);

    public RoomCoreImpl() {
        CoreManager.addClient(this);
        messages = new ArrayList<>();
    }

    @Override
    public void setDialogInfo(List<ActionDialogInfo> dialogInfo) {
        this.dialogInfo = dialogInfo;
    }

    @Override
    public List<ActionDialogInfo> getDialogInfo() {
        return dialogInfo;
    }

    private void sendStatistics() {
        if (curRoomInfo != null) {
            long uid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
            long roomUid = curRoomInfo.getUid();
            String ticket = CoreManager.getCore(IAuthCore.class).getTicket();
            Map<String, String> params = CommonParamUtil.getDefaultParam();
            params.put("uid", String.valueOf(uid));
            params.put("roomUid", String.valueOf(roomUid));
            params.put("time", String.valueOf(System.currentTimeMillis()));
            params.put("ticket", ticket);
            OkHttpManager.getInstance().doPostRequest(UriProvider.roomStatistics(), params, new OkHttpManager.MyCallBack() {

                @Override
                public void onError(Exception e) {
                    Logger.error(TAG, e.getMessage());
                }

                @Override
                public void onResponse(Object response) {

                }
            });
        }
    }

    @Override
    public List<ChatRoomMessage> getMessages() {
        return messages;
    }

    public void addMessages(ChatRoomMessage msg) {
        boolean needClear = false;

        if (messages == null) {
            messages = new ArrayList<>();
        }

        if (cacheMessages == null) {
            cacheMessages = new ArrayList<>();
        }

        if (messages != null && messages.size() >= 400) {
            messages.removeAll(cacheMessages);
            cacheMessages.clear();
            cacheMessages.addAll(messages);
            needClear = true;
        }

        messages.add(msg);

        if (cacheMessages.size() <= 200) {
            cacheMessages.add(msg);
        }

        notifyClients(IRoomCoreClient.class, IRoomCoreClient.METHOD_ON_CURRENT_ROOM_RECEIVE_NEW_MSG, msg, needClear);
    }

    @CoreEvent(coreClientClass = IIMRoomCoreClient.class)
    public void onEnterRoom() {
        long uid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
        if (curRoomInfo.getType() == RoomInfo.ROOMTYPE_LIGHT_CHAT) {
            if (BasicConfig.INSTANCE.isDebuggable()) {
                CoreManager.getCore(IAVRoomCore.class).joinHighQualityChannel(curRoomInfo.getRoomId() + "", (int) uid, true);
            } else {
                CoreManager.getCore(IAVRoomCore.class).joinHighQualityChannel(curRoomInfo.getRoomId() + "", (int) uid, false);
            }
        } else {
            CoreManager.getCore(IAVRoomCore.class).joinChannel(curRoomInfo.getRoomId() + "", (int) uid);
        }
        if (curRoomInfo.getUid() == uid) {
            CoreManager.getCore(IAVRoomCore.class).setRole(Constants.CLIENT_ROLE_BROADCASTER);
        } else {
            CoreManager.getCore(IAVRoomCore.class).setRole(Constants.CLIENT_ROLE_AUDIENCE);
        }
    }

    @CoreEvent(coreClientClass = IIMRoomCoreClient.class)
    public void onRoomInfoUpdate(ChatRoomNotificationAttachment attachment) {
        if (curRoomInfo == null) {
            return;
        }
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(curRoomInfo.getUid()));
        params.put("visitorUid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");

        OkHttpManager.getInstance().getRequest(UriProvider.getRoomInfo(), params, new OkHttpManager.MyCallBack<ServiceResult<RoomInfo>>() {

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(ServiceResult<RoomInfo> response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        curRoomInfo = response.getData();
                        notifyClients(IRoomCoreClient.class, IRoomCoreClient.METHOD_ON_CURRENT_ROOM_INFO_UPDATE, response.getData());
                    }
                }
            }
        });
    }

    @CoreEvent(coreClientClass = IIMRoomCoreClient.class)
    public void onEnterRoomFail(int code, String error) {
        LogUtil.i(TAG, "onEnterRoomFail--->code:" + code);
        notifyClients(IRoomCoreClient.class, IRoomCoreClient.METHOD_ON_ENTER_FAIL, code, error);
        clearRoomData();
    }

    @CoreEvent(coreClientClass = IIMRoomCoreClient.class)
    public void onKickOut(ChatRoomKickOutEvent.ChatRoomKickOutReason reason) {
        notifyClients(IRoomCoreClient.class, IRoomCoreClient.METHOD_ON_BE_KICK_OUT, reason);
        CoreManager.getCore(IAVRoomCore.class).leaveChannel();
        clearRoomData();
    }

    @CoreEvent(coreClientClass = IAVRoomCoreClient.class)
    public void onJoinAVRoom() {
        retryCount = 0;
        LogUtil.i(TAG, "onJoinAVRoom--->");
        notifyClients(IRoomCoreClient.class, IRoomCoreClient.METHOD_ON_ENTER, curRoomInfo);
        // 暂时未发现调用先注释
        String content = "封面、背景及内容含低俗、引导、暴露等都会被屏蔽处理，泄露用户隐私、导流第三方平台、欺诈用户等将被封号处理。请珍惜自己的直播间哦！";
//        ChatRoomMessage message = ChatRoomMessageBuilder.createTipMessage(content);
//        message.setContent(content);
        ChatRoomMessage message = new ChatRoomMessage(curRoomInfo.getRoomId() + "", content);

        addMessages(message);

//        CoreManager.getCore(IIMRoomCore.class).queryChatRoomMembers(curRoomInfo.getRoomId()+"");

        handler.removeMessages(0);
        handler.sendEmptyMessageDelayed(0, BaseConstants.SEND_STATISTICS_INTERVAL);

        userRoomIn(curRoomInfo.getUid());
//
//        long uid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
//        if (curRoomInfo.getUid() == uid) {
//            CoreManager.getCore(IStatisticsCore.class).onEventStart(getContext(), IStatisticsCore.EVENT_OPENROOM, "开房");
//        }
//        CoreManager.getCore(IStatisticsCore.class).onEventStart(getContext(), IStatisticsCore.EVENT_ENTERROOM, "进入房间");
    }

/*    @CoreEvent(coreClientClass = IIMLoginClient.class)
    public void onKickedOut(StatusCode code) {
        if (curRoomInfo != null) {
            exitRoom();
            handler.removeMessages(0);
        }
    }*/

    @CoreEvent(coreClientClass = IPhoneCallStateClient.class)
    public void onPhoneStateChanged(PhoneCallStateCoreImpl.PhoneCallStateEnum phoneCallStateEnum) {
        if (curRoomInfo != null) {
            if (phoneCallStateEnum != PhoneCallStateCoreImpl.PhoneCallStateEnum.IDLE) {
                boolean isAudience = CoreManager.getCore(IAVRoomCore.class).isAudienceRole();
                boolean isMute = CoreManager.getCore(IAVRoomCore.class).isMute();
                if (!isAudience && !isMute) {
                    CoreManager.getCore(IAVRoomCore.class).setMute(true);
                    modifyMuteState = true;
                } else {
                    modifyMuteState = false;
                }
            } else {
                if (modifyMuteState == true) {
                    CoreManager.getCore(IAVRoomCore.class).setMute(false);
                }
//                CoreManager.getCore(IAVRoomCore.class).setSpeeker(true);
                modifyMuteState = false;
            }
        } else {
            modifyMuteState = false;
        }
    }

    private void clearRoomData() {
        LogUtil.i(TAG, "clearRoomData");
        messages.clear();
        curRoomInfo = null;
        if (dialogInfo != null) {
            dialogInfo.clear();
            dialogInfo = null;
        }
    }

    @Override
    public boolean isRoomOwner() {
        if (null != curRoomInfo) {
            return curRoomInfo.getUid() == CoreManager.getCore(IAuthCore.class).getCurrentUid();
        } else {
            return false;
        }
    }

    @Override
    public RoomInfo getCurRoomInfo() {
        return curRoomInfo;
    }

    @Override
    public void updateByAdmin(long roomUid, String title, String desc, String pwd, String label, int tagId) {
        long uid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(uid));
        params.put("roomUid", String.valueOf(roomUid));
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        params.put("tagId", String.valueOf(tagId));

        if (title != null) {
            params.put("title", title);
            if (title.equals("")) {
                UserInfo userInfo = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(uid);
                if (userInfo != null) {
                    params.put("title", userInfo.getNick() + "的房间");
                }
            }
        }

        if (desc != null) {
            params.put("roomDesc", desc);
        }
        if (pwd != null) {
            params.put("roomPwd", pwd);
        }
        if (!StringUtil.isEmpty(label)) {
            params.put("roomTag", label);
        }

        OkHttpManager.getInstance().doPostRequest(UriProvider.updateByAdimin(), params, new OkHttpManager.MyCallBack<ServiceResult<RoomInfo>>() {

            @Override
            public void onError(Exception e) {
                Logger.error(TAG, e.getMessage());
                notifyClients(IRoomCoreClient.class, IRoomCoreClient.METHOD_ON_UPDATE_ROOM_INFO_FAIL, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<RoomInfo> response) {
                if (response.isSuccess()) {
                    notifyClients(IRoomCoreClient.class, IRoomCoreClient.METHOD_ON_UPDATE_ROOM_INFO, response.getData());
                } else {
                    notifyClients(IRoomCoreClient.class, IRoomCoreClient.METHOD_ON_UPDATE_ROOM_INFO_FAIL, response.getMessage());
                }
            }
        });
    }

    @Override
    public void updateRoomInfo(String title, String desc, String pwd, String label, int tagId) {
        long uid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(uid));
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        params.put("tagId", String.valueOf(tagId));

        if (title != null) {
            params.put("title", title);
            if (title.equals("")) {
                UserInfo userInfo = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(uid);
                if (userInfo != null) {
                    params.put("title", userInfo.getNick() + "的房间");
                }
            }
        }

        if (desc != null) {
            params.put("roomDesc", desc);
        }
        if (pwd != null) {
            params.put("roomPwd", pwd);
        }
        if (!StringUtil.isEmpty(label)) {
            params.put("roomTag", label);
        }

        OkHttpManager.getInstance().doPostRequest(UriProvider.updateRoomInfo(), params, new OkHttpManager.MyCallBack<ServiceResult<RoomInfo>>() {

            @Override
            public void onError(Exception e) {
                Logger.error(TAG, e.getMessage());
                notifyClients(IRoomCoreClient.class, IRoomCoreClient.METHOD_ON_UPDATE_ROOM_INFO_FAIL, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<RoomInfo> response) {
                if (response.isSuccess()) {
                    notifyClients(IRoomCoreClient.class, IRoomCoreClient.METHOD_ON_UPDATE_ROOM_INFO, response.getData());
                } else {
                    notifyClients(IRoomCoreClient.class, IRoomCoreClient.METHOD_ON_UPDATE_ROOM_INFO_FAIL, response.getMessage());
                }
            }
        });
    }

    @Override
    public void sendRoomTextMsg(String str) {
        if (curRoomInfo != null && !StringUtils.isEmpty(str)) {
            // 没找到调用位置，考虑删除
//
////            ChatRoomMessage message = ChatRoomMessageBuilder.createChatRoomTextMessage(
////                    curRoomInfo.getRoomId() + "", // 聊天室id
////                    str // 文本内容
////            );
//
//            ChatRoomMessage message = new ChatRoomMessage(curRoomInfo.getRoomId()+"",str);
//            CoreManager.getCore(IIMRoomCore.class).sendMessage(message);
//            addMessages(message);
        }
    }

    @Override
    public void openRoom(long uid, int type) {
        openRoom(uid, type, null, null, null, null);
    }

    @Override
    public void openRoom(final long uid, final int type, final String title, final String roomDesc,
                         final String backPic, final String rewardId) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(uid));
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        params.put("type", String.valueOf(type));
        params.put("roomPwd", "");
//        params.put("roomTag", "聊天");

        if (!StringUtil.isEmpty(title)) {
            params.put("title", title);
        } else {
            UserInfo userInfo = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(uid);
            if (userInfo != null) {
                params.put("title", userInfo.getNick() + "的房间");
            }
        }
        if (!StringUtil.isEmpty(roomDesc)) {
            params.put("roomDesc", roomDesc);
        } else {
            params.put("roomDesc", "");
        }
        if (!StringUtil.isEmpty(backPic)) {
            params.put("backPic", backPic);
        } else {
            params.put("backPic", "");
        }

        OkHttpManager.getInstance().doPostRequest(UriProvider.openRoom(), params, new OkHttpManager.MyCallBack<ServiceResult<RoomInfo>>() {

            @Override
            public void onError(Exception e) {
                Logger.error(TAG, e.getMessage());
                notifyClients(IRoomCoreClient.class, IRoomCoreClient.METHOD_ON_OPEN_ROOM_FAIL, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<RoomInfo> response) {
                if (null != response) {
                    if (response.isSuccess()) {
                        notifyClients(IRoomCoreClient.class, IRoomCoreClient.METHOD_ON_OPEN_ROOM, response.getData());
                    } else {
                        if (response.getCode() == 1500) {
                            notifyClients(IRoomCoreClient.class, IRoomCoreClient.METHOD_ON_ALREADY_OPENED_ROOM);
                        } else {
                            notifyClients(IRoomCoreClient.class, IRoomCoreClient.METHOD_ON_OPEN_ROOM_FAIL, response.getMessage());
                        }
                    }
                } else {
                    notifyClients(IRoomCoreClient.class, IRoomCoreClient.METHOD_ON_OPEN_ROOM_FAIL, "网络异常，请稍后");

                }
            }
        });
    }

    @Override
    public void requestRoomInfo(long uid, final int pageType) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(uid));
        params.put("visitorUid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");

        OkHttpManager.getInstance().getRequest(UriProvider.getRoomInfo(), params, new OkHttpManager.MyCallBack<ServiceResult<RoomInfo>>() {

            @Override
            public void onError(Exception e) {
                Logger.error(TAG, e.getMessage());
                LogUtil.i(AvRoomDataManager.TAG, "requestRoomInfo ---> onError:" + e.getMessage());
                notifyClients(IRoomCoreClient.class, IRoomCoreClient.METHOD_ON_GET_ROOM_INFO_FAIL, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<RoomInfo> response) {
                if (response != null) {
                    LogUtil.i(TAG, "requestRoomInfo ---> response:" + response.getCode());
                    if (response.isSuccess()) {
                        notifyClients(IRoomCoreClient.class, IRoomCoreClient.METHOD_ON_GET_ROOM_INFO, response.getData(), pageType);
                    } else {
                        notifyClients(IRoomCoreClient.class, IRoomCoreClient.METHOD_ON_GET_ROOM_INFO_FAIL, response.getCode(), response.getMessage(), pageType);
                    }
                } else {
                    notifyClients(IRoomCoreClient.class, IRoomCoreClient.METHOD_ON_GET_ROOM_INFO_FAIL, -1, response.getMessage(), pageType);
                }
            }
        });
    }

    @Override
    public void closeRoomInfo(String uid) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(uid));
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());

        OkHttpManager.getInstance().doPostRequest(UriProvider.closeRoom(), params, new OkHttpManager.MyCallBack<ServiceResult>() {

            @Override
            public void onError(Exception e) {
                Logger.error(TAG, e.getMessage());
                notifyClients(IRoomCoreClient.class, IRoomCoreClient.METHOD_ON_CLOSE_ROOM_INFO_FAIL, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult response) {
                if (null != response) {
                    if (response.isSuccess()) {
                        notifyClients(IRoomCoreClient.class, IRoomCoreClient.METHOD_ON_CLOSE_ROOM_INFO);
                    } else {
                        notifyClients(IRoomCoreClient.class, IRoomCoreClient.METHOD_ON_CLOSE_ROOM_INFO_FAIL, response.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public void getRoomConsumeList(long roomUid) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(roomUid));

        OkHttpManager.getInstance().getRequest(UriProvider.getRoomConsumeList(), params, new OkHttpManager.MyCallBack<ServiceResult<List<RoomConsumeInfo>>>() {

            @Override
            public void onError(Exception e) {
                Logger.error(TAG, e.getMessage());
                notifyClients(IRoomCoreClient.class, IRoomCoreClient.METHOD_ON_GET_ROOM_CONSUME_LIST_FAIL, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<List<RoomConsumeInfo>> response) {
                if (null != response) {
                    if (response.isSuccess()) {
                        notifyClients(IRoomCoreClient.class, IRoomCoreClient.METHOD_ON_GET_ROOM_CONSUME_LIST, response.getData());
                    } else {
                        notifyClients(IRoomCoreClient.class, IRoomCoreClient.METHOD_ON_GET_ROOM_CONSUME_LIST_FAIL, response.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public void roomSearch(String key) {
        if (!StringUtil.isEmpty(key)) {
            Map<String, String> params = CommonParamUtil.getDefaultParam();
            params.put("key", key);

            OkHttpManager.getInstance().getRequest(UriProvider.roomSearch(), params, new OkHttpManager.MyCallBack<ServiceResult<List<HomeRoom>>>() {

                @Override
                public void onError(Exception e) {
                    Logger.error(TAG, e.getMessage());
                    notifyClients(IRoomCoreClient.class, IRoomCoreClient.METHOD_ON_SEARCH_ROOM_FAIL, e.getMessage());
                }

                @Override
                public void onResponse(ServiceResult<List<HomeRoom>> response) {
                    if (null != response) {
                        if (response.isSuccess()) {
                            notifyClients(IRoomCoreClient.class, IRoomCoreClient.METHOD_ON_SEARCH_ROOM, response.getData());
                        } else {
                            notifyClients(IRoomCoreClient.class, IRoomCoreClient.METHOD_ON_SEARCH_ROOM_FAIL, response.getMessage());
                        }
                    }
                }
            });
        }
    }

    @Override
    public void getUserRoom(long uid) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(uid));

        OkHttpManager.getInstance().getRequest(UriProvider.getUserRoom(), params, new OkHttpManager.MyCallBack<ServiceResult<RoomInfo>>() {

            @Override
            public void onError(Exception e) {
                Logger.error(TAG, e.getMessage());
                notifyClients(IRoomCoreClient.class, IRoomCoreClient.METHOD_ON_GET_USER_ROOM_FAIL, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<RoomInfo> response) {
                if (response.isSuccess()) {
                    RoomInfo data = response.getData();
                    if (data.getUid() == 0) {
                        data.setP2pUid(uid);
                    }
                    notifyClients(IRoomCoreClient.class, IRoomCoreClient.METHOD_ON_GET_USER_ROOM, data);
                } else {
                    notifyClients(IRoomCoreClient.class, IRoomCoreClient.METHOD_ON_GET_USER_ROOM_FAIL, response.getMessage());
                }
            }
        });
    }

    @Override
    public void userRoomIn(long roomUid) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        long uid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
        params.put("uid", String.valueOf(uid));
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        params.put("roomUid", String.valueOf(roomUid));

        OkHttpManager.getInstance().doPostRequest(UriProvider.userRoomIn(), params, new OkHttpManager.MyCallBack<ServiceResult>() {

            @Override
            public void onError(Exception e) {
                Logger.error(TAG, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult response) {
                if (null != response) {
                    if (response.isSuccess()) {
                        notifyClients(IRoomCoreClient.class, IRoomCoreClient.METHOD_ON_USER_ROOM_IN);
                    }
                }
            }
        });
    }

    @Override
    public void userRoomOut() {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        long uid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
        params.put("uid", String.valueOf(uid));
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());

        OkHttpManager.getInstance().doPostRequest(UriProvider.userRoomOut(), params, new OkHttpManager.MyCallBack<ServiceResult>() {

            @Override
            public void onError(Exception e) {
                Logger.error(TAG, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult response) {
                if (null != response) {
                    if (response.isSuccess()) {
                        notifyClients(IRoomCoreClient.class, IRoomCoreClient.METHOD_ON_USER_ROOM_OUT);
                    }
                }
            }
        });
    }

    @Override
    public void getRoomTagList() {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());

        OkHttpManager.getInstance().doPostRequest(UriProvider.getRoomTagList(), params, new OkHttpManager.MyCallBack<ServiceResult<List<TabInfo>>>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IRoomCoreClient.class, IRoomCoreClient.METHOD_ROOM_GET_TAG_ERROR, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<List<TabInfo>> response) {
                if (null != response) {
                    if (response.isSuccess()) {
                        notifyClients(IRoomCoreClient.class, IRoomCoreClient.METHOD_ROOM_GET_TAG, response.getData());
                    } else {
                        notifyClients(IRoomCoreClient.class, IRoomCoreClient.METHOD_ROOM_GET_TAG_ERROR, response.getError());
                    }
                }
            }
        });
    }

    static class RoomCoreHandler extends Handler {
        WeakReference<RoomCoreImpl> roomCoreImpl;

        public RoomCoreHandler(RoomCoreImpl roomCoreImpl) {
            this.roomCoreImpl = new WeakReference<>(roomCoreImpl);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (roomCoreImpl == null || roomCoreImpl.get() == null)
                return;
            roomCoreImpl.get().sendStatistics();
            sendEmptyMessageDelayed(0, BaseConstants.SEND_STATISTICS_INTERVAL);
        }
    }
}
