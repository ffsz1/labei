package com.tongdaxing.xchat_core.room.presenter;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.tongdaxing.erban.libcommon.base.AbstractMvpPresenter;
import com.tongdaxing.erban.libcommon.listener.CallBack;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.manager.IMNetEaseManager;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.room.model.LightChatRoomModel;
import com.tongdaxing.xchat_core.room.view.ILightChatRoomView;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.im.IMProCallBack;
import com.tongdaxing.xchat_framework.im.IMReportBean;


/**
 * <p> 轻聊房</p>
 *
 * @author jiahui
 * @date 2017/12/24
 */
public class LightChatRoomPresenter extends AbstractMvpPresenter<ILightChatRoomView> {

    private final LightChatRoomModel mLightChatRoomModel;

    public LightChatRoomPresenter() {
        mLightChatRoomModel = new LightChatRoomModel();
    }

    public void sendRoomTextMsg(String message) {
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo == null || TextUtils.isEmpty(message)) return;
        IMNetEaseManager.get()
                .sendTextMsg(roomInfo.getRoomId(), message, new IMProCallBack() {
                    @Override
                    public void onSuccessPro(IMReportBean imReportBean) {

                    }

                    @Override
                    public void onError(int errorCode, String errorMsg) {

                    }
                });
//                .subscribe(new Consumer<ChatRoomMessage>() {
//                    @Override
//                    public void accept(ChatRoomMessage chatRoomMessage) throws Exception {
//                        IMNetEaseManager.get().addMessagesImmediately(chatRoomMessage);
//                        Logger.i("发送房间信息成功:" + chatRoomMessage);
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        throwable.printStackTrace();
//                        Logger.i("发送房间信息失败:" + throwable.getMessage());
//                    }
//                });
    }

    /**
     * 点赞
     *
     * @param type     type:喜欢操作类型，1是喜欢，2是取消喜欢，必填
     * @param likedUid 被点赞人uid，必填
     * @return
     */
    public void praise(final int type, final long likedUid) {
        mLightChatRoomModel.praise(type, likedUid, new CallBack<String>() {
            @Override
            public void onSuccess(String data) {
                if (getMvpView() != null)
                    getMvpView().praiseSuccess(type, likedUid);
            }

            @Override
            public void onFail(int code, String error) {
                if (getMvpView() != null)
                    getMvpView().praiseFail(type, likedUid);
            }
        });
    }

    public void upMicroPhone(int micPosition, final String uId, boolean isInviteUpMic) {
        final RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo == null) {
            return;
        }
        mLightChatRoomModel.upMicroPhone(micPosition, uId, String.valueOf(roomInfo.getRoomId()),
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
     * 下麦
     *
     * @param micPosition
     * @param isKick      是否是主动的
     */
    public void downMicroPhone(int micPosition, final boolean isKick) {
        final RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo == null) {
            return;
        }
        final String currentUid = String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid());
        mLightChatRoomModel.downMicroPhone(micPosition, new CallBack<String>() {
            @Override
            public void onSuccess(String data) {
                Logger.i("用户%1$s下麦成功：%2$s", currentUid, data);
                if (!isKick) {
                    //被踢了
                    if (getMvpView() != null) {
                        getMvpView().kickDownMicroPhoneSuccess();
                    }
                }
            }

            @Override
            public void onFail(int code, String error) {
                Logger.i("用户%1$s下麦失败：%2$s----", currentUid, error);
            }
        });
    }
}
