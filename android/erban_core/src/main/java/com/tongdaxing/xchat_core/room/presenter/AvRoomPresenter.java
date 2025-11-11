package com.tongdaxing.xchat_core.room.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.SparseArray;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomInfo;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomResultData;
import com.netease.nimlib.sdk.util.Entry;
import com.orhanobut.logger.Logger;
import com.tongdaxing.erban.libcommon.base.AbstractMvpPresenter;
import com.tongdaxing.erban.libcommon.listener.CallBack;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.bean.IMChatRoomMember;
import com.tongdaxing.xchat_core.bean.RoomMicInfo;
import com.tongdaxing.xchat_core.bean.RoomQueueInfo;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.manager.OnLoginCompletionListener;
import com.tongdaxing.xchat_core.manager.ReUsedSocketManager;
import com.tongdaxing.xchat_core.manager.RtcEngineManager;
import com.tongdaxing.xchat_core.redpacket.bean.ActionDialogInfo;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.room.model.AvRoomModel;
import com.tongdaxing.xchat_core.room.view.IAvRoomView;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.tongdaxing.xchat_framework.im.IMError;
import com.tongdaxing.xchat_framework.im.IMProCallBack;
import com.tongdaxing.xchat_framework.im.IMReportBean;
import com.tongdaxing.xchat_framework.im.IMReportResult;
import com.tongdaxing.xchat_framework.util.config.BasicConfig;
import com.tongdaxing.xchat_framework.util.config.SpEvent;
import com.tongdaxing.xchat_framework.util.util.Json;
import com.tongdaxing.xchat_framework.util.util.LogUtils;
import com.tongdaxing.xchat_framework.util.util.NetworkUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;

/**
 * <p>  </p>
 *
 * @author jiahui
 * @date 2017/12/11
 */
public class AvRoomPresenter extends AbstractMvpPresenter<IAvRoomView> {

    private final AvRoomModel mAvRoomModel;

    private static final String TAG = "LoginRoom";

    public AvRoomModel getmAvRoomModel() {
        return mAvRoomModel;
    }


    private final Gson mGson;
    private Disposable mGetOnlineNumberDisposable;
    public static final String GET_ROOM_FROM_IMNET_ERROR = "-1101";

    public AvRoomPresenter() {
        mAvRoomModel = new AvRoomModel();
        mGson = new Gson();
    }

    /**
     * 进入云信聊天室回调
     */
    public void enterRoom(final RoomInfo roomInfo) {
        if (roomInfo == null) {
            if (getMvpView() != null)
                getMvpView().showFinishRoomView();
            return;
        }
        final RoomInfo currentRoom = AvRoomDataManager.get().mCurrentRoomInfo;
        LogUtil.d(AvRoomDataManager.TAG, "enterRoom");
        //: 2018/11/2 im enterRoom
        //如果已经进入的房间要先退出房间
        if (currentRoom != null) {
            if (currentRoom.getUid() == roomInfo.getUid()) {
                return;
            }
            //进入新的房间需要先退出房间
            mAvRoomModel.exitRoom(new CallBack<String>() {
                @Override
                public void onSuccess(String data) {
                    LogUtil.d(AvRoomDataManager.TAG, "enterRoom ---> exitRoom ---> onSuccess");
                    if (getMvpView() != null) {
                        getMvpView().exitRoom(AvRoomDataManager.get().mCurrentRoomInfo);
                    }
                    enterRoomAction(roomInfo);
                }

                @Override
                public void onFail(int code, String error) {
                    LogUtil.d(AvRoomDataManager.TAG, "enterRoom ---> exitRoom ---> onFail");
                    if (getMvpView() != null) {
                        getMvpView().showFinishRoomView();
                    }
                }
            });
        } else {
            enterRoomAction(roomInfo);
        }
    }

    public void enterRoomAction(RoomInfo roomInfo) {
        LogUtil.d(AvRoomDataManager.TAG, "enterRoomAction ---> enterRoom");
        //同步的方法放到了管理类
        ReUsedSocketManager.get().enterRoom(roomInfo, new IMProCallBack() {
            @Override
            public void onSuccessPro(IMReportBean imReportBean) {
                initRtcEngine();
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                dealEnterRoomError(new Throwable(errorCode + ""));
            }
        });
    }

