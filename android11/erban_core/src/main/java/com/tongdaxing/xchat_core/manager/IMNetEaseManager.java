package com.tongdaxing.xchat_core.manager;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nimlib.sdk.NIMChatRoomSDK;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.util.api.RequestResult;
import com.orhanobut.logger.Logger;
import com.tongdaxing.erban.libcommon.listener.CallBack;
import com.tongdaxing.erban.libcommon.listener.IDisposableAddListener;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.erban.libcommon.net.statistic.StatisticManager;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.PreferencesUtils;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.bean.ChatRoomMessage;
import com.tongdaxing.xchat_core.bean.IMChatRoomMember;
import com.tongdaxing.xchat_core.bean.RoomMicInfo;
import com.tongdaxing.xchat_core.bean.RoomQueueInfo;
import com.tongdaxing.xchat_core.bean.attachmsg.RoomQueueMsgAttachment;
import com.tongdaxing.xchat_core.gift.IGiftCore;
import com.tongdaxing.xchat_core.im.avroom.IAVRoomCoreClient;
import com.tongdaxing.xchat_core.im.custom.bean.AuctionAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachParser;
import com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.MicInListAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.PkCustomAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.RoomCharmAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.RoomRuleAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.RoomTipAttachment;
import com.tongdaxing.xchat_core.im.room.IIMRoomCoreClient;
import com.tongdaxing.xchat_core.pk.bean.PkVoteInfo;
import com.tongdaxing.xchat_core.room.auction.bean.AuctionInfo;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.room.face.IFaceCore;
import com.tongdaxing.xchat_core.room.model.AuctionModel;
import com.tongdaxing.xchat_core.room.model.AvRoomModel;
import com.tongdaxing.xchat_core.room.model.RoomBaseModel;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.data.BaseConstants;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.tongdaxing.xchat_framework.im.IMError;
import com.tongdaxing.xchat_framework.im.IMKey;
import com.tongdaxing.xchat_framework.im.IMProCallBack;
import com.tongdaxing.xchat_framework.im.IMReportRoute;
import com.tongdaxing.xchat_framework.util.config.BasicConfig;
import com.tongdaxing.xchat_framework.util.util.Json;
import com.tongdaxing.xchat_framework.util.util.LogUtils;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;
import com.tongdaxing.xchat_framework.util.util.SpUtils;
import com.tongdaxing.xchat_framework.util.util.StringUtils;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import io.agora.rtc.Constants;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

import static com.tongdaxing.xchat_core.Constants.MESSAGE_COUNT_LOCAL_LIMIT;
//import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_ADD_ROOM_BLACK;
//import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_ALLOW_MIC_POISITION;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_CHANGE_ROOM_LOCK;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_CHANGE_ROOM_NAME;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_CHANGE_ROOM_NO_LOCK;
//import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_CLOSE_MIC_POISITION;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_FINGER_GUESSING_GAME_FIRST;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_FIRST_ROOM_CHARM;
//import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_FORBIDDEN_MIC_POISITION;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_AUCTION;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_FACE;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_GIFT;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_MATCH;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_MULTI_GIFT;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_PK_FIRST;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_QUEUE;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_QUEUE_INVITE;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_QUEUE_KICK;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_REMOVE_MSG_FILTER_CLOSE;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_REMOVE_MSG_FILTER_OPEN;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_RIDE_GIFT_EFFECT_CLOSE;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_RIDE_GIFT_EFFECT_OPEN;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_ROOM_TIP;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_SUB_GIFT_EFFECT_CLOSE;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_SUB_GIFT_EFFECT_OPEN;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_SUPER_GIFT;
//import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_LET_SOMEONE_OUT_ROOM;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_LOTTERY_BOX;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_MIC_IN_LIST;
//import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_OPEN_MIC_POISITION;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_ROOM_CALL_GIFT;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_ROOM_CLEAN_MEILI;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_SECOND_ROOM_CHARM_UPDATE;
//import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_SET_MANAGER_CLOSE;
//import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_SET_MANAGER_OPEN;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_SUB_PUBLIC_CHAT_ROOM;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_TYPE_BURST_GIFT;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_TYPE_RULE_FIRST;
import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_WAN_FA;

/**
 * <p>云信聊天室管理，一个全局的Model </p>
 *
 * @author jiahui
 * @date 2017/12/12
 */
public final class IMNetEaseManager {
    private static final String TAG = "IMNetEaseManager";
    private static final Object SYNC_OBJECT = new Object();
    private static volatile IMNetEaseManager sManager;
    private final AvRoomModel model;
    public List<ChatRoomMessage> messages;
    private int beforeDisConnectionMuteStatus; // 0 默认状态 1是断网前静音，-1是断网前非静音
    private boolean imRoomConnection = true;//房间IM连接状态
    public JSONObject roomCharmList;

    public static IMNetEaseManager get() {
        if (sManager == null) {
            synchronized (SYNC_OBJECT) {
                if (sManager == null) {
                    sManager = new IMNetEaseManager();
                }
            }
        }
        return sManager;
    }

    private IMNetEaseManager() {
        registerInComingRoomMessage();
        registerKickoutEvent();
//        registerOnlineStatusChange();
        messages = new CopyOnWriteArrayList<>();
        model = new AvRoomModel();
    }

    private void registerKickoutEvent() {
//        Observer<ChatRoomKickOutEvent> kickOutObserver = new Observer<ChatRoomKickOutEvent>() {
//            @Override
//            public void onEvent(ChatRoomKickOutEvent chatRoomKickOutEvent) {
//                LogUtil.e(TAG, "收到踢人信息");
//                // 提示被踢出的原因（聊天室已解散、被管理员踢出、被其他端踢出等
//                Map<String, Object> extension = chatRoomKickOutEvent.getExtension();
//                String account = null;
//                if (extension != null) {
//                    account = (String) extension.get("account");
//                }
//                noticeKickOutChatMember(chatRoomKickOutEvent, account);
//                // 清空缓存数据
//                AvRoomDataManager.get().release();
//            }
//        };
//        NIMChatRoomSDK.getChatRoomServiceObserve().observeKickOutEvent(kickOutObserver, true);
    }

    private void registerInComingRoomMessage() {
        ReUsedSocketManager.get().setImNoticeMsgListener(new SocketManager.IMNoticeMsgListener() {
            @Override
            public void onNotice(Json json) {
                LogUtils.d("im_Notice", json + "");
                dealChatMessage(json);
            }

            /**
             * 重连之后先登录im->进入房间成功回调调用的方法
             */
            @Override
            public void onDisConnectEnterRoomSuc() {
                imRoomConnection = true;
                if (AvRoomDataManager.get().isOnMic(CoreManager.getCore(IAuthCore.class).getCurrentUid())) {
                    RoomQueueInfo roomQueueInfo = AvRoomDataManager.get().getRoomQueueMemberInfoMyself();
                    if (roomQueueInfo != null && roomQueueInfo.mRoomMicInfo != null && !roomQueueInfo.mRoomMicInfo.isMicMute() && !roomQueueInfo.mRoomMicInfo.isMicLock()) {
                        //恢复断网前的静音状态
                        if (beforeDisConnectionMuteStatus == -1) {
                            RtcEngineManager.get().setMute(false);
                        } else if (beforeDisConnectionMuteStatus == 1) {
                            RtcEngineManager.get().setMute(true);
                        }
                    }
                }
                beforeDisConnectionMuteStatus = 0;
                noticeRoomConnect(AvRoomDataManager.get().mCurrentRoomInfo);
            }

            @Override
            public void onDisConnection(boolean isCloseSelf) {
                imRoomConnection = false;
//                RtcEngineManager.get().leaveChannel();
                LogUtils.d("im_onDisConnection", "onDisConnection");
                //异常断开im保存静音状态
                if (!isCloseSelf) {
                    beforeDisConnectionMuteStatus = RtcEngineManager.get().isMute() ? 1 : -1;
                    RtcEngineManager.get().setMute(true);
                } else {
                    beforeDisConnectionMuteStatus = 0;
                }
            }

            @Override
            public void onLoginError(int err_code, String reason) {
                if (err_code == IMError.IM_ERROR_LOGIN_AUTH_FAIL || err_code == IMError.IM_ERROR_GET_USER_INFO_FAIL) {
                    Json req_data = new Json();
                    req_data.set("errno", err_code);
                    req_data.set("errmsg", reason);
                    onKickOffLogin(req_data);
                }
                SingleToastUtil.showToast(reason);
            }

            @Override
            public void onDisConntectIMLoginSuc() {
                noticeIMConnectLoginSuc();
            }
        });
    }

