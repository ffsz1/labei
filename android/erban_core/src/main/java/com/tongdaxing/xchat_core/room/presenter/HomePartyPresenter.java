package com.tongdaxing.xchat_core.room.presenter;

import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.util.Entry;
import com.orhanobut.logger.Logger;
import com.tongdaxing.erban.libcommon.base.AbstractMvpPresenter;
import com.tongdaxing.erban.libcommon.listener.CallBack;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.erban.libcommon.widget.ButtonItem;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.bean.ChatRoomMessage;
import com.tongdaxing.xchat_core.bean.IMChatRoomMember;
import com.tongdaxing.xchat_core.bean.RoomMicInfo;
import com.tongdaxing.xchat_core.bean.RoomQueueInfo;
import com.tongdaxing.xchat_core.bean.attachmsg.RoomQueueMsgAttachment;
import com.tongdaxing.xchat_core.im.avroom.IAVRoomCoreClient;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.manager.IMNetEaseManager;
import com.tongdaxing.xchat_core.manager.IMSendCallBack;
import com.tongdaxing.xchat_core.manager.ReUsedSocketManager;
import com.tongdaxing.xchat_core.manager.RtcEngineManager;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.room.model.AvRoomModel;
import com.tongdaxing.xchat_core.room.model.FingerGuessingGameModel;
import com.tongdaxing.xchat_core.room.model.HomePartyModel;
import com.tongdaxing.xchat_core.room.queue.bean.RoomConsumeInfo;
import com.tongdaxing.xchat_core.room.view.IHomePartyView;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.tongdaxing.xchat_framework.im.IMError;
import com.tongdaxing.xchat_framework.im.IMProCallBack;
import com.tongdaxing.xchat_framework.im.IMReportBean;
import com.tongdaxing.xchat_framework.util.config.SpEvent;
import com.tongdaxing.xchat_framework.util.util.Json;
import com.tongdaxing.xchat_framework.util.util.LogUtils;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.agora.rtc.Constants;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

import static com.tongdaxing.xchat_core.UriProvider.JAVA_WEB_URL;
//import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_ALLOW_MIC_POISITION;
//import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_CLOSE_MIC_POISITION;
//import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_FORBIDDEN_MIC_POISITION;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_QUEUE;
//import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_OPEN_MIC_POISITION;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_ROOM_CLEAN_MEILI;

/**
 * <p>  </p>
 *
 * @author jiahui
 * @date 2017/12/8
 */
public class HomePartyPresenter extends AbstractMvpPresenter<IHomePartyView> {
    private final HomePartyModel mHomePartyMode;
    private final AvRoomModel avRoomModel;
    private FingerGuessingGameModel fingerGuessingGameModel;
    /**
     * 判断所坑服务端是否响应回来了
     */
    private boolean mIsLockMicPosResultSuccess = true;
    private boolean mIsUnLockMicPosResultSuccess = true;

    public HomePartyPresenter() {
        fingerGuessingGameModel = new FingerGuessingGameModel();
        avRoomModel = new AvRoomModel();
        mHomePartyMode = new HomePartyModel();
    }

    public void getFingerGuessingGameState(long roomId) {
        fingerGuessingGameModel.getFingerGuessingGameState(roomId, new OkHttpManager.MyCallBack<Json>() {
            @Override
            public void onError(Exception e) {
                mMvpView.showFingerGuessingGame(false);
            }

            @Override
            public void onResponse(Json response) {
                if (response != null && response.num("code") == 200) {
                    if (response.num("data") == 1) {
                        if (response.num("data") == 1) {
                            mMvpView.showFingerGuessingGame(true);
                        } else {
                            mMvpView.showFingerGuessingGame(false);
                        }
                    } else {
                        onError(new Exception());
                    }
                }
            }
        });
    }

//    public void startFingerGuessingGame(long roomId, int probability) {
//        fingerGuessingGameModel.startFingerGuessingGame(roomId, probability, new OkHttpManager.MyCallBack<Json>() {
//            @Override
//            public void onError(Exception e) {
//
//            }
//
//            @Override
//            public void onResponse(Json response) {
//
//            }
//        });
//    }