    /**
     * 声网appid动态获取
     */
    private void initRtcEngine() {
        LogUtil.d(AvRoomDataManager.TAG, "initRtcEngine");
        final long uid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", uid + "");
        params.put("roomId", AvRoomDataManager.get().mCurrentRoomInfo.getRoomId() + "");
        if (!NetworkUtils.isNetworkAvailable(BasicConfig.INSTANCE.getAppContext())) {
            dealEnterRoomError(new Throwable("414"));
        }
        OkHttpManager.getInstance().doPostRequest(UriProvider.getRoomAgoraKey(), params, new OkHttpManager.MyCallBack<Json>() {

            @Override
            public void onError(Exception e) {
                dealEnterRoomError(new Throwable("414"));
            }

            @Override
            public void onResponse(Json response) {
                if (response != null && !response.isNull("data")) {
                    String data = response.str("data");
                    RoomInfo curRoomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
                    if (curRoomInfo != null && getMvpView() != null) {
                        LogUtil.d(AvRoomDataManager.TAG, "initRtcEngine ---> onResponse ---> audioChannel = " + curRoomInfo.getAudioChannel());
                        if (curRoomInfo.getAudioChannel() == 0) {
                            dealEnterRoomError(new Throwable("100001100"));
                            return;
                        }
                        RtcEngineManager.get().setAudioOrganization(curRoomInfo.getAudioChannel());
                        RtcEngineManager.get().setOnLoginCompletionListener(new OnLoginCompletionListener() {

                            @Override
                            public void onLoginCompletionFail(String errorMsg) {
                                dealEnterRoomError(errorMsg);
                            }
                        });
                        RtcEngineManager.get().startRtcEngine(uid, data, curRoomInfo);
                        getMvpView().enterRoomSuccess();
                        //设置在线人数
                        if (AvRoomDataManager.get().mCurrentRoomInfo != null) {
                            getMvpView().onRoomOnlineNumberSuccess(AvRoomDataManager.get().mCurrentRoomInfo.getOnlineNum());
                        }
                    } else {
                        dealEnterRoomError(new Throwable("414"));
                    }
                } else {
                    dealEnterRoomError(new Throwable("414"));
                }
            }
        });
    }

    /**
     * 即构登录房间错误回调
     *
     * @param errorMsg 错误信息
     */
    private void dealEnterRoomError(String errorMsg) {
        exitRoom();
        if (getMvpView() != null) {
            getMvpView().enterRoomFail(-1, errorMsg);

        }
    }

    private void dealEnterRoomError(Throwable throwable) {
        throwable.printStackTrace();
        LogUtil.d(AvRoomDataManager.TAG, "dealEnterRoomError ---> Throwable = " + throwable.getMessage());
        String error;
        switch (throwable.getMessage()) {
            case "414":
                error = "参数错误";
                if (getMvpView() != null) {
                    getMvpView().enterRoomFail(-1, throwable.getMessage() + ":" + error);
                }
                break;
            case "404":
            case "13002":
            case IMError.IM_ROOM_NOT_EXIST://房间不存在
                if (getMvpView() != null) {
                    getMvpView().showFinishRoomView();
                }
                error = "聊天室不存在";
                break;
            case "403":
                error = "无权限";
                if (getMvpView() != null) {
                    getMvpView().enterRoomFail(-1, throwable.getMessage() + ":" + error);
                }
                break;
            case "500":
                error = "服务器内部错误";
                break;
            case "13001":
                error = "IM主连接状态异常";
                if (getMvpView() != null) {
                    getMvpView().enterRoomFail(-1, throwable.getMessage() + ":" + error);
                }
                break;
            case "13003":
            case IMError.IM_USER_IN_ROOM_BLACK_LIST://用户在房间黑名单中
                error = "黑名单用户禁止进入聊天室";
                if (getMvpView() != null) {
                    getMvpView().showBlackEnterRoomView();
                }
                break;
            case "100001100":
                exitRoom();
                if (getMvpView() != null) {
                    getMvpView().enterRoomFail(-1, "初始化失败，errorCode : 100001100");
                }
                break;
            case GET_ROOM_FROM_IMNET_ERROR:
                exitRoom();
                if (getMvpView() != null) {
                    getMvpView().enterRoomFail(-1, throwable.getMessage() + ":" + "网络异常");

                }
                break;

            case IMError.IM_LOGIN_FAIL://"im登录鉴权失败
            case IMError.IM_GET_USER_INFO_FAIL://获取用户信息失败
            case IMError.IM_GET_ROOM_INFO_FAIL://获取用户信息失败
            case IMError.IM_ROOM_SOCKET_ID_NOT_EXIST://用户房间socketId不存在
                exitRoom();
                if (getMvpView() != null) {
                    getMvpView().enterRoomFail(-1, throwable.getMessage() + ":" + "网络异常");

                }
                break;
            default:
                error = "网络异常";
                if (getMvpView() != null) {
                    getMvpView().enterRoomFail(-1, throwable.getMessage() + ":" + error);
                }
                break;

        }

    }


