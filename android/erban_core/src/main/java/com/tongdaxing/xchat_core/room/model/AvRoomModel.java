package com.tongdaxing.xchat_core.room.model;

import android.text.TextUtils;

import com.netease.nimlib.sdk.NIMChatRoomSDK;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.chatroom.constant.MemberQueryType;
import com.netease.nimlib.sdk.chatroom.constant.MemberType;
import com.netease.nimlib.sdk.util.Entry;
import com.orhanobut.logger.Logger;
import com.tongdaxing.erban.libcommon.listener.CallBack;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.bean.IMChatRoomMember;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.manager.IMNetEaseManager;
import com.tongdaxing.xchat_core.manager.ReUsedSocketManager;
import com.tongdaxing.xchat_core.manager.RoomEvent;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.tongdaxing.xchat_framework.im.IMProCallBack;
import com.tongdaxing.xchat_framework.im.IMReportBean;
import com.tongdaxing.xchat_framework.util.util.Json;
import com.tongdaxing.xchat_framework.util.util.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * <p> 房间网络数据操作 </p>
 *
 * @author jiahui
 * @date 2017/12/11
 */
public class AvRoomModel extends RoomBaseModel {

    public static final String EXPER_LEVEL = "experLevel";
    public static final String USER_NICK = "user_nick";
    public static final String USER_CAR = "user_car";
    public static final String USER_CAR_NAME = "user_car_name";
    public static final String IS_NEW_USER = "is_new_user";
    private static final String ROOM_ID = "room_id";

    public AvRoomModel() {

    }

    public static final int PUBLIC_CHAT_ROOM_TYPE = 1;
    public static final String PUBLIC_CHAT_ROOM = "PUBLIC_CHAT_ROOM";


    /**
     * 离开聊天室（退出房间）
     */
    public void quiteRoom(String roomId) {
        if (!TextUtils.isEmpty(roomId)) {
            AvRoomDataManager.get().release();
            //云信的房间退出
            LogUtils.d("nim_sdk", "exitChatRoom_1");

        }
    }

    /**
     * 退出房间
     * 2.4.5后添加退出麦序的逻辑
     */

    //: 2018/4/28 逻辑好乱记得改
    public void exitRoom(CallBack<String> callBack) {
        //: 2018/11/2 im exitRoom

        RoomInfo currentRoom = AvRoomDataManager.get().mCurrentRoomInfo;

        if (currentRoom == null) {
            if (callBack != null) {
                callBack.onFail(0, "");
            }
            return;
        }

        quitUserRoom(callBack, currentRoom);
    }

    private void quitUserRoom(final CallBack<String> callBack, RoomInfo currentRoom) {
        String roomId = String.valueOf(currentRoom.getRoomId());

        quiteRoom(roomId);

        ReUsedSocketManager.get().exitRoom(currentRoom.getRoomId(),new IMProCallBack() {
            @Override
            public void onSuccessPro(IMReportBean imReportBean) {
                if (imReportBean.getReportData().errno == 0) {
                    IMNetEaseManager.get().getChatRoomEventObservable()
                            .onNext(new RoomEvent().setEvent(RoomEvent.ROOM_EXIT));
                    if (callBack != null) {
                        callBack.onSuccess("");
                    }

                } else {
                    if (callBack != null) {
                        callBack.onFail(0, "");
                    }
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                callBack.onFail(0, "");
            }
        });

        long uid = CoreManager.getCore(IAuthCore.class).getCurrentUid();

        Logger.i("quitUserRoom 离开房间id:" + String.valueOf(uid));
        quitUserRoom(String.valueOf(uid), CoreManager.getCore(IAuthCore.class).getTicket());
    }

    /**
     * 获取房间队列信息
     *
     * @param roomId
     */
    public Observable<List<Entry<String, String>>> queryRoomMicInfo(final String roomId) {
        return Observable.create(new ObservableOnSubscribe<List<Entry<String, String>>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Entry<String, String>>> e) throws Exception {
                executeNIMClient(NIMClient.syncRequest(NIMChatRoomSDK.getChatRoomService()
                        .fetchQueue(roomId)), e);
            }
        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io());
    }

    public void quitUserRoom(String uId, String ticket) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", uId);
        params.put("ticket", ticket);

        OkHttpManager.getInstance().doPostRequest(UriProvider.userRoomOut(), params, new OkHttpManager.MyCallBack<ServiceResult<String>>() {

            @Override
            public void onError(Exception e) {
                Logger.i("quitUserRoom 通知服务端退出房间失败:" + e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<String> data) {
                if (data != null) {
                    if (data.isSuccess()) {
                        Logger.i("quitUserRoom 通知服务端退出房间成功:" + data);
                    } else {
                        Logger.i("quitUserRoom 通知服务端退出房间失败:" + data);
                    }
                }
            }
        });
    }

    public void userRoomIn(String uid, long roomId) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(uid));
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        params.put("roomUid", roomId + "");

        OkHttpManager.getInstance().doPostRequest(UriProvider.userRoomIn(), params, new OkHttpManager.MyCallBack<Json>() {

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(Json response) {

            }
        });
    }

    //: 2018/11/6  im 1
    public void getNormalChatMember(String roomId, final long currentUid) {
        NIMChatRoomSDK.getChatRoomService()
                .fetchRoomMembers(roomId, MemberQueryType.NORMAL, 0, 500)
                .setCallback(new RequestCallbackWrapper<List<com.netease.nimlib.sdk.chatroom.model.ChatRoomMember>>() {
                    @Override
                    public void onResult(int i, List<com.netease.nimlib.sdk.chatroom.model.ChatRoomMember> chatRoomMemberList, Throwable throwable) {
                        if (ListUtils.isListEmpty(chatRoomMemberList)) {
                            return;
                        }
                        AvRoomDataManager.get().clearMembers();
                        List<IMChatRoomMember> chatRoomMembers = new ArrayList<>();
                        for (com.netease.nimlib.sdk.chatroom.model.ChatRoomMember chatRoomMember : chatRoomMemberList) {
                            IMChatRoomMember member = new IMChatRoomMember(chatRoomMember);
                            if (Objects.equals(chatRoomMember.getAccount(), String.valueOf(currentUid))) {
                                //自己
                                AvRoomDataManager.get().mOwnerMember = member;
                            }
                            if (chatRoomMember.getMemberType() == MemberType.ADMIN) {
                                AvRoomDataManager.get().addAdminMember(member);
                            }
                            if (chatRoomMember.getMemberType() == MemberType.CREATOR) {
                                AvRoomDataManager.get().mRoomCreateMember = member;
                            }
                            chatRoomMembers.add(member);
                        }
                        AvRoomDataManager.get().mRoomFixedMemberList.addAll(chatRoomMembers);
                        AvRoomDataManager.get().mRoomAllMemberList.addAll(chatRoomMembers);
                        Logger.i("进入房间获取固定成员成功,人数:" + chatRoomMemberList.size());
                    }
                });
    }

    /**
     * 获取用户新手推荐
     */
    public void newUserRecommend(OkHttpManager.MyCallBack callBack) {

        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));

        getRequest(UriProvider.getNewUserRecommend(), params, callBack);
    }

    /**
     * 获取绑定手机
     */
    public void isPhones(OkHttpManager.MyCallBack<ServiceResult> callBack) {
        Map<String, String> param = CommonParamUtil.getDefaultParam();
        param.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));

        getRequest(UriProvider.isPhones(), param, callBack);
    }


}
