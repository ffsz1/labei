package com.vslk.lbgx.room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.vslk.lbgx.room.avroom.fragment.base.AbsRoomFragment;
import com.vslk.lbgx.room.avroom.fragment.AuctionFragment;
import com.vslk.lbgx.room.avroom.fragment.audio.AudioRootFragment;
import com.vslk.lbgx.room.avroom.fragment.InputPwdDialogFragment;
import com.vslk.lbgx.room.avroom.fragment.LightChatFragment;
import com.vslk.lbgx.room.avroom.other.BgTypeHelper;
import com.vslk.lbgx.ui.me.user.activity.UserInfoActivity;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_core.auth.IAuthClient;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.bean.ChatRoomMessage;
import com.tongdaxing.xchat_core.bean.RoomMemberComeInfo;
import com.tongdaxing.xchat_core.gift.IGiftCoreClient;
import com.tongdaxing.xchat_core.im.avroom.IAVRoomCore;
import com.tongdaxing.xchat_core.im.avroom.IAVRoomCoreClient;
import com.tongdaxing.xchat_core.im.custom.bean.FingerGuessingGameAttachment;
import com.tongdaxing.xchat_core.im.room.IIMRoomCoreClient;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.manager.IMNetEaseManager;
import com.tongdaxing.xchat_core.manager.RoomEvent;
import com.tongdaxing.xchat_core.redpacket.bean.ActionDialogInfo;
import com.tongdaxing.xchat_core.room.IBgClient;
import com.tongdaxing.xchat_core.room.bean.MoreUnpkFingerGuessingMsgInfo;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.room.model.FingerGuessingGameModel;
import com.tongdaxing.xchat_core.room.presenter.AvRoomPresenter;
import com.tongdaxing.xchat_core.room.view.IAvRoomView;
import com.tongdaxing.xchat_core.user.IUserClient;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.VersionsCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.im.IMReportRoute;
import com.tongdaxing.xchat_framework.util.config.BasicConfig;
import com.tongdaxing.xchat_framework.util.config.SpEvent;
import com.tongdaxing.xchat_framework.util.util.JavaUtil;
import com.tongdaxing.xchat_framework.util.util.Json;
import com.tongdaxing.xchat_framework.util.util.LogUtil;
import com.tongdaxing.xchat_framework.util.util.LogUtils;
import com.tongdaxing.xchat_framework.util.util.SpUtils;
import com.tongdaxing.xchat_framework.util.util.StringUtils;
import com.tongdaxing.xchat_framework.util.util.TimeUtils;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * 房间入口
 *
 * @author Administrator
 */
@CreatePresenter(AvRoomPresenter.class)
public class AVRoomActivity extends BaseMvpActivity<IAvRoomView, AvRoomPresenter> implements View.OnClickListener, IAvRoomView {

    public static final String TAG = "LoginRoom";

    private RelativeLayout finishLayout;
    private ViewStub mVsRoomOffline;
    private ImageView avatarBg;
    private TextView nick;
    private ImageView avatar;
    private ImageView gender;

    private long roomUid;
    private AbsRoomFragment mCurrentFragment;

    private InputPwdDialogFragment mPwdDialogFragment;
    private Disposable subscribe;

    private RoomInfo mRoomInfo;

    private View mainContainer;
    private ImageView ivBack;

    public static void start(Context context, long roomUid) {
        Log.e("onNewIntent", "AVRoomActivity - start");
        Intent intent = new Intent(context, AVRoomActivity.class);
        intent.putExtra(Constants.ROOM_UID, roomUid);
        context.startActivity(intent);
    }