    /**
     * 麦坑点击处理，麦上没人的时候
     *
     * @param micPosition    麦序位置
     * @param chatRoomMember 坑上的用户
     */
    public void microPhonePositionClick(final int micPosition, IMChatRoomMember chatRoomMember) {
        final RoomInfo currentRoom = AvRoomDataManager.get().mCurrentRoomInfo;
        if (currentRoom == null) {
            return;
        }
        final String currentUid = String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid());
        if (AvRoomDataManager.get().isRoomOwner(currentUid)) {
            onOwnerUpMicroClick(micPosition, currentRoom.getUid(), true);
        } else if (AvRoomDataManager.get().isRoomAdmin(currentUid)) {
            onOwnerUpMicroClick(micPosition, currentRoom.getUid(), false);
        } else {
            RoomQueueInfo roomQueueInfo = AvRoomDataManager.get().getRoomQueueMemberInfoByMicPosition(micPosition);
            if (roomQueueInfo == null) return;
            if(roomQueueInfo.mRoomMicInfo.isMicLock()){
                ToastUtils.showShort("麦位已经上锁，无法上麦！");
                return;
            }

            upMicroPhone(micPosition, currentUid, false);
        }
    }

    private void onOwnerUpMicroClick(final int micPosition, final long currentUid, boolean isOwner) {
        RoomQueueInfo roomQueueInfo = AvRoomDataManager.get().getRoomQueueMemberInfoByMicPosition(micPosition);
        if (roomQueueInfo == null) return;
        if (getMvpView() != null)
            getMvpView().showOwnerClickDialog(roomQueueInfo.mRoomMicInfo, micPosition, currentUid, isOwner);
    }

    /**
     * 排麦加入队列
     */
    public void addMicInList() {

        if (AvRoomDataManager.get().isOwnerOnMic())
            return;

        UserInfo userInfo = CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo();
        int index = AvRoomDataManager.get().checkHasEmpteyMic();
        if (index == AvRoomDataManager.MIC_FULL) {
            //: 2018/4/26 3
            RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
            if (roomInfo != null && userInfo != null)
                mHomePartyMode.updateQueueEx((int) userInfo.getUid(), roomInfo.getRoomId() + "", null, userInfo, 1);
        } else {
            if (userInfo != null) {
                upMicroPhone(index, userInfo.getUid() + "", false);
                CoreManager.notifyClients(IAVRoomCoreClient.class, IAVRoomCoreClient.micInListDismiss, "");
            }
        }


    }


    public void lockMicroPhone(int micPosition, final long currentUid) {
        if (!mIsLockMicPosResultSuccess) {
            return;
        }
        mIsLockMicPosResultSuccess = false;
        mHomePartyMode.lockMicroPhone(micPosition, String.valueOf(currentUid),
                CoreManager.getCore(IAuthCore.class).getTicket(), new OkHttpManager.MyCallBack<ServiceResult<String>>() {
                    @Override
                    public void onError(Exception e) {
                        mIsLockMicPosResultSuccess = true;
                    }

                    @Override
                    public void onResponse(ServiceResult<String> data) {
                        if (null != data && data.isSuccess()) {//解麦成功
                            mIsLockMicPosResultSuccess = true;
                        } else {//解麦失败
                            mIsLockMicPosResultSuccess = true;
                        }

                        /*String roomId = AvRoomDataManager.get().mCurrentRoomInfo.getRoomId()+"";
                        RoomQueueMsgAttachment queueMsgAttachment = new RoomQueueMsgAttachment(CUSTOM_MSG_HEADER_TYPE_QUEUE,
                                CUSTOM_MSG_CLOSE_MIC_POISITION);
                        queueMsgAttachment.uid = CoreManager.getCore(IAuthCore.class).getCurrentUid() + "";
                        queueMsgAttachment.micPosition = micPosition;
                        ChatRoomMessage message = new ChatRoomMessage();
                        message.setRoom_id(roomId);
                        message.setAttachment(queueMsgAttachment);
                        ReUsedSocketManager.get().sendCustomMessage(roomId, message, new IMSendCallBack() {
                            @Override
                            public void onSuccess(String data) {

                            }

                            @Override
                            public void onError(int errorCode, String errorMsg) {

                            }
                        });*/
                    }
                });
    }

    /**
     * 坑位释放锁
     */
    public void unLockMicroPhone(int micPosition) {
        if (!mIsUnLockMicPosResultSuccess) {
            return;
        }
        mIsUnLockMicPosResultSuccess = false;
        RoomInfo currentRoom = AvRoomDataManager.get().mCurrentRoomInfo;
        if (currentRoom == null) {
            return;
        }
        final String currentUid = String.valueOf(currentRoom.getUid());
        if (AvRoomDataManager.get().isRoomAdmin() || AvRoomDataManager.get().isRoomOwner(currentUid)) {
            mHomePartyMode.unLockMicroPhone(micPosition, currentUid, CoreManager.getCore(IAuthCore.class).getTicket(), new OkHttpManager.MyCallBack<ServiceResult<String>>() {
                @Override
                public void onError(Exception e) {
                    mIsUnLockMicPosResultSuccess = true;
                }

                @Override
                public void onResponse(ServiceResult<String> data) {
                    if (null != data && data.isSuccess()) {//解麦成功
                        mIsUnLockMicPosResultSuccess = true;
                    } else {//解麦失败
                        mIsUnLockMicPosResultSuccess = true;
                    }

//                    RoomQueueMsgAttachment queueMsgAttachment = new RoomQueueMsgAttachment(CUSTOM_MSG_HEADER_TYPE_QUEUE,
//                            CUSTOM_MSG_OPEN_MIC_POISITION);
//                    queueMsgAttachment.uid = CoreManager.getCore(IAuthCore.class).getCurrentUid() + "";
//                    queueMsgAttachment.micPosition = micPosition;
//                    ChatRoomMessage message = new ChatRoomMessage();
//                    message.setRoom_id(currentRoom.getRoomId()+"");
//                    message.setAttachment(queueMsgAttachment);
//                    ReUsedSocketManager.get().sendCustomMessage(currentRoom.getRoomId()+"", message, new IMSendCallBack() {
//                        @Override
//                        public void onSuccess(String data) {
//
//                        }
//
//                        @Override
//                        public void onError(int errorCode, String errorMsg) {
//
//                        }
//                    });
                }
            });
        }
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
        mHomePartyMode.downMicroPhone(micPosition, new CallBack<String>() {
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


    public void upMicroPhone(final int micPosition, final String uId, boolean isInviteUpMic) {
        upMicroPhone(micPosition, uId, isInviteUpMic, false);
    }

    /**
     * 上麦
     *
     * @param micPosition
     * @param uId
     * @param isInviteUpMic 是否是主动的，true：主动
     */
    public void upMicroPhone(final int micPosition, final String uId, boolean isInviteUpMic, boolean needCloseMic) {
        final RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo == null) {
            return;
        }
        mHomePartyMode.upMicroPhone(micPosition, uId, String.valueOf(roomInfo.getRoomId()),
                isInviteUpMic, new CallBack<String>() {
                    @Override
                    public void onSuccess(String data) {
                        if (micPosition == -1) {
                            if (getMvpView() != null) {
                                getMvpView().notifyRefresh();
                            }
                        }
                        Logger.i("用户%1$s上麦成功：%2$s", uId, data);
                    }

                    @Override
                    public void onFail(int code, String error) {
                        Logger.i("用户%1$s上麦失败：%2$s----", uId, error);
                    }
                });
    }

    /**
     * 邀请用户上麦
     *
     * @param micUid   要邀请的用户id
     * @param position 坑位
     */
    public void inviteMicroPhone(final String nickName, final long micUid, final int position) {
        if (AvRoomDataManager.get().isOnMic(micUid)) {
            return;
        }
        //如果点击的就是自己,那这里就是自己上麦
        if (AvRoomDataManager.get().isOwner(micUid)) {
            upMicroPhone(position, String.valueOf(micUid), true);
            return;
        }
        mHomePartyMode.inviteMicroPhone(nickName, micUid, position);
//                .subscribe(new BiConsumer<ChatRoomMessage, Throwable>() {
//                    @Override
//                    public void accept(ChatRoomMessage chatRoomMessage,
//                                       Throwable throwable) throws Exception {
//                        if (throwable != null) {
//                            Logger.i("邀请用户%d上麦失败!!!" + micUid);
//                        } else
//                            Logger.i("邀请用户%d上麦成功!!!" + micUid);
//                    }
//                });
    }

    /**
     * 头像点击，人在麦上
     *
     * @param micPosition
     */
    public void avatarClick(int micPosition) {
//        LogUtils.d("avatarClick", "micPosition:" + micPosition);
        final RoomInfo currentRoom = AvRoomDataManager.get().mCurrentRoomInfo;
        if (currentRoom == null) return;
        // 判断在不在麦上
        RoomQueueInfo roomQueueInfo = AvRoomDataManager.get().getRoomQueueMemberInfoByMicPosition(micPosition);
        if (roomQueueInfo == null) return;
        // 麦上的人员信息,麦上的坑位信息

        IMChatRoomMember chatRoomMember = roomQueueInfo.mChatRoomMember;
        RoomMicInfo roomMicInfo = roomQueueInfo.mRoomMicInfo;


        if (chatRoomMember == null && micPosition == -1) {
            chatRoomMember = new IMChatRoomMember();
            RoomInfo info = AvRoomDataManager.get().mCurrentRoomInfo;
            chatRoomMember.setNick("房主");
            chatRoomMember.setAvatar("");
            chatRoomMember.setAccount(info.getUid() + "");


        }
        if (chatRoomMember == null || roomMicInfo == null) return;

        String currentUid = String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid());
        boolean isMySelf = Objects.equals(currentUid, chatRoomMember.getAccount());
        boolean isTargetRoomAdmin = AvRoomDataManager.get().isRoomAdmin(chatRoomMember.getAccount());
        boolean isTargetRoomOwner = AvRoomDataManager.get().isRoomOwner(chatRoomMember.getAccount());


        List<ButtonItem> buttonItems = new ArrayList<>();
        if (getMvpView() == null) return;
        SparseArray<ButtonItem> avatarButtonItemMap = getMvpView().getAvatarButtonItemList(micPosition,
                chatRoomMember, currentRoom);
        if (AvRoomDataManager.get().isRoomOwner()) {
            //房主操作
            if (!isMySelf) {
                //点击不是自己
                buttonItems.add(avatarButtonItemMap.get(0));
                buttonItems.add(avatarButtonItemMap.get(4));
                buttonItems.add(avatarButtonItemMap.get(2));
                if (roomMicInfo.isMicMute()) {
                    buttonItems.add(avatarButtonItemMap.get(6));
                } else {
                    buttonItems.add(avatarButtonItemMap.get(1));
                }
                buttonItems.add(avatarButtonItemMap.get(3));

                if (!isTargetRoomAdmin) {
                    buttonItems.add(avatarButtonItemMap.get(7));
                } else {
                    buttonItems.add(avatarButtonItemMap.get(8));
                }
                buttonItems.add(avatarButtonItemMap.get(9));
                if (getMvpView() != null) {
                    getMvpView().showMicAvatarClickDialog(buttonItems);
                }
            } else {
                // 查看资料
                // 下麦旁听
                // 解禁此座位
//                buttonItems.add(avatarButtonItemMap.get(4));
//                buttonItems.add(avatarButtonItemMap.get(5));
//                if (roomMicInfo.isMicMute()) {
//                    buttonItems.add(avatarButtonItemMap.get(6));
//                }
                if (getMvpView() != null) {
                    getMvpView().showOwnerSelfInfo(chatRoomMember);
                }
            }

        } else if (AvRoomDataManager.get().isRoomAdmin()) {
            //管理员操作
            if (isMySelf) {
                //点击自己
                //查看资料
                //下麦旁听
                // 禁麦此座位/解禁此座位
                buttonItems.add(avatarButtonItemMap.get(4));
                buttonItems.add(avatarButtonItemMap.get(5));
//                if (roomMicInfo.isMicMute()) {
//                    buttonItems.add(avatarButtonItemMap.get(6));
//                } else {
//                    buttonItems.add(avatarButtonItemMap.get(1));
//                }
            } else {
                if (!isTargetRoomAdmin && !isTargetRoomOwner) {
                    //非房主或管理员
                    //送礼物
                    buttonItems.add(avatarButtonItemMap.get(0));
                    //查看资料
                    buttonItems.add(avatarButtonItemMap.get(4));
                    //抱他下麦
                    buttonItems.add(avatarButtonItemMap.get(2));
                    //禁麦/解麦操作
                    buttonItems.add(roomMicInfo.isMicMute() ?
                            avatarButtonItemMap.get(6) :
                            avatarButtonItemMap.get(1));
                    //踢出房间
                    buttonItems.add(avatarButtonItemMap.get(3));
                    //加入黑名单
                    buttonItems.add(avatarButtonItemMap.get(9));
                } else {
                    getMvpView().showGiftDialog(chatRoomMember);

//                    if (!roomMicInfo.isMicMute() && !isTargetRoomOwner) {
//                        buttonItems.add(avatarButtonItemMap.get(1));
//                    }

//                    if (roomMicInfo.isMicMute() && !isTargetRoomOwner) {
//                        buttonItems.add(avatarButtonItemMap.get(6));
//                    }


//                    if (!isTargetRoomOwner)
//                        buttonItems.add(avatarButtonItemMap.get(2));
                    // 对于管理员和房主,只有解麦功能
//                    if (roomMicInfo.isMicMute())
//                        buttonItems.add(avatarButtonItemMap.get(6));
                }
            }
            if (getMvpView() != null) {
                getMvpView().showMicAvatarClickDialog(buttonItems);
            }
        } else {
            //游客操作
            if (isMySelf) {
                //查看资料
                //下麦旁听
                buttonItems.add(avatarButtonItemMap.get(4));
                buttonItems.add(avatarButtonItemMap.get(5));
                if (getMvpView() != null) {
                    getMvpView().showMicAvatarClickDialog(buttonItems);
                }
            } else {
                buttonItems.add(avatarButtonItemMap.get(0));
                buttonItems.add(avatarButtonItemMap.get(4));

//                LogUtils.d("showGiftDialog", 1 + "");
                if (getMvpView() != null) {
//                    LogUtils.d("showGiftDialog", 2 + "");

                    if (micPosition == -1 && roomQueueInfo.mChatRoomMember == null) {
                        getMvpView().showGiftDialog(chatRoomMember);
                    } else {
                        getMvpView().showGiftDialog(chatRoomMember);
                    }

                }
            }
        }
    }


    /**
     * 开麦
     *
     * @param micPosition
     */
    public void openMicroPhone(int micPosition) {
        final RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo == null) {
            return;
        }
        mHomePartyMode.openMicroPhone(micPosition, roomInfo.getUid(), new OkHttpManager.MyCallBack<ServiceResult<String>>() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(ServiceResult<String> data) {
                /*RoomQueueMsgAttachment queueMsgAttachment = new RoomQueueMsgAttachment(CUSTOM_MSG_HEADER_TYPE_QUEUE,
                        CUSTOM_MSG_ALLOW_MIC_POISITION);
                queueMsgAttachment.uid = CoreManager.getCore(IAuthCore.class).getCurrentUid() + "";
                queueMsgAttachment.micPosition = micPosition;
                ChatRoomMessage message = new ChatRoomMessage();
                message.setRoom_id(String.valueOf(roomInfo.getRoomId()));
                message.setAttachment(queueMsgAttachment);
                ReUsedSocketManager.get().sendCustomMessage(roomInfo.getRoomId() + "", message, new IMSendCallBack() {
                    @Override
                    public void onSuccess(String data) {

                    }

                    @Override
                    public void onError(int errorCode, String errorMsg) {

                    }
                });*/
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
        if (roomInfo == null) {
            return;
        }
        mHomePartyMode.closeMicroPhone(micPosition, roomInfo.getUid(), new OkHttpManager.MyCallBack<ServiceResult<String>>() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(ServiceResult<String> data) {
                /*RoomQueueMsgAttachment queueMsgAttachment = new RoomQueueMsgAttachment(CUSTOM_MSG_HEADER_TYPE_QUEUE,
                        CUSTOM_MSG_FORBIDDEN_MIC_POISITION);
                queueMsgAttachment.uid = CoreManager.getCore(IAuthCore.class).getCurrentUid() + "";
                queueMsgAttachment.micPosition = micPosition;
                ChatRoomMessage message = new ChatRoomMessage();
                message.setRoom_id(String.valueOf(roomInfo.getRoomId()));
                message.setAttachment(queueMsgAttachment);
                ReUsedSocketManager.get().sendCustomMessage(roomInfo.getRoomId() + "", message, new IMSendCallBack() {
                    @Override
                    public void onSuccess(String data) {
                    }

                    @Override
                    public void onError(int errorCode, String errorMsg) {

                    }
                });*/
            }
        });
    }

    /***
     * 发送房间消息
     * @param message
     */
    public void sendTextMsg(String message) {
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo == null || TextUtils.isEmpty(message)) return;
        IMNetEaseManager.get().sendTextMsg(roomInfo.getRoomId(), message, new IMProCallBack() {
            @Override
            public void onSuccessPro(IMReportBean imReportBean) {
                if (imReportBean != null
                        && imReportBean.getReportData() != null
                        && imReportBean.getReportData().errno != 0) {
                    int errorCode = imReportBean.getReportData().errno;
                    if (errorCode == IMError.USER_REAL_NAME_NEED_PHONE
                            || errorCode == IMError.USER_REAL_NAME_AUDITING
                            || errorCode == IMError.USER_REAL_NAME_NEED_VERIFIED) {
                        if (getMvpView() != null) {
                            getMvpView().showVerifiedDialog(imReportBean.getReportData().errno, imReportBean.getReportData().errmsg);
                        }
                    }
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                SingleToastUtil.showToast(errorMsg);//todo 后期调整
            }
        });
    }

    public void chatRoomReConnect(final RoomQueueInfo queueInfo) {
        final RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo == null) return;

        avRoomModel.queryRoomMicInfo(String.valueOf(roomInfo.getRoomId()))
                .delay(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Entry<String, String>>>() {
                    @Override
                    public void accept(final List<Entry<String, String>> entries) throws Exception {
                        boolean needUpMic = true;
                        if (!ListUtils.isListEmpty(entries)) {
                            JsonParser jsonParser = new JsonParser();
                            IMChatRoomMember chatRoomMember;
                            AvRoomDataManager.get().mMicInListMap.clear();
                            int roomMicInfoPosition = -100;
                            if (queueInfo != null && queueInfo.mRoomMicInfo != null) {
                                roomMicInfoPosition = queueInfo.mRoomMicInfo.getPosition();
                            }
                            for (Entry<String, String> entry : entries) {

                                LogUtils.d("micInListLogFetchQueue", "key:" + entry.key + "   content:" + entry.value);

                                if (entry.key != null && entry.key.length() > 2) {
                                    //: 2018/4/26 01
                                    addInfoToMicInList(entry, jsonParser);

                                    continue;
//                                    AvRoomDataManager.get().addMicInListInfo(entry.key, roomQueueInfo);
                                }
                                //判断是否有别人在麦位上
                                if ((roomMicInfoPosition + "").equals(entry.key)) {
                                    String oldQueueUid = queueInfo.mChatRoomMember.getAccount() + "";
                                    String queueUid = Json.parse(entry.value).str("uid");
                                    if (!oldQueueUid.equals(queueUid)) {
                                        needUpMic = false;
                                    }
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
//                                            Map<String, Object> stringStringMap = new HashMap<>();
//                                            stringStringMap.put(SpEvent.headwearUrl, valueJsonObj.get(SpEvent.headwearUrl).getAsString());
//                                            chatRoomMember.setExtension(stringStringMap);
                                        }
                                        roomQueueInfo.mChatRoomMember = chatRoomMember;
                                    }


                                    AvRoomDataManager.get().addRoomQueueInfo(entry.key, roomQueueInfo);


                                }
                            }
                        } else {
                            //麦上都没有人
                            AvRoomDataManager.get().resetMicMembers();
                        }
                        if (getMvpView() != null)
                            getMvpView().chatRoomReConnectView();
                        //之前在麦上
                        if (queueInfo != null && queueInfo.mChatRoomMember != null && queueInfo.mRoomMicInfo != null) {
                            RoomQueueInfo roomQueueInfo = AvRoomDataManager.get()
                                    .getRoomQueueMemberInfoByMicPosition(queueInfo.mRoomMicInfo.getPosition());
                            //麦上没人
                            String account = queueInfo.mChatRoomMember.getAccount();
                            if (needUpMic && roomQueueInfo != null && (roomQueueInfo.mChatRoomMember == null ||
                                    Objects.equals(account, roomQueueInfo.mChatRoomMember.getAccount()))) {


                                roomQueueInfo.mChatRoomMember = null;

                                AvRoomDataManager.get().mIsNeedOpenMic = false;
                                upMicroPhone(queueInfo.mRoomMicInfo.getPosition(), account, true);
                            }
                            if (!needUpMic) {
                                if (AvRoomDataManager.get().isOwner(account)) {
                                    // 更新声网闭麦 ,发送状态信息
                                    RtcEngineManager.get().setRole(Constants.CLIENT_ROLE_AUDIENCE);
                                    AvRoomDataManager.get().mIsNeedOpenMic = false;
                                    if (getMvpView() != null)
                                        getMvpView().notifyBottomBtnState();
                                }
                            }
                        }
                        Logger.i("断网重连获取队列信息成功...." + entries);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        Logger.i("断网重连获取队列信息失败....");
                    }
                });
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

    public void removeMicInList() {


        UserInfo userInfo = CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo();
        final RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;

        IMNetEaseManager.get().removeMicInList(userInfo.getUid() + "", roomInfo.getRoomId() + "", new RequestCallback() {
            @Override
            public void onSuccess(Object param) {

            }

            @Override
            public void onFailed(int code) {

            }

            @Override
            public void onException(Throwable exception) {

            }
        });


    }

    public void updataQueueExBySdk(int micPosition, String roomId, long value) {
        mHomePartyMode.updataQueueExBySdk(micPosition, roomId, null, value, false);
    }

    private final long MAX = 1000;

    public void getTodayRichestManData() {
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        long roomId = 0;
        if (roomInfo != null) {
            roomId = roomInfo.getUid();
        }
        Map<String, String> param = CommonParamUtil.getDefaultParam();
        param.put("uid", String.valueOf(roomId));
        param.put("dataType", String.valueOf(1));
        param.put("type", String.valueOf(1));
        String url = JAVA_WEB_URL + "/roomctrb/queryByType";
        OkHttpManager.getInstance().getRequest(url, param, new OkHttpManager.MyCallBack<ServiceResult<List<RoomConsumeInfo>>>() {
            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().reDaySuperRichFailure("无数据!");
                }
            }

            @Override
            public void onResponse(ServiceResult<List<RoomConsumeInfo>> response) {
                //后台返回数据index第一为神豪
                if (response != null && !ListUtils.isListEmpty(response.getData()) && response.getData().get(0).getSumGold() >= MAX) {
                    getMvpView().reDaySuperRich(response.getData().get(0));
                } else {
                    onError(new Exception());
                }
            }
        });
    }
    public void eliminateMeiLi() {
        long uid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
        RoomInfo currentRoomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        Map<String, String> param = CommonParamUtil.getDefaultParam();
        param.put("roomUid", currentRoomInfo.getUid() + "");
        param.put("uid", uid+"");
        param.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        OkHttpManager.getInstance().getRequest(UriProvider.receiveRoomMicMsg(), param, new OkHttpManager.MyCallBack<Json>() {
            @Override
            public void onError(Exception e) {
            }

            @Override
            public void onResponse(Json response) {
                if (response != null) {
                    JSONObject jsonObject = JSON.parseObject(response.toString());
                    int code = jsonObject.getIntValue("code");
                    if (code == 200) {
                        ToastUtils.showLong("魅力值清除成功");
                        IMNetEaseManager.get().systemNotificationBySdk(uid, CUSTOM_MSG_ROOM_CLEAN_MEILI , -1);
                    } else {
                        String message = jsonObject.getString("message");
                        ToastUtils.showLong(message);
                    }
                } else {
                    onError(new Exception());
                }
            }
        });
    }

//    /**
//     * 排行榜由于model依赖顺序，神豪榜接口在此增加*******
//     * 神豪榜
//     * type 排行榜类型 0巨星榜，1贵族榜，2房间榜
//     * dateType 榜单周期类型 0日榜，1周榜，2总榜
//     */
//    public void mHomePartyMode(int type, int dateType) {
//        mHomePartyMode.getRankingList(type, dateType, new OkHttpManager.MyCallBack<List<RankingInfo>>() {
//            @Override
//            public void onError(Exception e) {
//
//            }
//
//            @Override
//            public void onResponse(List<RankingInfo> response) {
//                //后台返回数据index第一为神豪
//                if (response != null)
//                    getMvpView().reDaySuperRich(response.get(0));
//            }
//        });
//    }
}
