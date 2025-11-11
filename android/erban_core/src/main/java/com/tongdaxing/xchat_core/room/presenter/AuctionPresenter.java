package com.tongdaxing.xchat_core.room.presenter;

import com.alibaba.fastjson.JSONObject;
import com.orhanobut.logger.Logger;
import com.tongdaxing.erban.libcommon.base.AbstractMvpPresenter;
import com.tongdaxing.erban.libcommon.listener.CallBack;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.bean.RoomQueueInfo;
import com.tongdaxing.xchat_core.follow.FollowModel;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.manager.ReUsedSocketManager;
import com.tongdaxing.xchat_core.manager.RtcEngineManager;
import com.tongdaxing.xchat_core.room.auction.bean.AuctionInfo;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.room.model.AuctionModel;
import com.tongdaxing.xchat_core.room.model.HomePartyModel;
import com.tongdaxing.xchat_core.room.model.RoomBaseModel;
import com.tongdaxing.xchat_core.room.view.IAuctionView;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.im.IMProCallBack;
import com.tongdaxing.xchat_framework.im.IMReportBean;

import io.agora.rtc.Constants;
import io.reactivex.Single;

/**
 * @author xiaoyu
 * @date 2017/12/25
 */

public class AuctionPresenter extends AbstractMvpPresenter<IAuctionView> {
    private static final String TAG = "AuctionPresenter";
    private FollowModel followModel;
    private AuctionModel auctionModel;
    private RoomBaseModel roomBaseModel;
    private HomePartyModel homePartyModel;

    public AuctionPresenter() {
        followModel = FollowModel.get();
        auctionModel = AuctionModel.get();
        roomBaseModel = new RoomBaseModel();
        homePartyModel = new HomePartyModel();
    }

    public void inviteUpMic(long roomId, int micPosition) {
        UserInfo userInfo = CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo();
        if (userInfo == null) return;
        JSONObject json = new JSONObject();
        json.put("uid", String.valueOf(userInfo.getUid()));
        json.put("nick", userInfo.getNick());
        json.put("avatar", userInfo.getAvatar());
        json.put("gender", userInfo.getGender());
        final RoomQueueInfo roomQueueInfo = AvRoomDataManager.get().mMicQueueMemberMap.get(micPosition);
        ReUsedSocketManager.get().updateQueue(String.valueOf(roomId), micPosition, userInfo.getUid(), new IMProCallBack() {
            @Override
            public void onSuccessPro(IMReportBean imReportBean) {
                if (imReportBean != null && imReportBean.getReportData() != null && imReportBean.getReportData().errno == 0) {
                    if (roomQueueInfo != null && roomQueueInfo.mRoomMicInfo != null
                            && !roomQueueInfo.mRoomMicInfo.isMicMute()) {
                        RtcEngineManager.get().setRole(Constants.CLIENT_ROLE_BROADCASTER);
                        // 是否需要开麦
                        if (!AvRoomDataManager.get().mIsNeedOpenMic) {
                            RtcEngineManager.get().setMute(true);
                            AvRoomDataManager.get().mIsNeedOpenMic = true;
                        }
                    } else {
                        RtcEngineManager.get().setRole(Constants.CLIENT_ROLE_AUDIENCE);
                    }
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {

            }
        });
//        IMNetEaseManager.get().updateQueueEx(roomId, micPosition,
//                json.toJSONString()).subscribe(new BiConsumer<Boolean, Throwable>() {
//            @Override
//            public void accept(Boolean aBoolean, Throwable throwable) throws Exception {
//                if (throwable != null)
//                    Logger.e(TAG, "update queue fails: " + throwable.getMessage());
//                else {
//                    if (roomQueueInfo != null && roomQueueInfo.mRoomMicInfo != null
//                            && !roomQueueInfo.mRoomMicInfo.isMicMute()) {
//                        RtcEngineManager.get().setRole(Constants.CLIENT_ROLE_BROADCASTER);
//                        // 是否需要开麦
//                        if (!AvRoomDataManager.get().mIsNeedOpenMic) {
//                            RtcEngineManager.get().setMute(true);
//                            AvRoomDataManager.get().mIsNeedOpenMic = true;
//                        }
//                    } else {
//                        RtcEngineManager.get().setRole(Constants.CLIENT_ROLE_AUDIENCE);
//                    }
//                }
//            }
//        });
    }

    public void upMic(int micPosition, final String uId, boolean isInviteUpMic) {
        final RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo == null)
            return;
        roomBaseModel.upMicroPhone(micPosition, uId, String.valueOf(roomInfo.getRoomId()),
                isInviteUpMic, new CallBack<String>() {
                    @Override
                    public void onSuccess(String data) {
                        Logger.i("用户%1$s上麦成功：%2$s", uId, data);
                    }

                    @Override
                    public void onFail(int code, String error) {
                        Logger.i("用户%1$s上麦失败：%2$s----", uId, error);
                    }
                });
    }


    /**
     * 开麦
     *
     * @param micPosition
     */
    public void openMicroPhone(int micPosition) {
        final RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo == null) return;
        homePartyModel.openMicroPhone(micPosition, roomInfo.getUid(), new OkHttpManager.MyCallBack<ServiceResult<String>>() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(ServiceResult<String> data) {
                if (null != data && data.isSuccess()) {//解麦成功
                    Logger.i("用户%1$s闭麦成功: %2$s", String.valueOf(roomInfo.getUid()), data);
                } else {//解麦失败
                    Logger.i("用户%1$s闭麦失败: %2$s", String.valueOf(roomInfo.getUid()), data.getError());
                }
            }
        });
    }

    /**
     * 闭麦
     *
     * @param micPosition
     */
    public void closeMicroPhone(int micPosition) {
        final RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo == null) return;
        homePartyModel.closeMicroPhone(micPosition, roomInfo.getUid(), new OkHttpManager.MyCallBack<ServiceResult<String>>() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(ServiceResult<String> data) {
            }
        });
    }

    public void follow(final long likedUid, final boolean follow) {
        followModel.follow(likedUid, follow).subscribe();
    }

    public Single<ServiceResult<Boolean>> isFollowed(final long uid, final long isLikeUid) {
        return followModel.isFollowed(uid, isLikeUid);
    }

    public void startAuction(long uid, long auctUid, int auctMoney, int servDura, int minRaiseMoney, final String auctDesc) {
        auctionModel.startAuction(uid, auctUid, auctMoney, servDura, minRaiseMoney, auctDesc).subscribe();
    }

    public Single<AuctionInfo> requestAuctionInfo(long uid) {
        return auctionModel.requestAuctionInfo(uid);
    }

    public boolean isInAuctionNow() {
        return auctionModel.isInAuctionNow();
    }

    public AuctionInfo getAuctionInfo() {
        return auctionModel.getAuctionInfo();
    }
}
