    public static void start(Context context, long roomUid, int roomType) {
        Intent intent = new Intent(context, AVRoomActivity.class);
        intent.putExtra(Constants.ROOM_UID, roomUid);
        intent.putExtra(Constants.ROOM_TYPE, roomType);
        context.startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 如果是同一个房间，则只更新房间的信息
        long newRoomUid = intent.getLongExtra(Constants.ROOM_UID, 0);
        LogUtils.d("startGiftEffect", "newRoomUid" + newRoomUid);
        LogUtils.d("startGiftEffect", "roomUid" + roomUid);
        if (newRoomUid != 0 && newRoomUid == roomUid) {
            updateRoomInfo();
            return;
        }
        roomUid = newRoomUid;
        getSupportFragmentManager().beginTransaction()
                .remove(mCurrentFragment).commitAllowingStateLoss();
        mCurrentFragment = null;
        // 相同类型的房间，但是是不同人的房间
        if (AvRoomDataManager.get().isFirstEnterRoomOrChangeOtherRoom(roomUid)) {
            mainContainer.setVisibility(View.GONE);
            updateRoomInfo();
        }
    }

    private FingerGuessingGameModel fingerGuessingGameModel = new FingerGuessingGameModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        //沉浸式状态栏
        setStatusBar();
        mainContainer = findViewById(R.id.main_container);
        ivBack = (ImageView) findViewById(R.id.iv_room_back);
        //保持房间页面不息屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        roomUid = getIntent().getLongExtra(Constants.ROOM_UID, 0);
        checkIsKick(roomUid);
        setSwipeBackEnable(false);
        mVsRoomOffline = (ViewStub) findViewById(R.id.vs_room_offline);
        //修改新的注册监听方式防止内存泄露
        IMNetEaseManager.get().subscribeChatRoomEventObservable(this::onRoomEventReceive, this);
        //第一次进来
        if (AvRoomDataManager.get().isFirstEnterRoomOrChangeOtherRoom(roomUid)) {
            mainContainer.setVisibility(View.GONE);
            updateRoomInfo();
            return;
        }
        //为了避免标记未改变
        addRoomFragment(true);
    }



    /**
     * 获取更多未PK猜拳消息
     */
    private void getMoreUnpkFingerGuessingGameMsg() {
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo == null) {
            return;
        }
        fingerGuessingGameModel.getListFingerGuessingGame(roomInfo.getRoomId(), new OkHttpManager.MyCallBack<ServiceResult<List<MoreUnpkFingerGuessingMsgInfo>>>() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(ServiceResult<List<MoreUnpkFingerGuessingMsgInfo>> response) {
                if (response.isSuccess() && !ListUtils.isListEmpty(response.getData())) {
                    convertFingerGuessingGameBean(response.getData());
                }
            }
        });
    }

    private void convertFingerGuessingGameBean(List<MoreUnpkFingerGuessingMsgInfo> msgInfoList) {
        for (int i = 0; i < msgInfoList.size(); i++) {
            MoreUnpkFingerGuessingMsgInfo info = msgInfoList.get(i);
            if (info != null) {
                MoreUnpkFingerGuessingMsgInfo.DataBean dataBean = info.getData();
                if (dataBean != null) {
                    String str = dataBean.getMoraRecordMessage();
                    JSONObject object = JSON.parseObject(str);
                    FingerGuessingGameAttachment attachment = new FingerGuessingGameAttachment(info.getFirst(), info.getSecond());
                    attachment.setAvatar(object.getString("avatar"));
                    attachment.setNick(object.getString("nick"));
                    attachment.setGiftNum(object.getInteger("giftNum"));
                    attachment.setRecordId(object.getInteger("recordId"));
                    attachment.setExperienceLevel(object.getInteger("experienceLevel"));
                    if (object.containsKey("giftUrl")) {
                        attachment.setGiftUrl(object.getString("giftUrl"));
                    }
                    if (object.containsKey("opponentNick")) {
                        attachment.setOpponentNick(object.getString("opponentNick"));
                    }
                    ChatRoomMessage chatRoomMessage = new ChatRoomMessage();
                    chatRoomMessage.setAttachment(attachment);
                    chatRoomMessage.setRoute(IMReportRoute.sendMessageReport);
                    IMNetEaseManager.get().addMessagesImmediately(chatRoomMessage);
                }
            }
        }
    }

    private void checkIsKick(long roomUid) {
        int kickTime = CoreManager.getCore(VersionsCore.class).checkKick();
        if (kickTime < 1) {
            return;
        }

        if (kickTime > 600) {
            kickTime = 600;
        }
        String kickInfo = (String) SpUtils.get(this, SpEvent.onKickRoomInfo, "");
        Json json = Json.parse(kickInfo);
        String roomUidCache = json.str(SpEvent.roomUid);
        String time = json.str(SpEvent.time);
        if (roomUidCache.equals(roomUid + "")) {
            int i = kickTime * 1000;
            if (BasicConfig.INSTANCE.isDebuggable() && i > 10000) {
                i = 10000;
            }
            if (System.currentTimeMillis() - JavaUtil.str2long(time) < i) {
                toast("您被此房间踢出，请稍后再进");
                finish();
            }
        }
    }

    @Override
    public boolean blackStatusBar() {
        return false;
    }

    private void onRoomEventReceive(RoomEvent roomEvent) {
        if (roomEvent == null || roomEvent.getEvent() == RoomEvent.NONE) {
            return;
        }

        int event = roomEvent.getEvent();
        switch (event) {
            case RoomEvent.ENTER_ROOM:
                onEnterRoom();
                break;
            case RoomEvent.KICK_OUT_ROOM:
                if (StringUtils.isNotEmpty(roomEvent.getReason_msg())) {
                    toast(roomEvent.getReason_msg());
                }
                finish();
                break;
            case RoomEvent.ROOM_MANAGER_ADD:
            case RoomEvent.ROOM_MANAGER_REMOVE:
                if (AvRoomDataManager.get().isOwner(roomEvent.getAccount())) {
                    if (roomEvent.getEvent() == RoomEvent.ROOM_MANAGER_ADD) {
                        toast(R.string.set_room_manager);
                    } else if (roomEvent.getEvent() == RoomEvent.ROOM_MANAGER_REMOVE) {
                        toast(R.string.remove_room_manager);
                    }
                }
                break;
            case RoomEvent.RTC_ENGINE_NETWORK_BAD:
                toast("当前网络不稳定，请检查网络");
                break;
            case RoomEvent.PLAY_OR_PUBLISH_NETWORK_ERROR:
            case RoomEvent.RTC_ENGINE_NETWORK_CLOSE:
                toast("当前网络异常，与服务器断开连接，请检查网络");
                break;
            case RoomEvent.ROOM_INFO_UPDATE:
                setBackBg(roomEvent.getRoomInfo());
                break;
            case RoomEvent.ZEGO_RESTART_CONNECTION_EVENT:
                IMNetEaseManager.get().noticeKickOutChatMember(0, "当前网络异常，与服务器断开连接，请检查网络",
                        String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
                break;
            case RoomEvent.ZEGO_AUDIO_DEVICE_ERROR:
                toast("当前麦克风可能被占用，请检查设备是否可用");
                break;
            case RoomEvent.ROOM_MEMBER_IN:
            case RoomEvent.ROOM_MEMBER_EXIT:
                if (AvRoomDataManager.get().mCurrentRoomInfo == null) {
                    break;
                }
                if (mCurrentFragment != null) {
                    if (roomEvent.getChatRoomMessage() != null
                            && roomEvent.getChatRoomMessage().getImChatRoomMember() != null
                            && roomEvent.getChatRoomMessage().getImChatRoomMember().getTimestamp() >= AvRoomDataManager.get().getTimestamp()) {
                        AvRoomDataManager.get().setTimestamp(roomEvent.getChatRoomMessage().getImChatRoomMember().getTimestamp());
                        mCurrentFragment.onRoomOnlineNumberSuccess(roomEvent.getChatRoomMessage().getImChatRoomMember().getOnline_num());
                    }
                }
                break;
            default:
        }
    }

    private void onEnterRoom() {
        mRoomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        AvRoomDataManager.get().setStartPlayFull(true);
        mainContainer.setVisibility(View.VISIBLE);
        dimissDialog();
    }

    /**
     * @param isCreate 避免不在onCreate方法中调用commit报错IllegalStateException:
     *                 Can not perform this action after onSaveInstanceState
     */
    private void addRoomFragment(boolean isCreate) {
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo != null) {
            //获取房主信息
            IAVRoomCore core = CoreManager.getCore(IAVRoomCore.class);
            core.removeRoomOwnerInfo();
            core.requestRoomOwnerInfo(roomInfo.getUid() + "");//FIXME 这个接口要优化

            setBackBg(roomInfo);
            int roomType = roomInfo.getType();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (roomType == RoomInfo.ROOMTYPE_AUCTION &&
                    !(mCurrentFragment instanceof AuctionFragment)) {
                mCurrentFragment = new AuctionFragment();
            } else if (roomType == RoomInfo.ROOMTYPE_LIGHT_CHAT &&
                    !(mCurrentFragment instanceof LightChatFragment)) {
                mCurrentFragment = new LightChatFragment();
            } else if (roomType == RoomInfo.ROOMTYPE_HOME_PARTY &&
                    !(mCurrentFragment instanceof AudioRootFragment)) {
                mCurrentFragment = AudioRootFragment.newInstance(roomUid);
            }
            if (mCurrentFragment != null) {
//                if (isCreate) {
//                    fragmentTransaction.replace(R.id.main_container, mCurrentFragment).commit();
//                } else {
                fragmentTransaction.replace(R.id.main_container, mCurrentFragment).commitAllowingStateLoss();
//                }
            }
            getMvpPresenter().getActionDialog(2);
        }
    }


    private void setBackBg(RoomInfo roomInfo) {
        if (roomInfo != null) {
            if (StringUtils.isNotEmpty(roomInfo.getBackPicUrl())) {
                ImageLoadUtils.loadImage(this, roomInfo.getBackPicUrl(), ivBack, R.mipmap.img_music_morelist_bg, R.mipmap.img_music_morelist_bg);
            } else {
                int bgId = BgTypeHelper.getBgId(roomInfo.getBackPic());
                ImageLoadUtils.loadImageRes(this, bgId, ivBack, BgTypeHelper.getDefaultBackRes());
            }
        }
    }

    @CoreEvent(coreClientClass = IUserClient.class)
    public void onRequestUserInfo(UserInfo userInfo) {
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo != null)
            setBackBg(roomInfo);
        setUserInfo(userInfo);
    }

    @CoreEvent(coreClientClass = IBgClient.class)
    public void bgModify(String type, String bgUrl) {
        if (StringUtils.isNotEmpty(bgUrl)) {
            ImageLoadUtils.loadImage(this, bgUrl, ivBack);
        } else {
            int bgId = BgTypeHelper.getBgId(type);
            ImageLoadUtils.loadImageRes(this, bgId, ivBack, BgTypeHelper.getDefaultBackRes());
        }
    }

    private void showLiveFinishView() {
        if (mRoomInfo != null) {
            if (finishLayout == null) {
                finishLayout = (RelativeLayout) mVsRoomOffline.inflate();
                avatar = finishLayout.findViewById(R.id.avatar);
                avatarBg = finishLayout.findViewById(R.id.avatar_bg);
                nick = finishLayout.findViewById(R.id.nick);
                gender = finishLayout.findViewById(R.id.iv_gender);
            }
            finishLayout.setVisibility(View.VISIBLE);
            finishLayout.findViewById(R.id.home_page_btn).setOnClickListener(this);
            finishLayout.findViewById(R.id.back_btn).setOnClickListener(this);

            UserInfo userInfo = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(mRoomInfo.getUid());
            setUserInfo(userInfo);
            AvRoomDataManager.get().release();
        } else {
            toast("对方不在房间内");
            AvRoomDataManager.get().release();
            finish();
        }
    }

    private void setUserInfo(UserInfo userInfo) {
        if (avatarBg == null || avatar == null || nick == null) return;
        if (userInfo != null) {
            ImageLoadUtils.loadCircleImage(this, userInfo.getAvatar(), avatar, R.drawable.ic_default_avatar);
            nick.setText(userInfo.getNick());
            if (userInfo.getGender() == 1) {
                gender.setImageResource(R.drawable.icon_man);
            } else {
                gender.setImageResource(R.drawable.icon_woman);
            }
        } else {
            avatar.setImageResource(R.drawable.ic_default_avatar);
        }
    }


    private void updateRoomInfo() {
        LogUtil.d(TAG, "LoginRoom ---> updateRoomInfo ---> currentTime ---> " + TimeUtils.getDateTimeString
                (System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss:SSS"));
        getMvpPresenter().requestRoomInfoFromService(String.valueOf(roomUid));
    }


    @CoreEvent(coreClientClass = IIMRoomCoreClient.class)
    public void onUpdateMyRoomRoleFail() {
        toast("操作太频繁，请30秒后再试");
    }

    private void showRoomPwdDialog(final RoomInfo roomInfo) {
        if (mPwdDialogFragment == null) {
            mPwdDialogFragment = InputPwdDialogFragment.newInstance(getString(R.string.input_pwd), getString(R.string.ok), getString(R.string.cancel), roomInfo.getRoomPwd());
            //google的bug
            try {
                mPwdDialogFragment.show(getSupportFragmentManager(), "pwdDialog");
            } catch (Exception e) {
                finish();
            }

            mPwdDialogFragment.setOnDialogBtnClickListener(new InputPwdDialogFragment.OnDialogBtnClickListener() {
                @Override
                public void onBtnConfirm() {
                    mPwdDialogFragment.dismiss();
                    mPwdDialogFragment = null;
                    getMvpPresenter().enterRoom(roomInfo);
                }

                @Override
                public void onBtnCancel() {
                    mPwdDialogFragment.dismiss();
                    mPwdDialogFragment = null;
                    finish();
                }
            });
        }
    }

    @Override
    public void onGetActionDialog(List<ActionDialogInfo> dialogInfo) {
        if (mCurrentFragment != null && mCurrentFragment.isVisible()) {
            mCurrentFragment.onShowActivity(dialogInfo);
        }
    }

    @Override
    public void onGetActionDialogError(String error) {
    }

    @Override
    public void exitRoom(RoomInfo currentRoomInfo) {
        if (currentRoomInfo != null && currentRoomInfo.getUid() == roomUid) {
            finish();
        }
    }

    @Override
    public void onRoomOnlineNumberSuccess(int onlineNumber) {
        if (mCurrentFragment != null) {
            mCurrentFragment.onRoomOnlineNumberSuccess(onlineNumber);
        }
    }

    @CoreEvent(coreClientClass = IGiftCoreClient.class)
    public void onGiftPastDue() {
        toast("该礼物已过期");
    }

    public void toBack() {
        long myUid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomUid == myUid && roomInfo != null && roomInfo.getType() != RoomInfo.ROOMTYPE_HOME_PARTY) {
            getDialogManager().showOkCancelDialog("当前正在开播，是否要关闭直播？", true, () -> {
                getMvpPresenter().exitRoom();
                finish();
            });
        } else {
            getMvpPresenter().exitRoom();
            finish();
        }
    }


    @Override
    protected int setBgColor() {
        return R.color.black;
    }

    @Override
    protected boolean needSteepStateBar() {
        return false;
    }

    @Override
    protected void onDestroy() {
        try {
            //这里可能会导致空指针，直接捕获，不蹦就行
            if (subscribe != null) {
                subscribe.dispose();
                subscribe = null;
            }
            if (mPwdDialogFragment != null) {

                mPwdDialogFragment.dismiss();
                mPwdDialogFragment = null;
            }
            if (mCurrentFragment != null) {
                mCurrentFragment = null;
            }
            fixInputMethodManagerLeak(this);
            super.onDestroy();
        } catch (Exception e) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_page_btn:
                if (mRoomInfo != null) {
                    UserInfoActivity.start(this, mRoomInfo.getUid());
                    finish();
                }
                break;
            case R.id.back_btn:
                finish();
                break;
            default:
        }
    }

    @Override
    public void requestRoomInfoSuccessView(RoomInfo roomInfo) {
        mRoomInfo = roomInfo;
        if (TextUtils.isEmpty(roomInfo.getRoomPwd())
                || roomInfo.getUid() == CoreManager.getCore(IAuthCore.class).getCurrentUid()) {
            getMvpPresenter().enterRoom(roomInfo);
        } else {
            if (isFinishing()) {
                return;
            }
            showRoomPwdDialog(roomInfo);
        }
    }

    @Override
    public void requestRoomInfoFailView(String errorStr) {
        getDialogManager().dismissDialog();
        toast(errorStr);
        finish();
    }

    @Override
    public void enterRoomSuccess() {
        if (mPwdDialogFragment != null) {
            mPwdDialogFragment.dismiss();
        }
        getMoreUnpkFingerGuessingGameMsg();
        addRoomFragment(false);
        //获取管理员
        //: 2018/11/14 im 废弃
//        getMvpPresenter().getNormalChatMember();
        getMvpPresenter().getRoomManagerMember();
    }

    @Override
    public void enterRoomFail(int code, String error) {
        //: 2018/4/13 0013
        dimissDialog();
        {
            AvRoomDataManager.get().release();
            toast(error);
            finish();
        }

    }

    @Override
    public void showFinishRoomView() {
        dimissDialog();
        showLiveFinishView();
    }

    @Override
    public void showBlackEnterRoomView() {

        dimissDialog();
        AvRoomDataManager.get().release();
        toast(getString(R.string.add_black_list));
        finish();
    }

    private void dimissDialog() {
        getDialogManager().dismissDialog();
        if (mPwdDialogFragment != null) {
            mPwdDialogFragment.dismiss();
        }
    }

    // 新版IM  通知类型消息，需要单独处理
    @CoreEvent(coreClientClass = IAVRoomCoreClient.class)
    public void onUserCome(ChatRoomMessage roomMessage) {
        RoomMemberComeInfo memberComeInfo = new RoomMemberComeInfo();
        if (roomMessage != null && roomMessage.getImChatRoomMember() != null) {
            memberComeInfo.setNickName(roomMessage.getImChatRoomMember().getNick());
            memberComeInfo.setExperLevel(roomMessage.getImChatRoomMember().getExperLevel());
            memberComeInfo.setCarName(roomMessage.getImChatRoomMember().getCar_name());
            memberComeInfo.setCarImgUrl(roomMessage.getImChatRoomMember().getCar_url());
        }
        //加到处理队列（因为有可能fragment还没初始化，还不能show，所以加到队列，由fragment处理后再remove）
        AvRoomDataManager.get().addMemberComeInfo(memberComeInfo);
        CoreManager.notifyClients(IAVRoomCoreClient.class, IAVRoomCoreClient.dealUserComeMsg);
    }

    @CoreEvent(coreClientClass = IAuthClient.class)
    public void onLogout() {
        AvRoomDataManager.get().release();
        finish();
    }

    @CoreEvent(coreClientClass = IIMRoomCoreClient.class)
    public void enterError() {
        enterRoomFail(-1, "网络异常");
    }


}