package com.tongdaxing.xchat_core.room.presenter;

import android.util.Log;

import com.netease.nim.uikit.common.util.string.StringUtil;
import com.tongdaxing.erban.libcommon.base.AbstractMvpPresenter;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.gift.IGiftCore;
import com.tongdaxing.xchat_core.home.TabInfo;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.manager.IMNetEaseManager;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.room.model.RoomSettingModel;
import com.tongdaxing.xchat_core.room.view.IRoomSettingView;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;

import java.util.List;

import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_CHANGE_ROOM_LOCK;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_CHANGE_ROOM_NAME;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_CHANGE_ROOM_NO_LOCK;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_RIDE_GIFT_EFFECT_CLOSE;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_RIDE_GIFT_EFFECT_OPEN;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_SUB_GIFT_EFFECT_CLOSE;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_SUB_GIFT_EFFECT_OPEN;

/**
 * <p>  </p>
 *
 * @author jiahui
 * @date 2017/12/15
 */
public class RoomSettingPresenter extends AbstractMvpPresenter<IRoomSettingView> {

    private final RoomSettingModel model;

    public RoomSettingPresenter() {
        model = new RoomSettingModel();
    }

    public void requestTagAll() {
        model.requestTagAll(CoreManager.getCore(IAuthCore.class).getTicket(), new OkHttpManager.MyCallBack<ServiceResult<List<TabInfo>>>() {
            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().onResultRequestTagAllFail(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult<List<TabInfo>> data) {
                IRoomSettingView roomSettingView = getMvpView();
                if (roomSettingView == null) return;
                if (null != data && data.isSuccess()) {
                    roomSettingView.onResultRequestTagAllSuccess(data.getData());
                } else {
                    roomSettingView.onResultRequestTagAllFail(data.getErrorMessage());
                }
            }
        });
    }

    /**
     * 判断开关 避免后台发送更新信息太快而出现无法判断状态是否改变的情况
     */
    boolean changeGiftEffect = false;
    boolean ridtGiftEffect = false;
    boolean roomNameEffect = false;//房间名字
    boolean roomLockFlag = false;//房间锁

    /**
     * 更新房间设置信息
     *
     * @param title
     * @param desc
     * @param pwd
     * @param label   标签名字
     * @param tagId   标签id
     * @param backPic
     */
    public void updateRoomInfo(String title, String desc, String pwd, String label, int tagId, String backPic, final int giftEffect, final int rideEffect) {
        final long uid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
        if (StringUtil.isEmpty(title)) {
            UserInfo userInfo = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(uid);
            if (userInfo != null) {
                title = userInfo.getNick() + "的房间";
            }
        }

        if (AvRoomDataManager.get().mCurrentRoomInfo.getGiftEffectSwitch() != giftEffect) {
            changeGiftEffect = true;
        }
        if (AvRoomDataManager.get().mCurrentRoomInfo.getGiftCardSwitch() != rideEffect) {
            ridtGiftEffect = true;
        }

        String roomTitle = AvRoomDataManager.get().mCurrentRoomInfo.title;
        if (title !=null) {
            if(roomTitle!=null) {
                if (!title.equals(roomTitle)) roomNameEffect = true;
            }
        }

        String roomPwd = AvRoomDataManager.get().mCurrentRoomInfo.roomPwd;
        if (pwd != null) {
            if(roomPwd !=null){
                if(!pwd.equals(roomPwd)) roomLockFlag = true;
            }
        }

        model.updateRoomInfo(title, desc, pwd, label, tagId, uid, CoreManager.getCore(IAuthCore.class).getTicket(), backPic, giftEffect, rideEffect, new OkHttpManager.MyCallBack<ServiceResult<RoomInfo>>() {

            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().updateRoomInfoFail(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult<RoomInfo> data) {
                IRoomSettingView roomSettingView = getMvpView();
                if (roomSettingView == null) {
                    return;
                }
                if (null != data && data.isSuccess()) {
                    roomSettingView.updateRoomInfoSuccess(data.getData());
                    AvRoomDataManager.get().mCurrentRoomInfo = data.getData();

                    if (AvRoomDataManager.get().mCurrentRoomInfo != null) {
                        if(changeGiftEffect) {
                            //判断是否发送屏蔽小礼物特效消息
                            IMNetEaseManager.get().systemNotificationBySdk(uid, giftEffect == 1 ? CUSTOM_MSG_HEADER_TYPE_SUB_GIFT_EFFECT_OPEN : CUSTOM_MSG_HEADER_TYPE_SUB_GIFT_EFFECT_CLOSE, -1);
                            changeGiftEffect = false;
                        }
                        if(ridtGiftEffect){
                            //判断是否发送屏蔽小礼物特效消息
                            IMNetEaseManager.get().systemNotificationBySdk(uid, rideEffect == 1 ? CUSTOM_MSG_HEADER_TYPE_RIDE_GIFT_EFFECT_OPEN : CUSTOM_MSG_HEADER_TYPE_RIDE_GIFT_EFFECT_CLOSE, -1);
                            ridtGiftEffect = false;
                        }
                        if(roomNameEffect){
                            //修改房间名字
                            CoreManager.getCore(IGiftCore.class).sendRoomNameMeg(uid, data.getData().getTitle() , AvRoomDataManager.get().mCurrentRoomInfo.getRoomId());
                            roomNameEffect = false;
                        }
                        if(roomLockFlag){
                            //房间锁
                            IMNetEaseManager.get().systemNotificationBySdk(uid, StringUtil.isEmpty(pwd) ? CUSTOM_MSG_CHANGE_ROOM_NO_LOCK : CUSTOM_MSG_CHANGE_ROOM_LOCK, -1);
                            roomLockFlag = false;
                        }
                    }
                } else {
                    roomSettingView.updateRoomInfoFail(data.getErrorMessage());
                }
            }
        });
    }