    private void noticeIMConnectLoginSuc() {
        getChatRoomEventObservable().onNext(new RoomEvent().setEvent(RoomEvent.SOCKET_IM_RECONNECT_LOGIN_SUCCESS));
    }

    private void dealChatMessage(Json json) {
        if (json != null) {
            Log.e("-dealChatMessage-", json.toString());
            String route = json.str(IMKey.route);
            Json req_data = json.json(IMKey.req_data);
            if (StringUtils.isNotEmpty(route) && req_data != null) {
                ChatRoomMessage msg = new ChatRoomMessage();
                msg.setRoute(route);
                if (IMReportRoute.sendMessageReport.equalsIgnoreCase(route) || IMReportRoute.sendPublicMsgNotice.equalsIgnoreCase(route)) {//处理自定义消息
                    String custom = "";
                    if (req_data != null) {
                        custom = req_data.str(IMKey.custom);
                    }
                    IMCustomAttachment IMCustomAttachment = IMCustomAttachParser.parse(custom);
                    if (IMCustomAttachment == null) {
                        return;
                    }
                    msg.setAttachment(IMCustomAttachment);
                    switch (IMCustomAttachment.getFirst()) {
                        case CUSTOM_MSG_FINGER_GUESSING_GAME_FIRST://猜拳
                            addMessages(msg);
                            break;
                        case CUSTOM_MSG_HEADER_TYPE_QUEUE://麦序列表相关自定义消息
                            dealWithMicQueueMessage(IMCustomAttachment, msg);
                            break;
                        case CUSTOM_MSG_HEADER_TYPE_AUCTION:
                            dealWithFirstAuctionMessage(IMCustomAttachment, msg);
                            break;
                        case CUSTOM_MSG_MIC_IN_LIST://排麦
                            dealWithMicInList(msg);
                            break;
                        case CUSTOM_MSG_HEADER_TYPE_FACE://表情
                            List<ChatRoomMessage> messages = new CopyOnWriteArrayList<>();
                            messages.add(msg);
                            CoreManager.getCore(IFaceCore.class).onReceiveChatRoomMessages(messages);
                            break;
                        case CUSTOM_MSG_HEADER_TYPE_MATCH://速配
                            addMessages(msg);
                            CoreManager.getCore(IFaceCore.class).onReceiveRoomMatchFace(msg);
                            break;
                        case CUSTOM_MSG_HEADER_TYPE_GIFT://单个礼物消息
                        case CUSTOM_MSG_HEADER_TYPE_MULTI_GIFT://多个和全麦礼物
                            addMessages(msg);
                        case CUSTOM_MSG_HEADER_TYPE_SUPER_GIFT://全服消息类型
                            List<ChatRoomMessage> messages2 = new CopyOnWriteArrayList<>();
                            messages2.add(msg);
                            CoreManager.getCore(IGiftCore.class).onReceiveChatRoomMessages(messages2);
                            break;
                        case CUSTOM_MSG_LOTTERY_BOX://捡海螺
                        case CUSTOM_MSG_HEADER_TYPE_ROOM_TIP://关注提示、分享提示
                        case CUSTOM_MSG_SUB_PUBLIC_CHAT_ROOM:
                        case CUSTOM_MSG_WAN_FA://玩法
                        case CUSTOM_MSG_TYPE_RULE_FIRST://玩法
                        case CUSTOM_MSG_CHANGE_ROOM_NAME://房间名称
                        case CUSTOM_MSG_ROOM_CALL_GIFT://打call送礼
                        case CUSTOM_MSG_HEADER_TYPE_PK_FIRST://PK
                        case CUSTOM_MSG_TYPE_BURST_GIFT://爆出礼物
                            addMessages(msg);
                            break;
                        case CUSTOM_MSG_FIRST_ROOM_CHARM://房间魅力值
                            if (IMCustomAttachment.getSecond() == CUSTOM_MSG_SECOND_ROOM_CHARM_UPDATE) {
                                LogUtil.d(AvRoomDataManager.TAG, "onNotice = " + json.toString());
                                if (IMCustomAttachment instanceof RoomCharmAttachment) {
                                    RoomCharmAttachment roomCharmAttachment = (RoomCharmAttachment) IMCustomAttachment;
                                    //缓存后端进入房间成功推送了最新的魅力值信息 -- 没有放入MicroViewAdapter 解决第一次进入房间推送过来后界面没有初始化完成导致无法监听而无法 显示魅力值问题
                                    if (roomCharmAttachment.getTimestamps() > AvRoomDataManager.get().getCharmTimestamps()) {
                                        AvRoomDataManager.get().addMicRoomCharmInfo(((RoomCharmAttachment) IMCustomAttachment).getLatestCharm());
                                    }
                                    noticeCharmUpdate((RoomCharmAttachment) IMCustomAttachment);
                                }
                            }
                            break;
                    }
                } else {
                    if (IMReportRoute.sendTextReport.equalsIgnoreCase(route)) {
                        dealWithTxtMessage(msg, req_data);
                    } else if (IMReportRoute.chatRoomMemberIn.equalsIgnoreCase(route)) {
                        chatRoomMemberIn(msg, req_data);
                    } else if (IMReportRoute.chatRoomMemberExit.equalsIgnoreCase(route)) {
                        chatRoomMemberExit(req_data);
                    } else if (IMReportRoute.QueueMemberUpdateNotice.equalsIgnoreCase(route)) {
                        chatRoomQueueChangeNotice(req_data);
                    } else if (IMReportRoute.ChatRoomMemberKicked.equalsIgnoreCase(route)) {
                        onKickedOutRoom(req_data);
                    } else if (IMReportRoute.QueueMicUpdateNotice.equalsIgnoreCase(route)) {
                        roomQueueMicUpdate(req_data);
                    } else if (IMReportRoute.ChatRoomInfoUpdated.equalsIgnoreCase(route)) {
                        chatRoomInfoUpdate(req_data);
                    } else if (IMReportRoute.ChatRoomMemberBlackAdd.equalsIgnoreCase(route)) {
                        //这里导致拉黑踢出不显示提示
                        addBlackList(req_data);
                    } else if (IMReportRoute.ChatRoomMemberBlackRemove.equalsIgnoreCase(route)) {

                    } else if (IMReportRoute.ChatRoomManagerAdd.equalsIgnoreCase(route)) {
                        addManagerMember(msg, req_data);
                    } else if (IMReportRoute.ChatRoomManagerRemove.equalsIgnoreCase(route)) {
                        dealWithRemoveManager(req_data);
                    } else if (IMReportRoute.kickoff.equalsIgnoreCase(route)) {
                        onKickOffLogin(req_data);
                    }
                }
            }
        }
    }


    //******************************************************** 接收各种消息的处理逻辑 **********************************************//


    /**
     * 处理麦序相关的消息接收逻辑
     *
     * @param IMCustomAttachment
     * @param msg
     */
    private void dealWithMicQueueMessage(IMCustomAttachment IMCustomAttachment, ChatRoomMessage msg) {
        RoomQueueMsgAttachment queueMsgAttachment = (RoomQueueMsgAttachment) IMCustomAttachment;
        long uid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
        if (IMCustomAttachment.getSecond() == CUSTOM_MSG_HEADER_TYPE_QUEUE_INVITE) {
            //邀請上麥
//            addMessages(msg);
            if (Objects.equals(queueMsgAttachment.uid, String.valueOf(uid))) {
                updateQueueData(queueMsgAttachment, uid);
            }
        } else if (IMCustomAttachment.getSecond() == CUSTOM_MSG_HEADER_TYPE_QUEUE_KICK) {
            //踢他下麥
//            addMessages(msg);
            if (Objects.equals(queueMsgAttachment.uid, String.valueOf(uid))) {
                int micPosition = AvRoomDataManager.get().getMicPosition(uid);
                noticeKickDownMic(micPosition);
            }
            AvRoomDataManager.get().removeMicRoomCharmInfo(queueMsgAttachment.uid);
        } else if (IMCustomAttachment.getSecond() == CUSTOM_MSG_HEADER_TYPE_REMOVE_MSG_FILTER_CLOSE
                || IMCustomAttachment.getSecond() == CUSTOM_MSG_HEADER_TYPE_REMOVE_MSG_FILTER_OPEN
                || IMCustomAttachment.getSecond() == CUSTOM_MSG_HEADER_TYPE_SUB_GIFT_EFFECT_OPEN
                || IMCustomAttachment.getSecond() == CUSTOM_MSG_HEADER_TYPE_SUB_GIFT_EFFECT_CLOSE
                || IMCustomAttachment.getSecond() == CUSTOM_MSG_HEADER_TYPE_RIDE_GIFT_EFFECT_OPEN
                || IMCustomAttachment.getSecond() == CUSTOM_MSG_HEADER_TYPE_RIDE_GIFT_EFFECT_CLOSE
                || IMCustomAttachment.getSecond() == CUSTOM_MSG_CHANGE_ROOM_LOCK//房间锁
                || IMCustomAttachment.getSecond() == CUSTOM_MSG_CHANGE_ROOM_NO_LOCK//取消锁房
                || IMCustomAttachment.getSecond() == CUSTOM_MSG_ROOM_CLEAN_MEILI//魅力值清零
//                || IMCustomAttachment.getSecond() == CUSTOM_MSG_SET_MANAGER_OPEN//设置管理员
//                || IMCustomAttachment.getSecond() == CUSTOM_MSG_SET_MANAGER_CLOSE//取消管理员
//                || IMCustomAttachment.getSecond() == CUSTOM_MSG_LET_SOMEONE_OUT_ROOM//踢出房间
//                || IMCustomAttachment.getSecond() == CUSTOM_MSG_ADD_ROOM_BLACK//加入房间黑名单
//                || IMCustomAttachment.getSecond() == CUSTOM_MSG_OPEN_MIC_POISITION//解封此座位
//                || IMCustomAttachment.getSecond() == CUSTOM_MSG_CLOSE_MIC_POISITION//封锁此座位
//                || IMCustomAttachment.getSecond() == CUSTOM_MSG_FORBIDDEN_MIC_POISITION//禁麦座位
//                || IMCustomAttachment.getSecond() == CUSTOM_MSG_ALLOW_MIC_POISITION//取消座位禁麦
        ) {
            addMessages(msg);
        }
    }