    private void addInfoToMicInList(Entry<String, String> entry, JsonParser jsonParser) {

        JsonObject valueJsonObj = jsonParser.parse(entry.value).getAsJsonObject();


        Set<String> strings = valueJsonObj.keySet();
        if (valueJsonObj == null)
            return;
        Json json = new Json();
        for (String key : strings) {
            json.set(key, valueJsonObj.get(key).getAsString());
        }
        AvRoomDataManager.get().addMicInListInfo(entry.key, json);

    }

    /**
     * 处理网易云信坑位信息
     */
    private SparseArray<RoomQueueInfo> dealMicMemberFromIMNet(List<Entry<String, String>> entries) {

        if (!ListUtils.isListEmpty(entries)) {
            JsonParser jsonParser = new JsonParser();
            IMChatRoomMember chatRoomMember;
            for (Entry<String, String> entry : entries) {
                LogUtils.d("micInListLogFetchQueue", "key:" + entry.key + "   content:" + entry.value);
                if (entry.key != null && entry.key.length() > 2) {
                    //: 2018/4/26 01
                    addInfoToMicInList(entry, jsonParser);
                    continue;

                }


                RoomQueueInfo roomQueueInfo = AvRoomDataManager.get().mMicQueueMemberMap.get(Integer.parseInt(entry.key));
                if (roomQueueInfo != null) {
                    JsonObject valueJsonObj = jsonParser.parse(entry.value).getAsJsonObject();
                    if (valueJsonObj != null) {
                        chatRoomMember = new IMChatRoomMember();
                        if (valueJsonObj.has("uid")) {
                            int uid = valueJsonObj.get("uid").getAsInt();
                            chatRoomMember.setAccount(String.valueOf(uid));
                        }
                        if (valueJsonObj.has("nick")) {
                            chatRoomMember.setNick(valueJsonObj.get("nick").getAsString());
                        }
                        if (valueJsonObj.has("avatar")) {
                            chatRoomMember.setAvatar(valueJsonObj.get("avatar").getAsString());
                        }
                        if (valueJsonObj.has("gender")) {
                            chatRoomMember.setGender(valueJsonObj.get("gender").getAsInt());
                        }

                        if (valueJsonObj.has(SpEvent.headwearUrl)) {
                            chatRoomMember.setHeadwear_url(valueJsonObj.get(SpEvent.headwearUrl).getAsString());
                        }

                        roomQueueInfo.mChatRoomMember = chatRoomMember;
                    }
                    AvRoomDataManager.get().mMicQueueMemberMap.put(Integer.valueOf(entry.key), roomQueueInfo);
                }
            }
        }
        return AvRoomDataManager.get().mMicQueueMemberMap;
    }