    boolean giftEffectAdmin = false;
    boolean rideEffectAdmin = false;
    boolean roomNameAdmin = false;//房间名字
    boolean roomLockAdmin = false;//房间锁

    public void updateByAdmin(long roomUid, String title, String desc, String pwd, String label, int tagId, String backPic, final int giftEffect, final int rideEffect) {
        final long uid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
        if ("".equals(title)) {
            title = null;
            UserInfo userInfo = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(uid);
            if (userInfo != null) {
                title = userInfo.getNick() + "的房间";
            }
        }
        if (AvRoomDataManager.get().mCurrentRoomInfo.getGiftEffectSwitch() != giftEffect) {
            giftEffectAdmin = true;
        }
        if (AvRoomDataManager.get().mCurrentRoomInfo.getGiftCardSwitch() != rideEffect) {
            rideEffectAdmin = true;
        }

        String roomTitle = AvRoomDataManager.get().mCurrentRoomInfo.title;
        if (title !=null) {
            if(roomTitle!=null) {
                if (!title.equals(roomTitle)) roomNameAdmin = true;
            }
        }

        String roomPwd = AvRoomDataManager.get().mCurrentRoomInfo.roomPwd;
        if (pwd != null) {
            if(roomPwd !=null){
                if(!pwd.equals(roomPwd)) roomLockAdmin = true;
            }
        }
        model.updateByAdmin(roomUid, title, desc, pwd, label, tagId, uid, CoreManager.getCore(IAuthCore.class).getTicket(), backPic, giftEffect,rideEffect, new OkHttpManager.MyCallBack<ServiceResult<RoomInfo>>() {

            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().updateRoomInfoFail(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult<RoomInfo> data) {
                IRoomSettingView roomSettingView = getMvpView();
                if (roomSettingView == null) {
                    return;
                }
                if (null != data && data.isSuccess()) {
                    if (AvRoomDataManager.get().mCurrentRoomInfo != null) {
                        //判断是否发送屏蔽小礼物特效消息
                        if (giftEffectAdmin) {
                            IMNetEaseManager.get().systemNotificationBySdk(uid, giftEffect == 1 ? CUSTOM_MSG_HEADER_TYPE_SUB_GIFT_EFFECT_OPEN : CUSTOM_MSG_HEADER_TYPE_SUB_GIFT_EFFECT_CLOSE, -1);
                            giftEffectAdmin = false;
                        }
                        if (rideEffectAdmin) {
                            //判断是否发送屏蔽小礼物特效消息
                            IMNetEaseManager.get().systemNotificationBySdk(uid, rideEffect == 1 ? CUSTOM_MSG_HEADER_TYPE_RIDE_GIFT_EFFECT_OPEN : CUSTOM_MSG_HEADER_TYPE_RIDE_GIFT_EFFECT_CLOSE, -1);
                            rideEffectAdmin = false;
                        }
                        if(roomNameAdmin){
                            //修改房间名字
                            CoreManager.getCore(IGiftCore.class).sendRoomNameMeg(uid, data.getData().getTitle() , AvRoomDataManager.get().mCurrentRoomInfo.getRoomId());
                            roomNameAdmin = false;
                        }
                        if(roomLockAdmin){
                            //房间锁
                            IMNetEaseManager.get().systemNotificationBySdk(uid, StringUtil.isEmpty(pwd) ? CUSTOM_MSG_CHANGE_ROOM_NO_LOCK : CUSTOM_MSG_CHANGE_ROOM_LOCK, -1);
                            roomLockAdmin = false;
                        }
                    }

                    roomSettingView.updateRoomInfoSuccess(data.getData());
                } else {
                    roomSettingView.updateRoomInfoFail(data.getErrorMessage());
                }
            }
        });
    }

    public void saveRoomPlayTip(String playInfo, RoomInfo roomInfo, String url) {

        model.saveRoomPlayTip(playInfo, roomInfo, url, new OkHttpManager.MyCallBack<ServiceResult>() {

            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().onSaveRoomPlayTipFailView(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        if (getMvpView() != null) {
                            getMvpView().onSaveRoomPlayTipSuccessView();
                        }
                    } else {
                        if (getMvpView() != null) {
                            getMvpView().onSaveRoomPlayTipFailView(response.getMessage());
                        }
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().onSaveRoomPlayTipFailView("数据异常");
                    }
                }
            }
        });
    }

    /**
     * 修改房间话题
     */
    public void saveRoomTopic(String roomDesc, String roomNotice, RoomInfo roomInfo, String url) {

        model.saveRoomTopic(roomDesc, roomNotice, roomInfo, url, new OkHttpManager.MyCallBack<ServiceResult>() {

            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().onSaveRoomTopicFailView(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        if (getMvpView() != null) {
                            getMvpView().onSaveRoomTopicSuccessView(roomDesc,roomNotice);
                        }
                    } else {
                        if (getMvpView() != null) {
                            getMvpView().onSaveRoomTopicFailView(response.getMessage());
                        }
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().onSaveRoomTopicFailView("数据异常");
                    }
                }
            }
        });
    }
}