    public static final String SHOW_PASSIVITY_UP_MIC_HINT = "show_passivity_up_mic_hint";

    private void updateQueueData(RoomQueueMsgAttachment queueMsgAttachment, long uid) {
        final RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo == null) {
            return;
        }
        SpUtils.put(BasicConfig.INSTANCE.getAppContext(), SHOW_PASSIVITY_UP_MIC_HINT, true);
        RoomBaseModel roomBaseModel = new RoomBaseModel();
        roomBaseModel.upMicroPhone(queueMsgAttachment.micPosition,
                String.valueOf(uid), String.valueOf(roomInfo.getRoomId()), true, new CallBack<String>() {
                    @Override
                    public void onSuccess(String data) {
                        AvRoomDataManager.get().mIsNeedOpenMic = false;
                        noticeInviteUpMic(queueMsgAttachment.micPosition, queueMsgAttachment.uid);//更新上麦UI
                        Logger.i("用户%1$s上麦成功：%2$s", String.valueOf(uid), data);
                    }

                    @Override
                    public void onFail(int code, String error) {
                        Logger.i("用户%1$s上麦失败：%2$s----", String.valueOf(uid), error);
                    }
                });
    }

    /**
     * 目前没有发现使用的位置 先独立出来
     * 处理拍卖类型消息（拍卖房间）
     *
     * @param IMCustomAttachment
     * @param msg
     */
    private void dealWithFirstAuctionMessage(IMCustomAttachment IMCustomAttachment, ChatRoomMessage msg) {
        AuctionAttachment auctionAttachment = (AuctionAttachment) IMCustomAttachment;
        if (auctionAttachment.getSecond() == IMCustomAttachment.CUSTOM_MSG_SUB_TYPE_AUCTION_START) {
            AuctionModel.get().setAuctionInfo(auctionAttachment.getAuctionInfo());
            noticeAuctionStart(auctionAttachment.getAuctionInfo());
        } else if (auctionAttachment.getSecond() == IMCustomAttachment.CUSTOM_MSG_SUB_TYPE_AUCTION_FINISH) {
            AuctionModel.get().setAuctionInfo(null);
            noticeAuctionFinish(auctionAttachment.getAuctionInfo());
        } else if (IMCustomAttachment.CUSTOM_MSG_SUB_TYPE_AUCTION_UPDATE == auctionAttachment.getSecond()) {
            AuctionModel.get().setAuctionInfo(auctionAttachment.getAuctionInfo());
            noticeAuctionUpdate(auctionAttachment.getAuctionInfo());
        }
        addMessages(msg);
    }

    /**
     * 处理排麦相关消息
     *
     * @param msg
     */
    private void dealWithMicInList(ChatRoomMessage msg) {
        MicInListAttachment micInListAttachment = (MicInListAttachment) msg.getAttachment();
        String params = micInListAttachment.getParams();
        String key = Json.parse(params).str("key");
        SparseArray<RoomQueueInfo> mMicQueueMemberMap = AvRoomDataManager.get().mMicQueueMemberMap;
        if (!TextUtils.isEmpty(key) && mMicQueueMemberMap != null) {
            RoomQueueInfo roomQueueInfo = mMicQueueMemberMap.get(Integer.parseInt(key));
            if (roomQueueInfo != null)
                if (!roomQueueInfo.mRoomMicInfo.isMicLock())
                    micInListToUpMic(key);
        }
    }

    /**
     * 处理文本消息
     *
     * @param msg
     * @param req_data
     */
    private void dealWithTxtMessage(ChatRoomMessage msg, Json req_data) {
        Gson gson = new Gson();
        IMChatRoomMember member = null;
        try {
            member = gson.fromJson(req_data.json(IMKey.member).toString(), IMChatRoomMember.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (member == null) {
            return;
        }
        msg.setImChatRoomMember(member);
        msg.setContent(req_data.str(IMKey.content));
        addMessages(msg);
    }

    /**
     * 成员进入房间
     */
    private void chatRoomMemberIn(ChatRoomMessage msg, Json req_data) {
        Gson gson = new Gson();
        IMChatRoomMember member = null;
        try {
            member = gson.fromJson(req_data.json(IMKey.member).toString(), IMChatRoomMember.class);
            if (member != null) {
                int online_num = req_data.num("online_num");
                long timestamp = req_data.num_l("timestamp", 0);
                member.setOnline_num(online_num);
                member.setTimestamp(timestamp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (member == null) return;
        msg.setImChatRoomMember(member);
        if (AvRoomDataManager.get().isOwner(member.getAccount())) {
            AvRoomDataManager.get().mOwnerMember = member;
            AvRoomDataManager.get().mCurrentRoomInfo.setOnlineNum(member.getOnline_num());
        }
        addMessages(msg);
        CoreManager.notifyClients(IAVRoomCoreClient.class, IAVRoomCoreClient.onUserCome, msg);
        noticeRoomMemberChange(true, member.getAccount(), member);
//        if (!AvRoomDataManager.get().hashatRoomMember(member.getAccount())) {
//            AvRoomDataManager.get().mRoomAllMemberList.add(member);
//        }
//         新的IM 管理员需要通过角色类型加入对应的队列
//        if (chatRoomMember.getMemberType() == MemberType.ADMIN) {
//            addManagerMember(chatRoomMember);
//        }

    }


    /**
     * 增加管理员
     *
     * @param message
     */
    private void addManagerMember(final ChatRoomMessage message, Json req_data) {
        Gson gson = new Gson();
        IMChatRoomMember member = null;
        try {
            member = gson.fromJson(req_data.json(IMKey.member).toString(), IMChatRoomMember.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (member != null) {
            message.setImChatRoomMember(member);
            AvRoomDataManager.get().addAdminMember(member);
            if (AvRoomDataManager.get().isOwner(member.getAccount())) {
                AvRoomDataManager.get().mOwnerMember = member;
            }
            // 放在这里的原因是,只有管理员身份改变了才能发送通知
            noticeManagerChange(true, member.getAccount());
        }
    }


    /**
     * 处理移除管理员消息
     *
     * @param req_data
     */
    private void dealWithRemoveManager(Json req_data) {
        Gson gson = new Gson();
        IMChatRoomMember member = null;
        try {
            member = gson.fromJson(req_data.json(IMKey.member).toString(), IMChatRoomMember.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        removeManagerMember(member.getAccount());
    }

    /**
     * 移除管理员
     */
    private void removeManagerMember(String account) {
        AvRoomDataManager.get().removeManagerMember(account);
        noticeManagerChange(false, account);
    }

    /**
     * 加入黑名单
     *
     * @param req_data
     */
    private void addBlackList(Json req_data) {
        Gson gson = new Gson();
        IMChatRoomMember member = null;
        try {
            member = gson.fromJson(req_data.json(IMKey.member).toString(), IMChatRoomMember.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (member != null && !AvRoomDataManager.get().isOwner(member.getAccount())) {
            noticeChatMemberBlackAdd(member.getAccount());
        }

    }

    /**
     * 退出房间处理
     */
    private void chatRoomMemberExit(Json req_data) {
        String account = req_data.str("uid");
        if (StringUtils.isNotEmpty(account)) {
            //用于判断退出数量
            IMChatRoomMember chatRoomMember = new IMChatRoomMember();
            chatRoomMember.setAccount(account);
            int online_num = req_data.num("online_num");
            long timestamp = req_data.num_l("timestamp", 0);
            chatRoomMember.setOnline_num(online_num);
            chatRoomMember.setTimestamp(timestamp);
            noticeRoomMemberChange(false, account, chatRoomMember);

            RoomInfo mCurrentRoomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
            if (mCurrentRoomInfo == null) return;
            AvRoomDataManager.get().mCurrentRoomInfo.setOnlineNum(online_num);
            if (AvRoomDataManager.get().isOnMic(Long.valueOf(account))) {
                //在麦上的要退出麦
                LogUtils.d("nim_sdk", "chatRoomMemberExit     " + account);
                downMicroPhoneBySdk(AvRoomDataManager.get().getMicPosition(Long.valueOf(account)), null);
                SparseArray<RoomQueueInfo> mMicQueueMemberMap = AvRoomDataManager.get().mMicQueueMemberMap;
                int size = mMicQueueMemberMap.size();
                for (int i = 0; i < size; i++) {
                    RoomQueueInfo roomQueueInfo = mMicQueueMemberMap.valueAt(i);
                    if (roomQueueInfo.mChatRoomMember != null
                            && Objects.equals(roomQueueInfo.mChatRoomMember.getAccount(), account)) {
                        roomQueueInfo.mChatRoomMember = null;
                        LogUtils.d("remove  mChatRoomMember", 3 + "");
                        noticeDownMic(String.valueOf(mMicQueueMemberMap.keyAt(i)), account);
                        break;
                    }
                }
            }
//            removeManagerMember(account);
            AvRoomDataManager.get().removeMicRoomCharmInfo(account);
        }
    }

    /**
     * 踢出房间操作
     *
     * @param req_data
     */
    private void onKickedOutRoom(Json req_data) {
//        chatRoomMemberExit(req_data);
        String account = req_data.str("uid");
        if (AvRoomDataManager.get().isOwner(account)) {//是踢自己
            int reason_no = req_data.num("reason_no");
            String reason_msg = req_data.str("reason_msg");
            // 提示被踢出的原因（聊天室已解散、被管理员踢出、被其他端踢出等
            noticeKickOutChatMember(reason_no, reason_msg, account);
            // 清空缓存数据
            AvRoomDataManager.get().release();
        }
    }

    /**
     * 踢出账号
     *
     * @param req_data
     */
    private void onKickOffLogin(Json req_data) {
//        chatRoomMemberExit(req_data);
        int reason_no = req_data.num("errno");
        String reason_msg = req_data.str("errmsg");
        // 提示被踢出的原因（聊天室已解散、被管理员踢出、被其他端踢出等
        noticeKickOutChatMember(reason_no, reason_msg, CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        // 清空缓存数据
        AvRoomDataManager.get().release();
        CoreManager.getCore(IAuthCore.class).logout();
        PreferencesUtils.setFristQQ(true);
    }

    /**
     * 队列麦坑状态更新
     *
     * @param req_data
     */
    private void roomQueueMicUpdate(Json req_data) {
        String micInfo = req_data.str("mic_info");
        LogUtil.d(AvRoomDataManager.TAG, "roomQueueMicUpdate ---> position 坑位 " +
                "posState ---> 0：开锁，1：闭锁" +
                "micStat --->  0：开麦，1：闭麦" +
                "\n micInfo = " + micInfo);
        if (!TextUtils.isEmpty(micInfo)) {
            Gson gson = new Gson();
            RoomMicInfo roomMicInfo = gson.fromJson(micInfo, RoomMicInfo.class);
            if (roomMicInfo != null) {
                int position = roomMicInfo.getPosition();
                RoomQueueInfo roomQueueInfo = AvRoomDataManager.get().mMicQueueMemberMap.get(position);
                if (roomQueueInfo != null) {
                    //解锁麦位
                    if (roomQueueInfo.mRoomMicInfo.isMicLock() && !roomMicInfo.isMicLock()) {
                        micInListToUpMic(position + "");
                    }
                    roomQueueInfo.mRoomMicInfo = roomMicInfo;
                    //处理声网声音相关的
                    if (roomQueueInfo.mChatRoomMember != null) {
                        if (AvRoomDataManager.get().isOwner(roomQueueInfo.mChatRoomMember.getAccount())) {
                            RtcEngineManager.get().setRole(
                                    roomQueueInfo.mRoomMicInfo.isMicMute() ? Constants.CLIENT_ROLE_AUDIENCE : Constants.CLIENT_ROLE_BROADCASTER);
                        }
                    }
                    noticeMicPosStateChange(position + 1, roomQueueInfo);
                }
            }
        }
    }


    /**
     * 房间信息更新
     *
     * @param req_data -
     */
    private void chatRoomInfoUpdate(Json req_data) {
        Gson gson = new Gson();
        RoomInfo roomInfo = null;
        try {
            roomInfo = gson.fromJson(req_data.json("room_info").toString(), RoomInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (roomInfo != null) {
            AvRoomDataManager.get().mCurrentRoomInfo = roomInfo;
//                LogUtil.d("RoomSettingActivity2", AvRoomDataManager.get().mCurrentRoomInfo + "");
            noticeRoomInfoUpdate(roomInfo);
        }
    }

    /**
     * 麦序更新
     *
     * @param req_data
     */
    private void chatRoomQueueChangeNotice(Json req_data) {
        if (req_data == null)
            return;
        //排麦的key是用uid，所以length大于2
        int key = req_data.num("key");
        boolean isMicInList = (key >= 10);
        Json content = req_data.json("value");
        switch (req_data.num("type")) {
            case 1:
                LogUtil.d(AvRoomDataManager.TAG, "chatRoomQueueChangeNotice ---> type(1：更新key 2：删除) = " + 1);
                if (isMicInList) {
                    addMicInList(key, content);
                } else {
                    upMicroQueue(content, key);
                }
                break;
            case 2:
                LogUtil.d(AvRoomDataManager.TAG, "chatRoomQueueChangeNotice ---> type(1：更新key 2：删除) = " + 2);
                if (isMicInList) {
                    removeMicInList(key + "");
                } else {
                    downMicroQueue(key + "");
                }
                break;
            default:
        }
    }

    /**
     * 上麦
     *
     * @param content --
     */
    private synchronized void upMicroQueue(Json content, final int key) {
        final SparseArray<RoomQueueInfo> mMicQueueMemberMap = AvRoomDataManager.get().mMicQueueMemberMap;
        if (content != null) {
            Gson gson = new Gson();
            IMChatRoomMember chatRoomMember = null;
            try {
                LogUtil.d(AvRoomDataManager.TAG, "upMicroQueue ---> content" + content.toString());
                chatRoomMember = gson.fromJson(content.toString(), IMChatRoomMember.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (chatRoomMember == null) {
                return;
            }
            int micPosition = key;
            RoomQueueInfo roomQueueInfo = mMicQueueMemberMap.get(micPosition);
            if (roomQueueInfo == null) {
                return;
            }
//            IMChatRoomMember chatRoomMember = parseChatRoomMember(content, roomQueueInfo);
            int size = mMicQueueMemberMap.size();
            if (size > 0) {
                for (int j = 0; j < size; j++) {
                    RoomQueueInfo temp = mMicQueueMemberMap.valueAt(j);
                    if (temp.mChatRoomMember != null
                            && Objects.equals(temp.mChatRoomMember.getAccount(), chatRoomMember.getAccount())) {
                        //处理同一个人换坑问题
                        temp.mChatRoomMember = null;
                    }
                }
            }
            RoomQueueInfo tempRoomQueueInfo = mMicQueueMemberMap.get(micPosition);
            if (tempRoomQueueInfo != null && tempRoomQueueInfo.mChatRoomMember != null
                    && !Objects.equals(tempRoomQueueInfo.mChatRoomMember.getAccount(), chatRoomMember.getAccount())) {
                //被挤下麦的情况
                noticeDownCrowdedMic(micPosition, tempRoomQueueInfo.mChatRoomMember.getAccount());
            }
            roomQueueInfo.mChatRoomMember = chatRoomMember;
            //重新更新队列，队列上是否还有自己
            if (!AvRoomDataManager.get().isOwnerOnMic()) {
                //处理可能自己被挤下还能说话的情况
                RtcEngineManager.get().setRole(Constants.CLIENT_ROLE_AUDIENCE);
            }
            if (AvRoomDataManager.get().isOwner(chatRoomMember.getAccount())) {
                if (!roomQueueInfo.mRoomMicInfo.isMicMute()) {
                    //开麦
                    RtcEngineManager.get().setRole(Constants.CLIENT_ROLE_BROADCASTER);
                    //是否需要开麦
                    if (!AvRoomDataManager.get().mIsNeedOpenMic) {
                        //闭麦
                        RtcEngineManager.get().setMute(true);
                        AvRoomDataManager.get().mIsNeedOpenMic = true;
                    }
                } else {
                    RtcEngineManager.get().setRole(Constants.CLIENT_ROLE_AUDIENCE);
                }
            }
            micInListToDownMic(chatRoomMember.getAccount());
            noticeUpMic(key, chatRoomMember.getAccount());
        } else {
            RtcEngineManager.get().setRole(Constants.CLIENT_ROLE_AUDIENCE);
        }
    }

    /**
     * 下麦
     *
     * @param key --
     */
    private void downMicroQueue(String key) {
        LogUtil.d(AvRoomDataManager.TAG, "downMicroQueue ---> key(麦位) = " + key);
        SparseArray<RoomQueueInfo> mMicQueueMemberMap = AvRoomDataManager.get().mMicQueueMemberMap;
        if (!TextUtils.isEmpty(key) && mMicQueueMemberMap != null) {
            RoomQueueInfo roomQueueInfo = mMicQueueMemberMap.get(Integer.parseInt(key));
            if (roomQueueInfo != null && roomQueueInfo.mChatRoomMember != null) {
                String account = roomQueueInfo.mChatRoomMember.getAccount();
                if (AvRoomDataManager.get().isOwner(account)) {
                    // 更新声网闭麦 ,发送状态信息
                    RtcEngineManager.get().setRole(Constants.CLIENT_ROLE_AUDIENCE);
                    AvRoomDataManager.get().mIsNeedOpenMic = true;
                }
                AvRoomDataManager.get().removeMicRoomCharmInfo(account);
                roomQueueInfo.mChatRoomMember = null;
                // 通知界面更新麦序信息
                noticeDownMic(key, account);
                //排麦
                if (!roomQueueInfo.mRoomMicInfo.isMicLock())
                    micInListToUpMic(key);
            }
        }
    }


    //******************************************************** 消息发送方法 ******************************************************//

    /**
     * 进入房间的提醒消息
     *
     * @return
     */
    private ChatRoomMessage getFirstMessageContent() {
        String content = "系统通知：官方倡导绿色健康的互动体验，严禁传送色情、赌博、政治等不良信息，一经发现，封停账号。";
        ChatRoomMessage message = new ChatRoomMessage();
        message.setRoute(IMReportRoute.ChatRoomTip);
        message.setContent(content);
        return message;
    }

    /**
     * 发送房间规则消息
     */
    public void sendRoomRulesMessage() {
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo == null) {
            return;
        }
        if (StringUtils.isEmpty(roomInfo.getPlayInfo()))
            return;
        RoomRuleAttachment rules = new RoomRuleAttachment(IMCustomAttachment.CUSTOM_MSG_TYPE_RULE_FIRST, IMCustomAttachment.CUSTOM_MSG_TYPE_RULE_FIRST);
        rules.setRule(roomInfo.getPlayInfo());
        ChatRoomMessage message = new ChatRoomMessage();
        message.setRoute(IMReportRoute.sendMessageReport);
        message.setRoom_id(roomInfo.getRoomId() + "");
        message.setAttachment(rules);
        messages.add(message);
        noticeReceiverMessageImmediately(message);
    }


    /**
     * 发送文本信息
     *
     * @param message -
     */
    public void sendTextMsg(long roomId, String message, IMProCallBack imProCallBack) {
        if (TextUtils.isEmpty(message) || TextUtils.isEmpty(message.trim()))
            return;
        final ChatRoomMessage chatRoomMessage = new ChatRoomMessage(String.valueOf(roomId), message);
        chatRoomMessage.setRoute(IMReportRoute.sendTextReport);
        chatRoomMessage.setImChatRoomMember(getCurrentChatMember());
        ReUsedSocketManager.get().sendTxtMessage(roomId + "", chatRoomMessage, imProCallBack);
    }

    /**
     * 发送公屏上的Tip信息
     * 子协议一: 关注房主提示- IMCustomAttachment.CUSTOM_MSG_SUB_TYPE_ROOM_TIP_SHARE_ROOM
     * 子协议二: 分享房间成功的提示- IMCustomAttachment.CUSTOM_MSG_SUB_TYPE_ROOM_TIP_ATTENTION_ROOM_OWNER
     *
     * @param targetUid -
     * @param subType   -发送公屏上Tip信息的子协议
     */
    public void sendTipMsg(long targetUid, int subType) {
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        UserInfo userInfo = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(targetUid);
        long myUid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
        UserInfo myUserInfo = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(myUid);
        if (roomInfo != null && userInfo != null && myUserInfo != null) {
            RoomTipAttachment roomTipAttachment = new RoomTipAttachment(
                    CUSTOM_MSG_HEADER_TYPE_ROOM_TIP,
                    subType
            );
            roomTipAttachment.setUid(myUid);
            roomTipAttachment.setNick(myUserInfo.getNick());
            roomTipAttachment.setTargetUid(targetUid);
            roomTipAttachment.setTargetNick(userInfo.getNick());
            ChatRoomMessage message = new ChatRoomMessage();
            message.setRoom_id(roomInfo.getRoomId() + "");
            message.setAttachment(roomTipAttachment);
            ReUsedSocketManager.get().sendCustomMessage(roomInfo.getRoomId() + "", message, new IMSendCallBack() {
                @Override
                public void onSuccess(String data) {

                }

                @Override
                public void onError(int errorCode, String errorMsg) {

                }
            });
        }
    }


    private void sendMicInListNimMsg(final String key) {
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        MicInListAttachment micInListAttachment = new MicInListAttachment(CUSTOM_MSG_MIC_IN_LIST, CUSTOM_MSG_MIC_IN_LIST);
        Json json = new Json();
        json.set("key", key);
        micInListAttachment.setParams(json + "");
        final ChatRoomMessage message = new ChatRoomMessage();
        message.setRoom_id(roomInfo.getRoomId() + "");
        message.setAttachment(micInListAttachment);
        ReUsedSocketManager.get().sendCustomMessage(roomInfo.getRoomId() + "", message, new IMSendCallBack() {
            @Override
            public void onSuccess(String data) {
                if (message != null) {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            micInListToUpMic(key);
                        }
                    }, 300);
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {

            }
        });
    }

    /**
     * 邀请上麦的自定义消息
     *
     * @param micUid   上麦用户uid
     * @param position 要上麦的位置
     */
    public void inviteMicroPhoneBySdk(final String nickName, final long micUid, final int position) {
        final RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        //上麦条件:游客不在麦上 或者是管理员，房主
        if ((!AvRoomDataManager.get().isOnMic(micUid)
                || AvRoomDataManager.get().isRoomOwner()
                || AvRoomDataManager.get().isRoomAdmin())
                && position != Integer.MIN_VALUE) {
            RoomQueueMsgAttachment queueMsgAttachment = new RoomQueueMsgAttachment(CUSTOM_MSG_HEADER_TYPE_QUEUE,
                    CUSTOM_MSG_HEADER_TYPE_QUEUE_INVITE);
            queueMsgAttachment.uid = String.valueOf(micUid);//被抱上麦用户的uid
            queueMsgAttachment.micPosition = position;
            queueMsgAttachment.adminOrManagerUid = CoreManager.getCore(IAuthCore.class).getCurrentUid() + "";//群主或者管理员uid
            queueMsgAttachment.micName = nickName;
            ChatRoomMessage message = new ChatRoomMessage();
            message.setRoom_id(roomInfo.getRoomId() + "");
            message.setAttachment(queueMsgAttachment);
            ReUsedSocketManager.get().sendCustomMessage(roomInfo.getRoomId() + "", message, new IMSendCallBack() {
                @Override
                public void onSuccess(String data) {

                }

                @Override
                public void onError(int errorCode, String errorMsg) {

                }
            });
        }
    }

    /**
     * 踢人下麦
     *
     * @param micUid 被踢用户uid
     * @param roomId 房间ID
     */
    public void kickMicroPhoneBySdk(final String nickName, long micUid, long roomId) {
//        RoomQueueMsgAttachment queueMsgAttachment = new RoomQueueMsgAttachment(CUSTOM_MSG_HEADER_TYPE_QUEUE,
//                CUSTOM_MSG_HEADER_TYPE_QUEUE_KICK);
//        queueMsgAttachment.uid = String.valueOf(micUid);
//        queueMsgAttachment.adminOrManagerUid = CoreManager.getCore(IAuthCore.class).getCurrentUid() + "";//群主或者管理员uid
//        queueMsgAttachment.micName = nickName;
//        ChatRoomMessage message = new ChatRoomMessage();
//        message.setRoom_id(String.valueOf(roomId));
//        message.setAttachment(queueMsgAttachment);
//        ReUsedSocketManager.get().sendCustomMessage(roomId + "", message, new IMSendCallBack() {
//            @Override
//            public void onSuccess(String data) {
//
//            }
//
//            @Override
//            public void onError(int errorCode, String errorMsg) {
//
//            }
//        });
    }


    /**
     * 发送开启和关闭屏蔽小礼物特效消息和屏蔽公屏消息
     *
     * @param uid
     * @param second
     * @param micIndex
     */
    public void systemNotificationBySdk(long uid, int second, int micIndex) {
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;

        if (roomInfo == null) {
            return;
        }
        long roomId = roomInfo.getRoomId();
        RoomQueueMsgAttachment queueMsgAttachment = new RoomQueueMsgAttachment(CUSTOM_MSG_HEADER_TYPE_QUEUE, second);
        queueMsgAttachment.uid = String.valueOf(uid);
        if (micIndex != -1) {
            queueMsgAttachment.micPosition = micIndex;
        }
        final ChatRoomMessage message = new ChatRoomMessage();
        message.setRoom_id(String.valueOf(roomId));
        message.setAttachment(queueMsgAttachment);
        ReUsedSocketManager.get().sendCustomMessage(String.valueOf(roomId), message, new IMSendCallBack() {
            @Override
            public void onSuccess(String data) {
//                addMessagesImmediately(message);
            }

            @Override
            public void onError(int errorCode, String errorMsg) {

            }
        });
    }

    /**
     * 发送Pk消息
     *
     * @param second
     * @param pkVoteInfo
     */
    public void sendPkNotificationBySdk(int first, int second, PkVoteInfo pkVoteInfo) {
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo == null) {
            return;
        }
        String roomId = roomInfo.getRoomId() + "";
        PkCustomAttachment pkCustomAttachment = new PkCustomAttachment(first, second);
        pkCustomAttachment.setPkVoteInfo(pkVoteInfo);
        ChatRoomMessage message = new ChatRoomMessage();
        message.setRoom_id(String.valueOf(roomId));
        message.setAttachment(pkCustomAttachment);
        ReUsedSocketManager.get().sendCustomMessage(roomId, message, new IMSendCallBack() {
            @Override
            public void onSuccess(String data) {

            }

            @Override
            public void onError(int errorCode, String errorMsg) {

            }
        });
    }

    // 新的IM 排麦逻辑需要修改
    public void addMicInList(int key, Json content) {
//        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(content))
//            return;
//        int keyInt = Integer.parseInt(key);
//        JsonParser jsonParser = new JsonParser();
//        JsonObject valueJsonObj = jsonParser.parse(content).getAsJsonObject();
//        if (valueJsonObj == null)
//            return;
//        SparseArray<Json> mMicInListMap = AvRoomDataManager.get().mMicInListMap;
//        Json json = mMicInListMap.get(keyInt);
//        if (json == null) {
//            json = new Json();
//        }
//        Set<String> strings = valueJsonObj.keySet();
//
//
//        for (String jsonKey : strings) {
//            json.set(jsonKey, valueJsonObj.get(jsonKey).getAsString());
//        }
//
//
//        AvRoomDataManager.get().addMicInListInfo(key, json);
//        noticeMicInList();
    }

    private void removeMicInList(String key) {
        AvRoomDataManager.get().removeMicListInfo(key);
        noticeMicInList();

    }

    /**
     * 进入聊天室
     */
    public void joinAvRoom() {
        RoomInfo curRoomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        noticeEnterMessages();
        handler.removeMessages(0);
        handler.sendEmptyMessageDelayed(0, BaseConstants.SEND_STATISTICS_INTERVAL);
        if (curRoomInfo != null) {
            long uid = CoreManager.getCore(IAuthCore.class).getCurrentUid();

            model.userRoomIn(String.valueOf(uid), curRoomInfo.getUid());
        }
    }

    public void addMessagesImmediately(ChatRoomMessage msg) {
        if (messages.size() == 0) {
            ChatRoomMessage firstMessageContent = getFirstMessageContent();
            messages.add(firstMessageContent);
            noticeReceiverMessageImmediately(firstMessageContent);
            sendRoomRulesMessage();
        }
        messages.add(msg);
        limitMessageMaxCount();
        noticeReceiverMessageImmediately(msg);
    }

    private void limitMessageMaxCount() {
        if (messages.size() > MESSAGE_COUNT_LOCAL_LIMIT) {
            messages.remove(0);
        }
    }

    private void addMessages(ChatRoomMessage msg) {
        if (messages.size() == 0) {
            ChatRoomMessage firstMessageContent = getFirstMessageContent();
            messages.add(firstMessageContent);
            noticeReceiverMessage(firstMessageContent);
            sendRoomRulesMessage();
        }
        messages.add(msg);
        limitMessageMaxCount();
        noticeReceiverMessage(msg);
    }

    private PublishProcessor<RoomEvent> roomProcessor;
    private PublishSubject<ChatRoomMessage> msgProcessor;

    @Deprecated
    /**
     * this method has been replaced by
     * {@code com.tongdaxing.xchat_core.manager.IMNetEaseManager.subscribeChatRoomMsgFlowable(Consumer<List<ChatRoomMessage>> chatMsg,IDisposableAddListener iDisposableAddListener)}
     */
    public Observable<List<ChatRoomMessage>> getChatRoomMsgFlowable() {
        return getChatRoomMsgPublisher().toFlowable(BackpressureStrategy.BUFFER)
                .toObservable().buffer(200, TimeUnit.MILLISECONDS, 20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void subscribeChatRoomMsgFlowable(Consumer<List<ChatRoomMessage>> chatMsg, IDisposableAddListener iDisposableAddListener) {
        Disposable disposable = getChatRoomMsgFlowable().subscribe(chatMsg);
        if (iDisposableAddListener != null)
            iDisposableAddListener.addDisposable(disposable);
    }

    private PublishSubject<ChatRoomMessage> getChatRoomMsgPublisher() {
        if (msgProcessor == null) {
            synchronized (IMNetEaseManager.class) {
                if (msgProcessor == null) {
                    msgProcessor = PublishSubject.create();
                    msgProcessor.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                }
            }
        }
        return msgProcessor;
    }

    @Deprecated
    /**
     * this method has been replaced by
     * {@code com.tongdaxing.xchat_core.manager.IMNetEaseManager.subscribeChatRoomEventObservable(Consumer<RoomEvent> roomEvent,IDisposableAddListener iDisposableAddListener)}
     */
    public PublishProcessor<RoomEvent> getChatRoomEventObservable() {
        if (roomProcessor == null) {
            synchronized (IMNetEaseManager.class) {
                if (roomProcessor == null) {
                    roomProcessor = PublishProcessor.create();
                    roomProcessor.subscribeOn(AndroidSchedulers.mainThread())
                            .observeOn(AndroidSchedulers.mainThread());
                }
            }
        }
        return roomProcessor;
    }

    public void subscribeChatRoomEventObservable(Consumer<RoomEvent> roomEvent, IDisposableAddListener iDisposableAddListener) {
        Disposable disposable = getChatRoomEventObservable().subscribe(roomEvent);
        if (iDisposableAddListener != null)
            iDisposableAddListener.addDisposable(disposable);
    }

    public void clear() {
        handler.removeCallbacksAndMessages(null);
        messages.clear();
        roomCharmList = null;//清空房间魅力值
        Log.e(IMNetEaseManager.class.getSimpleName(), "清除房间消息....");
    }

    private MessageHandler handler = new MessageHandler();


    /**
     * @param account 被操作的用户uid
     * @param is_add  1添加，0移除
     */
    public void markBlackList(String account, boolean is_add, OkHttpManager.MyCallBack myCallBack) {
        Map<String, String> params = getImDefaultParamsMap();
        if (params == null)
            return;
        params.put("account", account);
        params.put("is_add", is_add ? "1" : "0");
        OkHttpManager.getInstance().doPostRequest(UriProvider.markBlackList(), params, myCallBack);

    }

    public Map<String, String> getImDefaultParamsMap() {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo == null) {
            return null;
        }
        IAuthCore iAuthCore = CoreManager.getCore(IAuthCore.class);
        long currentUid = iAuthCore.getCurrentUid();
        String ticket = iAuthCore.getTicket();
        long roomId = roomInfo.getRoomId();
        params.put("room_id", roomId + "");
        params.put("uid", currentUid + "");
        params.put("ticket", ticket);
        return params;
    }


    /**
     * 踢出房间
     *
     * @param account    被踢出用户uid
     * @param myCallBack
     */
    public void kickMember(String account, OkHttpManager.MyCallBack myCallBack) {
        Map<String, String> params = getImDefaultParamsMap();
        if (params == null)
            return;
        params.put("account", account);
        OkHttpManager.getInstance().doPostRequest(UriProvider.kickMember(), params, myCallBack);
    }


    private static class MessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            IMNetEaseManager.get().sendStatistics();
            sendEmptyMessageDelayed(0, BaseConstants.SEND_STATISTICS_INTERVAL);//5分钟发送一次
        }
    }

    /************************云信聊天室 普通操作(每个人都可以使用的) start******************************/


    private void micInListToUpMic(final String key) {
        LogUtils.d("micInListToUpMic", "key:" + key);

        //房主不不能上
        if ("-1".equals(key))
            return;
        LogUtils.d("micInListToUpMic_!=-1", "key:" + key);
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo == null)
            return;
        //那之前更新一次队列
        //: 2018/4/27 更新
        final Json json = AvRoomDataManager.get().getMicInListTopInfo();
        if (json == null)
            return;

        final String micInListTopKey = json.str("uid");

        LogUtils.d("micInListToUpMic", micInListTopKey);

        checkMicInListUpMicSuccess(micInListTopKey, roomInfo.getRoomId(), key);

        if (!micInListTopKey.equals(CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo().getUid() + "")) {
            return;
        }

        remveMicInlistOrUpMic(key, roomInfo, micInListTopKey);
    }

    private void remveMicInlistOrUpMic(final String key, RoomInfo roomInfo, final String micInListTopKey) {
        removeMicInList(micInListTopKey, roomInfo.getRoomId() + "", new RequestCallback() {
            @Override
            public void onSuccess(Object param) {
                //移除成功报上麦,判断是否自己,如果是自动上麦

                LogUtils.d("micInListToUpMic", 1 + "");
                CoreManager.notifyClients(IAVRoomCoreClient.class, IAVRoomCoreClient.onMicInListToUpMic, Integer.parseInt(key), micInListTopKey);
                CoreManager.notifyClients(IAVRoomCoreClient.class, IAVRoomCoreClient.micInListDismiss, "");

            }

            @Override
            public void onFailed(int code) {
                LogUtils.d("micInListLogUpMic", 2 + "code:" + code);
            }

            @Override
            public void onException(Throwable exception) {
                LogUtils.d("micInListLogUpMic", 3 + "");
            }
        });
    }

    private void checkMicInListUpMicSuccess(final String micInListTopKey, final long roomId, final String key) {
        String account = getFirstMicUid();

        long currentUid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
        boolean isKicker = (currentUid + "").equals(account);
        //如果是首个在麦上的人
        if (isKicker) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Json json = AvRoomDataManager.get().getMicInListTopInfo();
                    if (json == null)
                        return;

                    String topKey = json.str("uid", "null");
                    //1.5秒后判断排麦，首位跟换没有
                    if (micInListTopKey.equals(topKey)) {

//                        remveMicInlistOrUpMic(key, roomInfo, micInListTopKey);
                        IMNetEaseManager.get().removeMicInList(micInListTopKey, roomId + "", new RequestCallback() {
                            @Override
                            public void onSuccess(Object param) {
                                //移除成功后通知别人上麦
                                sendMicInListNimMsg(key);
                            }

                            @Override
                            public void onFailed(int code) {
                                LogUtils.d("micInListToUpMiconFailed", key);

                            }

                            @Override
                            public void onException(Throwable exception) {
                                LogUtils.d("micInListToUpMiconException", key);
                            }
                        });
                        LogUtils.d("checkMicInListUpMicSuccess", "kick");

                    }
                }
            }, 1500);
        }


    }

    private String getFirstMicUid() {
        String account = "";
        SparseArray<RoomQueueInfo> mMicQueueMemberMap = AvRoomDataManager.get().mMicQueueMemberMap;

        for (int i = 0; i < mMicQueueMemberMap.size(); i++) {
            RoomQueueInfo roomQueueInfo = mMicQueueMemberMap.valueAt(i);
//
//            LogUtils.d("checkMicInListUpMicSuccess", mMicQueueMemberMap.keyAt(i) + "");
            if (roomQueueInfo == null)
                continue;
            IMChatRoomMember mChatRoomMember = roomQueueInfo.mChatRoomMember;
            if (mChatRoomMember == null)
                continue;
            else {
                account = mChatRoomMember.getAccount();
                break;
            }
        }
        return account;
    }

    public void removeMicInList(String key, String roomId, RequestCallback requestCallback) {
        NIMClient.getService(ChatRoomService.class).pollQueue(roomId, key).setCallback(requestCallback);

    }

    /**
     * 检测用户是否有推流权限
     */
    public void checkPushAuth(OkHttpManager.MyCallBack callBack) {

        Map<String, String> paramsMap = getImDefaultParamsMap();
        if (paramsMap == null) {
            return;
        }
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        paramsMap.put("room_id", String.valueOf(roomInfo.getRoomId()));
        paramsMap.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        paramsMap.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());

        OkHttpManager.getInstance().doPostRequest(UriProvider.checkPushAuth(), paramsMap, callBack);
    }

    private void micInListToDownMic(String key) {

        if (!AvRoomDataManager.get().checkInMicInlist())
            return;
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        removeMicInList(key, roomInfo.getRoomId() + "", null);
    }


    public Single<List<IMChatRoomMember>> fetchRoomMembersByIds(final List<String> accounts) {
        return Single.create(new SingleOnSubscribe<List<IMChatRoomMember>>() {
            @Override
            public void subscribe(SingleEmitter<List<IMChatRoomMember>> e) throws Exception {

                final RoomInfo mCurrentRoomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
                if (mCurrentRoomInfo == null || ListUtils.isListEmpty(accounts)) {
                    e.onError(new IllegalArgumentException("RoomInfo is null or accounts is null"));
                    CoreManager.notifyClients(IIMRoomCoreClient.class, IIMRoomCoreClient.enterError);
                    return;
                }
                RequestResult<List<com.netease.nimlib.sdk.chatroom.model.ChatRoomMember>> result = NIMClient.syncRequest(NIMChatRoomSDK.getChatRoomService()
                        .fetchRoomMembersByIds(String.valueOf(mCurrentRoomInfo.getRoomId()), accounts));
                if (result.exception != null)
                    e.onError(result.exception);
                else if (result.code != BaseMvpModel.RESULT_OK) {
                    e.onError(new Exception("错误码: " + result.code));
                } else {
                    List<IMChatRoomMember> chatRoomMembers = new CopyOnWriteArrayList<>();
                    for (int i = 0; i < result.data.size(); i++) {
                        IMChatRoomMember chatRoomMember = new IMChatRoomMember(result.data.get(i));
                        chatRoomMembers.add(chatRoomMember);
                    }
                    e.onSuccess(chatRoomMembers);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /************************云信聊天室 普通操作 end******************************/

    /************************云信聊天室 房主/管理员操作 begin******************************/


    /**
     * 设置管理员
     *
     * @param is_add     1加，0移除
     * @param account    要设置的管理员id
     * @param myCallBack
     */
    //: 2018/11/13 需要后端通知
    public void markManager(final String account, final boolean is_add, final OkHttpManager.MyCallBack myCallBack) {
        Map<String, String> params = getImDefaultParamsMap();
        if (params == null)
            return;
        params.put("is_add", is_add ? "1" : "0");
        params.put("account", account);
        OkHttpManager.getInstance().doPostRequest(UriProvider.markChatRoomManager(), params, myCallBack);
    }

    /**
     * 下麦
     *
     * @param micPosition -
     * @param callBack    -
     */
    public void downMicroPhoneBySdk(int micPosition, final CallBack<String> callBack) {
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo == null) return;

        if (micPosition < -1) {
            return;
        }
        //防止房主掉麦
        if (micPosition == -1) {
            String currentUid = String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid());
            if (!AvRoomDataManager.get().isRoomOwner(currentUid)) {
                return;
            }
        }
        ReUsedSocketManager.get().pollQueue(String.valueOf(roomInfo.getRoomId()), micPosition, new IMSendCallBack() {
            @Override
            public void onSuccess(String data) {
                if (callBack != null)
                    callBack.onSuccess("下麦成功");
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                if (callBack != null) {
                    callBack.onFail(errorCode, "下麦失败:" + errorMsg);
                }
            }
        });
    }

    /**
     * 发送通用的当前用户信息
     *
     * @return
     */
    public IMChatRoomMember getCurrentChatMember() {
        IMChatRoomMember member = new IMChatRoomMember();
        if (AvRoomDataManager.get().mOwnerMember != null) {//传递需要的信息
            member.setAccount(AvRoomDataManager.get().mOwnerMember.getAccount() + "");
            member.setNick(AvRoomDataManager.get().mOwnerMember.getNick());
            member.setAvatar(AvRoomDataManager.get().mOwnerMember.getAvatar());
            member.setExperLevel(AvRoomDataManager.get().mOwnerMember.getExperLevel());
            member.setCar_name(AvRoomDataManager.get().mOwnerMember.getCar_name());
            member.setIs_new_user(AvRoomDataManager.get().mOwnerMember.getIs_new_user());
        }
        return member;
    }

    /**
     * -------------------------通知begin-------------------------------
     */
    private void noticeImNetReLogin(RoomQueueInfo roomQueueInfo) {
        getChatRoomEventObservable().onNext(new RoomEvent()
                .setEvent(RoomEvent.ROOM_CHAT_RECONNECTION)
                .setRoomQueueInfo(roomQueueInfo));
    }

    private void noticeRoomMemberChange(boolean isMemberIn, String account, IMChatRoomMember imChatRoomMember) {
        RoomEvent roomEvent = new RoomEvent();
        //进入房间的时候把imChatRoomMember传过去
        if (imChatRoomMember != null) {
            ChatRoomMessage chatRoomMessage = new ChatRoomMessage();
            chatRoomMessage.setImChatRoomMember(imChatRoomMember);
            roomEvent.setChatRoomMessage(chatRoomMessage);
        }

        getChatRoomEventObservable().onNext(roomEvent
                .setAccount(account)
                .setEvent(isMemberIn ? RoomEvent.ROOM_MEMBER_IN : RoomEvent.ROOM_MEMBER_EXIT));
    }

    private void noticeManagerChange(boolean isAdd, String account) {
        getChatRoomEventObservable().onNext(new RoomEvent()
                .setEvent(isAdd ? RoomEvent.ROOM_MANAGER_ADD : RoomEvent.ROOM_MANAGER_REMOVE)
                .setAccount(account));
    }


    private void noticeReceiverMessageImmediately(ChatRoomMessage chatRoomMessage) {
        getChatRoomEventObservable().onNext(new RoomEvent()
                .setEvent(RoomEvent.RECEIVE_MSG)
                .setChatRoomMessage(chatRoomMessage)
        );
    }

    private void noticeReceiverMessage(ChatRoomMessage chatRoomMessage) {
        getChatRoomMsgPublisher().onNext(chatRoomMessage);
    }

    private void noticeEnterMessages() {
        getChatRoomEventObservable().onNext(new RoomEvent().setEvent(RoomEvent.ENTER_ROOM));
    }

    public void noticeKickOutChatMember(int reason_no, String reason_msg, String account) {
        LogUtils.d(TAG, account + ": noticeKickOutChatMember");
        getChatRoomEventObservable().onNext(new RoomEvent()
                .setEvent(RoomEvent.KICK_OUT_ROOM)
                .setReason_msg(reason_msg)
                .setReason_no(reason_no)
                .setAccount(account));
    }

    private void noticeKickDownMic(int position) {
        getChatRoomEventObservable().onNext(new RoomEvent()
                .setEvent(RoomEvent.KICK_DOWN_MIC)
                .setMicPosition(position));
    }

    private void noticeInviteUpMic(int position, String account) {
        getChatRoomEventObservable().onNext(new RoomEvent().setEvent(RoomEvent.INVITE_UP_MIC)
                .setAccount(account)
                .setMicPosition(position));
    }

    public void noticeDownMic(String position, String account) {
        getChatRoomEventObservable().onNext(new RoomEvent()
                .setEvent(RoomEvent.DOWN_MIC)
                .setAccount(account)
                .setMicPosition(Integer.valueOf(position)));
    }


    private void noticeMicPosStateChange(int position, RoomQueueInfo roomQueueInfo) {
        getChatRoomEventObservable().onNext(new RoomEvent()
                .setEvent(RoomEvent.MIC_QUEUE_STATE_CHANGE)
                .setMicPosition(position)
                .setRoomQueueInfo(roomQueueInfo));
    }

    /**
     * 发送魅力值更新通知
     *
     * @param charmAttachment
     */
    private void noticeCharmUpdate(RoomCharmAttachment charmAttachment) {
        getChatRoomEventObservable().onNext(new RoomEvent().setEvent(RoomEvent.ROOM_CHARM).setRoomCharmAttachment(charmAttachment));
    }

    private void noticeChatMemberBlackAdd(String account) {
        getChatRoomEventObservable().onNext(new RoomEvent()
                .setEvent(RoomEvent.ADD_BLACK_LIST)
                .setAccount(account));
    }


    private void noticeUpMic(int position, String account) {
        getChatRoomEventObservable().onNext(new RoomEvent()
                .setEvent(RoomEvent.UP_MIC)
                .setMicPosition(position)
                .setAccount(account)
        );
    }

    private void noticeMicInList() {
        getChatRoomEventObservable().onNext(new RoomEvent()
                .setEvent(RoomEvent.MIC_IN_LIST_UPDATE)

        );
    }

    /**
     * 被挤下麦通知
     */
    private void noticeDownCrowdedMic(int position, String account) {
        getChatRoomEventObservable().onNext(new RoomEvent()
                .setEvent(RoomEvent.DOWN_CROWDED_MIC)
                .setMicPosition(position)
                .setAccount(account)
        );
    }

    private void noticeRoomInfoUpdate(RoomInfo roomInfo) {
        getChatRoomEventObservable().onNext(new RoomEvent().setEvent(RoomEvent.ROOM_INFO_UPDATE).setRoomInfo(roomInfo));
    }

    private void noticeRoomConnect(RoomInfo roomInfo) {
        getChatRoomEventObservable().onNext(new RoomEvent().setEvent(RoomEvent.ROOM_RECONNECT).setRoomInfo(roomInfo));
    }

    private void noticeAuctionStart(AuctionInfo auctionInfo) {
        getChatRoomEventObservable().onNext(new RoomEvent().setEvent(RoomEvent.AUCTION_START).setAuctionInfo(auctionInfo));
    }

    private void noticeAuctionFinish(AuctionInfo auctionInfo) {
        getChatRoomEventObservable().onNext(new RoomEvent().setEvent(RoomEvent.AUCTION_FINISH).setAuctionInfo(auctionInfo));
    }

    private void noticeAuctionUpdate(AuctionInfo auctionInfo) {
        getChatRoomEventObservable().onNext(new RoomEvent().setEvent(RoomEvent.AUCTION_UPDATE).setAuctionInfo(auctionInfo));
    }

    private void sendStatistics() {
        RoomInfo curRoomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (curRoomInfo != null) {
            long uid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
            long roomUid = curRoomInfo.getUid();
            String ticket = CoreManager.getCore(IAuthCore.class).getTicket();
            Map<String, String> params = CommonParamUtil.getDefaultParam();
            params.put("uid", String.valueOf(uid));
            params.put("roomUid", String.valueOf(roomUid));
            params.put("time", String.valueOf(System.currentTimeMillis()));
            params.put("ticket", ticket);
            StatisticManager.get().sendStatisticToService(UriProvider.roomStatistics(), params);
        }
    }

    public int getBeforeDisConnectionMuteStatus() {
        return beforeDisConnectionMuteStatus;
    }

    public void setBeforeDisConnectionMuteStatus(int beforeDisConnectionMuteStatus) {
        this.beforeDisConnectionMuteStatus = beforeDisConnectionMuteStatus;
    }

    public boolean isImRoomConnection() {
        return imRoomConnection;
    }

    public void setImRoomConnection(boolean imNetsConnection) {
        this.imRoomConnection = imNetsConnection;
    }
}