    /**
     * 处理服务端坑位信息
     */
    @Nullable
    private ObservableSource<List<Entry<String, String>>> dealServerMicInfo(EnterChatRoomResultData enterChatRoomResultData) {

        if (enterChatRoomResultData == null)
            return Observable.error(new Throwable(GET_ROOM_FROM_IMNET_ERROR));
        else {
            final ChatRoomInfo roomInfo = enterChatRoomResultData.getRoomInfo();
            if (roomInfo == null) {
                return Observable.error(new Throwable(GET_ROOM_FROM_IMNET_ERROR));
            }
            AvRoomDataManager.get().mCurrentRoomInfo.setOnlineNum(roomInfo.getOnlineUserCount());
            Map<String, Object> extension = roomInfo.getExtension();
            if (extension != null) {
                String roomInfoStr = (String) extension.get(Constants.KEY_CHAT_ROOM_INFO_ROOM);
                if (!TextUtils.isEmpty(roomInfoStr)) {
                    RoomInfo extRoomInfo = mGson.fromJson(roomInfoStr, RoomInfo.class);
                    extRoomInfo.setRoomId(Long.valueOf(roomInfo.getRoomId()));
                    extRoomInfo.setOnlineNum(AvRoomDataManager.get().mCurrentRoomInfo.getOnlineNum());
                    //云信服务端信息
                    AvRoomDataManager.get().mCurrentRoomInfo = extRoomInfo;
                }
                //获取云信麦序相关信息
                String roomMicStr = (String) extension.get(Constants.KEY_CHAT_ROOM_INFO_MIC);
                if (!TextUtils.isEmpty(roomMicStr)) {
                    //初始化所有坑位
                    Map<String, String> micMapStr = mGson.fromJson(roomMicStr, new TypeToken<Map<String, String>>() {
                    }.getType());
                    for (Map.Entry<String, String> entry : micMapStr.entrySet()) {
                        AvRoomDataManager.get().mMicQueueMemberMap.put(Integer.valueOf(entry.getKey()),
                                new RoomQueueInfo(mGson.fromJson(entry.getValue(), RoomMicInfo.class), null));
                    }


                    return mAvRoomModel.queryRoomMicInfo(roomInfo.getRoomId());
                }
            }
            return Observable.error(new Throwable(GET_ROOM_FROM_IMNET_ERROR));
        }
    }

    public void exitRoom() {
        mAvRoomModel.exitRoom(new CallBack<String>() {
            @Override
            public void onSuccess(String data) {
                if (getMvpView() != null) {
                    getMvpView().exitRoom(AvRoomDataManager.get().mCurrentRoomInfo);
                }
            }

            @Override
            public void onFail(int code, String error) {

            }
        });
    }


    public void requestRoomInfoFromService(String uId) {
        if (TextUtils.isEmpty(uId)) {
            return;
        }

        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", uId);
        params.put("visitorUid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");

        OkHttpManager.getInstance().getRequest(UriProvider.getRoomInfo(), params, new OkHttpManager.MyCallBack<ServiceResult<RoomInfo>>() {
            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().requestRoomInfoFailView(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult<RoomInfo> data) {
                if (null != data && data.isSuccess()) {
                    if (getMvpView() != null) {
                        if (data.getData() == null || data.getData().getRoomId() == 0) {
                            if (getMvpView() != null) {
                                getMvpView().showFinishRoomView();
                            }
                            return;
                        }
                        getMvpView().requestRoomInfoSuccessView(data.getData());
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().requestRoomInfoFailView(data.getErrorMessage());
                    }
                }
            }
        });
    }

    /**
     * 获取活动信息
     */
    public void getActionDialog(int type) {

        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        params.put("type", type + "");

        OkHttpManager.getInstance().getRequest(UriProvider.getRedBagDialogType(), params, new OkHttpManager.MyCallBack<ServiceResult<List<ActionDialogInfo>>>() {

            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().onGetActionDialogError(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult<List<ActionDialogInfo>> data) {
                if (null != data && data.isSuccess()) {
                    if (getMvpView() != null && data.getData() != null) {
                        getMvpView().onGetActionDialog(data.getData());
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().onGetActionDialogError(data.getErrorMessage());
                    }
                }
            }
        });
    }

    /**
     * 获取房间内固定成员列表
     */
    public void getNormalChatMember() {
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo == null) {
            return;
        }
        long currentUid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
        mAvRoomModel.getNormalChatMember(String.valueOf(roomInfo.getRoomId()), currentUid);
    }

    /**
     * 获取管理员列表
     */
    public void getRoomManagerMember() {
        mAvRoomModel.fetchRoomManagers(new OkHttpManager.MyCallBack<IMReportResult<List<IMChatRoomMember>>>() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(IMReportResult<List<IMChatRoomMember>> response) {
                if (response != null && response.isSuccess() && response.getData() != null && !ListUtils.isListEmpty(response.getData()))
                    AvRoomDataManager.get().mRoomManagerList = response.getData();
            }
        });
    }

    @Override
    public void onDestroyPresenter() {
        super.onDestroyPresenter();
        if (mGetOnlineNumberDisposable != null) {
            mGetOnlineNumberDisposable.dispose();
            mGetOnlineNumberDisposable = null;
        }
    }

    @Override
    public void onCreatePresenter(@Nullable Bundle saveState) {
        super.onCreatePresenter(saveState);
//        startGetOnlineMemberNumberJob();
    }
}
