package com.vslk.lbgx.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.microquation.linkedme.android.LinkedME;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.cache.NimUserInfoCache;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nim.uikit.common.util.sys.TimeUtil;
import com.netease.nim.uikit.contact.ContactEventListener;
import com.netease.nim.uikit.session.NimUser;
import com.netease.nim.uikit.session.SessionCustomization;
import com.netease.nim.uikit.session.SessionEventListener;
import com.netease.nim.uikit.session.actions.BaseAction;
import com.netease.nim.uikit.session.actions.ImageAction;
import com.netease.nim.uikit.session.fragment.MsgBean;
import com.netease.nim.uikit.session.module.IShareFansCoreClient;
import com.netease.nim.uikit.session.viewholder.MsgViewHolderInvitationFans;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.NimIntent;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_core.activity.bean.TeenagerModelInfo;
import com.tongdaxing.xchat_core.auth.AccountInfo;
import com.tongdaxing.xchat_core.auth.IAuthClient;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.home.presenter.MainPresenter;
import com.tongdaxing.xchat_core.home.view.IMainView;
import com.tongdaxing.xchat_core.im.custom.bean.NoticeAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.OpenRoomNotiAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.RedPacketAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.ShareFansAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.nim.LotteryAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.nim.NimGiftAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.nim.TransferAttachment;
import com.tongdaxing.xchat_core.im.login.IIMLoginClient;
import com.tongdaxing.xchat_core.im.message.IIMMessageCore;
import com.tongdaxing.xchat_core.im.message.IIMMessageCoreClient;
import com.tongdaxing.xchat_core.linked.ILinkedCoreClient;
import com.tongdaxing.xchat_core.linked.LinkedInfo;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.manager.IMNetEaseManager;
import com.tongdaxing.xchat_core.manager.RoomEvent;
import com.tongdaxing.xchat_core.pay.IPayCore;
import com.tongdaxing.xchat_core.pay.IPayCoreClient;
import com.tongdaxing.xchat_core.redpacket.IRedPacketCoreClient;
import com.tongdaxing.xchat_core.redpacket.bean.RedPacketInfoV2;
import com.tongdaxing.xchat_core.room.IRoomCore;
import com.tongdaxing.xchat_core.room.IRoomCoreClient;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.room.publicchatroom.PublicChatRoomController;
import com.tongdaxing.xchat_core.user.IUserClient;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.VersionsCore;
import com.tongdaxing.xchat_core.user.VersionsCoreClient;
import com.tongdaxing.xchat_core.user.bean.CheckUpdataBean;
import com.tongdaxing.xchat_core.user.bean.NewRecommendBean;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.coremanager.IAppInfoCore;
import com.tongdaxing.xchat_framework.data.BaseConstants;
import com.tongdaxing.xchat_framework.im.IMError;
import com.tongdaxing.xchat_framework.util.config.BasicConfig;
import com.tongdaxing.xchat_framework.util.config.SpEvent;
import com.tongdaxing.xchat_framework.util.util.JavaUtil;
import com.tongdaxing.xchat_framework.util.util.SpUtils;
import com.tongdaxing.xchat_framework.util.util.log.MLog;
import com.tongdaxing.xchat_framework.util.util.pref.CommonPref;
import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.vslk.lbgx.constant.Extras;
import com.vslk.lbgx.event.ToHim;
import com.vslk.lbgx.im.actions.GiftAction;
import com.vslk.lbgx.im.holder.MsgViewHolderContent;
import com.vslk.lbgx.im.holder.MsgViewHolderGift;
import com.vslk.lbgx.im.holder.MsgViewHolderLottery;
import com.vslk.lbgx.im.holder.MsgViewHolderOnline;
import com.vslk.lbgx.im.holder.MsgViewHolderRedPacket;
import com.vslk.lbgx.im.transfer.INimCore;
import com.vslk.lbgx.im.transfer.MsgTransferHolder;
import com.vslk.lbgx.im.transfer.NimCoreImpl;
import com.vslk.lbgx.im.transfer.TransferAction;
import com.vslk.lbgx.room.AVRoomActivity;
import com.vslk.lbgx.service.DaemonService;
import com.vslk.lbgx.ui.common.widget.CircleImageView;
import com.vslk.lbgx.ui.dialog.ChoiceHomeDialog;
import com.vslk.lbgx.ui.find.fragment.SpeedDatingFragment;
import com.vslk.lbgx.ui.home.fragment.HomeFragment;
import com.vslk.lbgx.ui.home.view.NoScrollViewPager;
import com.vslk.lbgx.ui.launch.activity.MiddleActivity;
import com.vslk.lbgx.ui.login.activity.NewLoginActivity;
import com.vslk.lbgx.ui.me.MeFragment;
import com.vslk.lbgx.ui.me.user.activity.UserInfoActivity;
import com.vslk.lbgx.ui.message.fragment.MsgFragment;
import com.vslk.lbgx.ui.message.msgsend.IMsgSendCore;
import com.vslk.lbgx.ui.message.msgsend.MsgSendCoreImpl;
import com.vslk.lbgx.ui.sign.dialog.TeenagerModelDialog;
import com.vslk.lbgx.ui.verified.VerifiedDialog;
import com.vslk.lbgx.ui.web.CommonWebViewActivity;
import com.vslk.lbgx.ui.widget.MainTabLayout;
import com.vslk.lbgx.ui.widget.dialog.NewUserDialog;
import com.vslk.lbgx.ui.widget.dialog.RedPacketDialog;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.vslk.lbgx.utils.SessionHelper;
import com.vslk.lbgx.utils.UIHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
@CreatePresenter(MainPresenter.class)
public class MainActivity extends BaseMvpActivity<IMainView, MainPresenter> implements MainTabLayout.OnTabClickListener, IMainView, View.OnTouchListener {
    private static final String EXTRA_APP_QUIT = "APP_QUIT";
    private static final String TAG = "MainActivity";
    private FrameLayout avatarLayout;
    private CircleImageView avatarImage;
    private MainTabLayout mMainTabLayout;
    private int mCurrentMainPosition;
    private ImageView ivMyRoom;
    private ViewGroup mViewGroup;
    private int mWidthPixels;
    private int mHeightPixels;
    private long mDownTimeMillis;
    private int mMainTabHeight;
    private TextView tvNick;

    private boolean isRequest;
    private ChoiceHomeDialog choicedialog;
    private NoScrollViewPager nosvp;
    private ArrayList<Fragment> mFragment;

    public static final String TAG_EXIT = "exit";

    public static void start(Context context) {
        start(context, null);
    }

    public static void start(Context context, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);


    }

    public static void startPage(Context context, int page) {
        Intent home = new Intent(context, MainActivity.class);
        home.putExtra(Extras.EXTRA_CHANGE_INDEX, page);
        context.startActivity(home);
    }

    @CoreEvent(coreClientClass = IRoomCoreClient.class)
    public void onGetRoomInfoFail(int errorno, String error, int pageType) {
        getDialogManager().dismissDialog();
        if (errorno == IMError.USER_REAL_NAME_NEED_PHONE
                || errorno == IMError.USER_REAL_NAME_AUDITING
                || errorno == IMError.USER_REAL_NAME_NEED_VERIFIED) {
            VerifiedDialog verifiedDialog = VerifiedDialog.newInstance(error, errorno);
            verifiedDialog.show(getSupportFragmentManager(), "verifiedDialog");
        } else {
            toast(error);
        }
    }

    @CoreEvent(coreClientClass = IRoomCoreClient.class)
    public void onOpenRoomFail(int errorno, String error) {
        if (errorno == IMError.USER_REAL_NAME_NEED_PHONE
                || errorno == IMError.USER_REAL_NAME_AUDITING
                || errorno == IMError.USER_REAL_NAME_NEED_VERIFIED) {
            VerifiedDialog verifiedDialog = VerifiedDialog.newInstance(error, errorno);
            verifiedDialog.show(getSupportFragmentManager(), "verifiedDialog");
        } else {
            toast(error);
        }
    }

    @Override
    protected boolean needSteepStateBar() {
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.KEY_MAIN_POSITION, mCurrentMainPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mCurrentMainPosition = savedInstanceState.getInt(Constants.KEY_MAIN_POSITION);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mCurrentMainPosition = savedInstanceState.getInt(Constants.KEY_MAIN_POSITION);
        }
        setContentView(R.layout.activity_main);
        iNimCore = NimCoreImpl.getInstance().get();
        setSwipeBackEnable(false);
        //自动登录
        CoreManager.getCore(IAuthCore.class).autoLogin();
        initView();
        permission();
        onParseIntent();
        updateDatas();
        updateRoomState();
        initP2PSessionCustomization();
        IMNetEaseManager.get().subscribeChatRoomEventObservable(roomEvent -> {
            if (roomEvent == null || roomEvent.getEvent() == RoomEvent.NONE) {
                return;
            }
            int event = roomEvent.getEvent();
            if (event == RoomEvent.ENTER_ROOM) {
                onEnter(AvRoomDataManager.get().mCurrentRoomInfo);
            } else if (event == RoomEvent.KICK_OUT_ROOM) {
                exitRoom();
            } else if (event == RoomEvent.ROOM_EXIT) {
                exitRoom();
            } else if (event == RoomEvent.ROOM_CHAT_RECONNECTION) {
                updateRoomState();
            }
        }, this);
        checkUpdata();
        //设置debug模式下打印LinkedME日志
        setLinkedMeDebug();
        EventBus.getDefault().register(this);
    }

    /**
     * 去找Ta 请求发起
     *
     * @param toHim
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onToHim(ToHim toHim) {
        if (toHim.getUid() > 0) {
            getDialogManager().showProgressDialog(this, "请稍后...");
            CoreManager.getCore(IRoomCore.class).getUserRoom(toHim.getUid());
        }
    }

    /**
     * 去找Ta 统一处理
     *
     * @param roomInfo
     */
    @CoreEvent(coreClientClass = IRoomCoreClient.class)
    public void onGetUserRoom(RoomInfo roomInfo) {
        long formalRoomId =
                BasicConfig.INSTANCE.isDebuggable() ? PublicChatRoomController.devRoomId : PublicChatRoomController.formalRoomId;
        if (roomInfo != null && roomInfo.getRoomId() == formalRoomId) {
            toast("对方不在房间内");
            return;
        }
        getDialogManager().dismissDialog();
        RoomInfo current = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo != null && roomInfo.getUid() > 0) {
            if (current != null) {
                if (current.getUid() == roomInfo.getUid()) {
                    AVRoomActivity.start(MainActivity.this, current.getUid());
                    return;
                }
            }
            AVRoomActivity.start(MainActivity.this, roomInfo.getUid());
        } else {
            NimUIKit.startP2PSession(MainActivity.this, roomInfo.getP2pUid() + "");
//            toast("对方不在房间，已为你跳转私聊页面");
        }
    }

    /**
     * 显示用户信息弹窗或界面
     *
     * @param nimUser
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowUserInfo(NimUser nimUser) {
        UserInfoActivity.start(this, Long.parseLong(nimUser.getSessionId()));
    }


    /**
     * 私聊消息拦截
     *
     * @param msgBean
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendMsgVerify(MsgBean msgBean) {
        IMsgSendCore iMsgSendCore = MsgSendCoreImpl.getInstance().getIMsgSendCore();
        iMsgSendCore.onSendMsg(msgBean.getMessage(), (msg) -> {
            NIMClient.getService(MsgService.class).sendMessage(msg, false);
            msgBean.getMessageListPanel().onMsgSend(msg);
            msgBean.getAitManager().reset();
        });
    }


    private void setLinkedMeDebug() {
        if (BasicConfig.INSTANCE.isDebuggable()) {
            LinkedME.getInstance(this).setDebug();
        } else {
            LinkedME.getInstance(this);
        }
        LinkedME.getInstance().setImmediate(false);
        LinkedME.getInstance().setHandleActivity(MiddleActivity.class.getName());
    }


    private void checkUpdata() {
        VersionsCore core = CoreManager.getCore(VersionsCore.class);
        core.getConfig();
        core.checkVersion();
        core.requestSensitiveWord();
        CoreManager.getCore(IAppInfoCore.class).checkBanned();
    }

    @CoreEvent(coreClientClass = VersionsCoreClient.class)
    public void onVersionUpdataDialog(CheckUpdataBean checkUpdataBean) {
        if (checkUpdataBean == null) {
            return;
        }
        String updateVersion = checkUpdataBean.getUpdateVersion();
        if (TextUtils.isEmpty(updateVersion)) {
            return;
        }
        boolean force = false;
        switch (checkUpdataBean.getStatus()) {
            case 1:
                return;
            case 3:
                force = true;
                break;
            case 4:
                force = false;
                break;
            default:

        }

        boolean finalForce = force;

        long l = (Long) SpUtils.get(MainActivity.this, "updataDialogDissTime", 0L);
        if (System.currentTimeMillis() - l < (86400 * 1000) && !finalForce) {
            return;
        }
        AlertDialog alertDialog = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)

                .setTitle("检测到有最新的版本是否更新")
                .setMessage(checkUpdataBean.getUpdateVersionDesc())
                .setPositiveButton("更新", (dialog, which) -> {
                    String download_url = checkUpdataBean.getDownloadUrl();
                    if (TextUtils.isEmpty(download_url)) {
                        download_url = BaseConstants.NEWEST_APK_FILE_URL;//默认最新包地址
                    }
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(download_url));
                    startActivity(intent);
                    finish();
                }).setNegativeButton("取消", (dialog, which) -> {
                    if (finalForce) {
                        finish();
                    } else {
                        SpUtils.put(MainActivity.this, "updataDialogDissTime", System.currentTimeMillis());
                    }
                }).create();

        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(!force);
        alertDialog.show();
    }


    private void closeOpenRoomAnimation() {
        avatarImage.clearAnimation();
        avatarLayout.setVisibility(View.GONE);
    }

    private void initP2PSessionCustomization() {
        SessionCustomization sessionCustomization = new SessionCustomization();
        ArrayList<BaseAction> actions = new ArrayList<>();
        actions.add(new ImageAction());
        actions.add(new GiftAction());
        sessionCustomization.actions = actions;
        sessionCustomization.withSticker = true;
        NimUIKit.setCommonP2PSessionCustomization(sessionCustomization);
        // 新版IM 私聊这里需要单独处理
//
        NimUIKit.registerMsgItemViewHolder(OpenRoomNotiAttachment.class, MsgViewHolderOnline.class);
        NimUIKit.registerMsgItemViewHolder(NimGiftAttachment.class, MsgViewHolderGift.class);
        NimUIKit.registerMsgItemViewHolder(NoticeAttachment.class, MsgViewHolderContent.class);
        NimUIKit.registerMsgItemViewHolder(RedPacketAttachment.class, MsgViewHolderRedPacket.class);
        NimUIKit.registerMsgItemViewHolder(LotteryAttachment.class, MsgViewHolderLottery.class);
        NimUIKit.registerMsgItemViewHolder(ShareFansAttachment.class, MsgViewHolderInvitationFans.class);
        NimUIKit.registerMsgItemViewHolder(TransferAttachment.class, MsgTransferHolder.class);

        NimUIKit.setSessionListener(listener);
        NimUIKit.setContactEventListener(listener1);
    }

    private ContactEventListener listener1 = new ContactEventListener() {
        @Override
        public void onItemClick(Context context, String account) {
            NimUIKit.startP2PSession(context, account);
        }

        @Override
        public void onItemLongClick(Context context, String account) {

        }

        @Override
        public void onAvatarClick(Context context, String account) {
            UserInfoActivity.start(MainActivity.this, JavaUtil.str2long(account));
        }
    };

    private SessionEventListener listener = new SessionEventListener() {
        @Override
        public void onAvatarClicked(Context context, IMMessage message) {
            if (String.valueOf(Constants.OFFICIAL).equals(message.getFromAccount())) {
                return;
            }
            UserInfoActivity.start(MainActivity.this, JavaUtil.str2long(message.getFromAccount()));
        }

        @Override
        public void onAvatarLongClicked(Context context, IMMessage message) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        LinkedME.getInstance().setImmediate(true);
    }
//
//    private void isShowSignInDialog() {
//        String fileName = "sign_in_file_" + CoreManager.getCore(IAuthCore.class).getCurrentUid();
//        SharedPreferences sharedPreferences = getSharedPreferences(fileName, Activity.MODE_PRIVATE);
//        long time = sharedPreferences.getLong("time", 0);
//        if (isOpenDialog(time)) {//弹出对话框
//            SignInDialog signInDialog = new SignInDialog();
//            signInDialog.show(getSupportFragmentManager(), "");
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            long curTime = System.currentTimeMillis();
//            editor.putLong("time", curTime);
//            if (editor.commit()) {
//                Log.e(TAG, "commit data " + curTime);
//            }
//        }
//    }
//
//    private boolean isOpenDialog(long time) {
//        if (time == 0) {
//            return true;
//        }
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
//        String oldTime = simpleDateFormat.format(new Date(time));
//        String curTime = simpleDateFormat.format(new Date(System.currentTimeMillis()));
//        return !oldTime.equals(curTime);
//    }

    private void initView() {
        ivMyRoom = (ImageView) findViewById(R.id.me_item_create_my_room);
        mHeightPixels = getResources().getDisplayMetrics().heightPixels;
        mWidthPixels = getResources().getDisplayMetrics().widthPixels;
        mMainTabLayout = (MainTabLayout) findViewById(R.id.main_tab_layout);
        avatarLayout = (FrameLayout) findViewById(R.id.avatar_image_layout);
        avatarImage = (CircleImageView) findViewById(R.id.avatar_image);
        tvNick = (TextView) findViewById(R.id.tv_nick);
        nosvp = (NoScrollViewPager) findViewById(R.id.vp_main);
        nosvp.setNoScroll(true);
        mFragment = new ArrayList<>();
        Fragment mainFragment = new HomeFragment();
        Fragment speedDatingFragment = new SpeedDatingFragment();
        Fragment msgFragment = new MsgFragment();
        Fragment meFragment = new MeFragment();
        mFragment.add(mainFragment);
        mFragment.add(speedDatingFragment);
        mFragment.add(msgFragment);
        mFragment.add(meFragment);
        MainVpAdapter mainVpAdapter = new MainVpAdapter(getSupportFragmentManager());
        nosvp.setAdapter(mainVpAdapter);
        nosvp.setOffscreenPageLimit(mFragment.size());
        mViewGroup = (ViewGroup) findViewById(R.id.root);
        avatarLayout.setVisibility(View.GONE);
        mMainTabLayout.setOnTabClickListener(this);
        ivMyRoom.setOnClickListener(v -> {
            getDialogManager().showProgressDialog(MainActivity.this, "请稍后...");
            CoreManager.getCore(IRoomCore.class).requestRoomInfo(CoreManager.getCore(IAuthCore.class).getCurrentUid(), 0);
//            if (choicedialog == null) {
//                choicedialog = new ChoiceHomeDialog();
//                choicedialog.setListener(new ChoiceHomeDialog.ICHListener() {
//                    @Override
//                    public void onRoom() {
//                        getDialogManager().showProgressDialog(MainActivity.this, "请稍后...");
//                        CoreManager.getCore(IRoomCore.class).requestRoomInfo(CoreManager.getCore(IAuthCore.class).getCurrentUid(), 0);
//                    }
//
//                    @Override
//                    public void onXY() {
//
//                    }
//                });
//                choicedialog.show(getSupportFragmentManager(), "choiceHome");
//            }
        });
        ImageView ivCloseFloatingWindow = (ImageView) findViewById(R.id.iv_close_floating_window);
        ivCloseFloatingWindow.setOnClickListener(v -> {
            getMvpPresenter().exitRoom();
        });
        initAvatarLayout();
        mMainTabLayout
                .setHomeIcon("首页", R.mipmap.ic_main_tab_home_pressed, R.mipmap.ic_main_tab_home)
                .setAttentionIcon("交友", R.mipmap.ic_main_tab_attention_pressed, R.mipmap.ic_main_tab_attention)
                .setMsgIcon("消息", R.mipmap.ic_main_tab_msg_pressed, R.mipmap.ic_main_tab_msg)
                .setMeIcon("我的", R.mipmap.ic_main_tab_me_pressed, R.mipmap.ic_main_tab_me);

    }

    @CoreEvent(coreClientClass = IRoomCoreClient.class)
    public void onGetRoomInfo(RoomInfo roomInfo, int pageType) {
        getDialogManager().dismissDialog();
        if (roomInfo != null) {
            if (roomInfo.isValid()) {
                AVRoomActivity.start(this, roomInfo.getUid());
            } else {
                openRoom();
            }
        } else {
            openRoom();
        }
    }

    private void openRoom() {
        UserInfo userInfo = CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo();
        CoreManager.getCore(IRoomCore.class).openRoom(userInfo.getUid(), RoomInfo.ROOMTYPE_HOME_PARTY);
    }

    @CoreEvent(coreClientClass = IRoomCoreClient.class)
    public void onOpenRoom(RoomInfo roomInfo) {
        AVRoomActivity.start(this, roomInfo.getUid());
    }

    @CoreEvent(coreClientClass = IRoomCoreClient.class)
    public void onOpenRoomFail(String error) {
        toast(error);
    }

    /**
     * 房间入口按钮拖动逻辑
     */
    private void initAvatarLayout() {
        avatarLayout.setOnTouchListener(this);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) avatarLayout.getLayoutParams();
        int width1 = layoutParams.width;
        int height1 = layoutParams.height;
        int distance = ConvertUtils.dp2px(20);
        ViewGroup.LayoutParams mMainTabLayoutLayoutParams = mMainTabLayout.getLayoutParams();
        mMainTabHeight = mMainTabLayoutLayoutParams.height;
        int mh = mHeightPixels - height1 - mMainTabHeight - 3 * distance;
        int mw = mWidthPixels - width1 - distance;

        layoutParams.leftMargin = mw;
        layoutParams.topMargin = mh;
        avatarLayout.setLayoutParams(layoutParams);
        mViewGroup.invalidate();
    }


    private void updateDatas() {
        mMainTabLayout.select(mCurrentMainPosition);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent != null) {
            boolean isExit = intent.getBooleanExtra(TAG_EXIT, false);
            if (isExit) {
                this.finish();
            }
        }

        setIntent(intent);
        onParseIntent();
    }

    private long startTime;
    private boolean shown;

    private void onParseIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
            IMMessage message = (IMMessage) getIntent().getSerializableExtra(NimIntent.EXTRA_NOTIFY_CONTENT);
            switch (message.getSessionType()) {
                case P2P:
                    SessionHelper.startP2PSession(this, message.getSessionId());
                    break;
                case Team:
                    SessionHelper.startTeamSession(this, message.getSessionId());
                    break;
                default:
                    break;
            }
        } else if (intent.hasExtra(EXTRA_APP_QUIT)) {
            onLogout();
        } else if (intent.hasExtra(Extras.EXTRA_JUMP_P2P)) {
            Intent data = intent.getParcelableExtra(Extras.EXTRA_DATA);
            String account = data.getStringExtra(Extras.EXTRA_ACCOUNT);
            if (!TextUtils.isEmpty(account)) {
                SessionHelper.startP2PSession(this, account);
            }
        } else if (intent.hasExtra("url") && intent.hasExtra("type")) {
            startTime = System.currentTimeMillis();
        } else if (intent.hasExtra(Extras.EXTRA_CHANGE_INDEX)) {
            //切换主界面的显示页面
            int index = intent.getIntExtra(Extras.EXTRA_CHANGE_INDEX, 0);
            if (index != mCurrentMainPosition) {
                mMainTabLayout.select(index);
            }
        }
    }
    /**
     * 基本权限管理
     */
    private final String[] BASIC_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO,
    };
    private void permission() {
        boolean agreeBln = (boolean) SpUtils.get(this, NewLoginActivity.agreeStr,false);
        if(agreeBln)
        checkPermission(() -> {
                }, R.string.ask_again,BASIC_PERMISSIONS);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (choicedialog != null) {
            choicedialog.dismiss();
        }
        listener = null;
        listener1 = null;
        NimUIKit.setSessionListener(null);
        NimUIKit.setContactEventListener(null);
        MLog.debug(TAG, "MainActivity : destroyed");
        EventBus.getDefault().unregister(this);
    }

    /**
     * 双击返回键退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void checkoutLinkedMe() {
        Intent intent = new Intent(this, MiddleActivity.class);
        startActivity(intent);
    }

    private INimCore iNimCore;
    private boolean isOne = true;
    @CoreEvent(coreClientClass = IAuthClient.class)
    public void onLogin(AccountInfo accountInfo) {
        CoreManager.getCore(IPayCore.class).getWalletInfo(CoreManager.getCore(IAuthCore.class).getCurrentUid());
        CoreManager.getCore(IPayCore.class).loadDianDianCoinInfos();
        checkoutLinkedMe();
//        isShowSignInDialog();
        isShowTeenagerModelDialog();


        if (isOne) {
            if (iNimCore != null) iNimCore.givegoldcheck((data -> {
                SessionCustomization sessionCustomization = new SessionCustomization();
                ArrayList<BaseAction> actions = new ArrayList<>();
                actions.add(new ImageAction());
                actions.add(new GiftAction());
                if (data) {
                    actions.add(new TransferAction());
                }
                sessionCustomization.actions = actions;
                sessionCustomization.withSticker = true;
                NimUIKit.setCommonP2PSessionCustomization(sessionCustomization);
                isOne = false;
            }));
        }
    }

    private boolean isOpenTeenagerModelDialog() {
//        Calendar calendar = Calendar.getInstance();
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//        if ((hour >= 22 && hour <= 23) || hour >= 0 && hour <= 6) {//只在晚上10点至早上6点范围内弹出
            return true;
//        } else {
//            return false;
//        }
    }

    @CoreEvent(coreClientClass = IUserClient.class)
    public void isShowTeenagerModelDialog() {
        CoreManager.getCore(IPayCore.class).isShowTeenagerModel();
    }


    boolean isOpen;
    @CoreEvent(coreClientClass = IPayCoreClient.class)
    public void onGetTeenagerModelInfo(TeenagerModelInfo teenagerModelInfo) {
        if (teenagerModelInfo != null) {
            if(teenagerModelInfo.getUid() == 0 && teenagerModelInfo.getErbanNo() == 0 && StringUtil.isEmpty(teenagerModelInfo.getNick()) && StringUtil.isEmpty(teenagerModelInfo.getCipherCode())) {
                isOpen = false;
            } else {
                isOpen = true;
            }

            if(!isOpen){
                long lastTime = (long) SpUtils.get(this, SpEvent.teenage_last_time, 0L);
                boolean isSameDay = TimeUtil.isSameDay(TimeUtil.currentTimeMillis(),lastTime);
                if(!isSameDay){
                    //if (isOpenTeenagerModelDialog()) {
                    TeenagerModelDialog dialog = new TeenagerModelDialog();
                    dialog.show(getSupportFragmentManager(), "");
                    SpUtils.put(this, SpEvent.teenage_last_time, TimeUtil.currentTimeMillis());
//                }
                }
            }
        }
    }

    @CoreEvent(coreClientClass = IAuthClient.class)
    public void onRequestTicketFail(String error) {
        toast(error);
    }

    @CoreEvent(coreClientClass = IAuthClient.class)
    public void onLogout() {
        getMvpPresenter().exitRoom();
        NewLoginActivity.start(this);
    }

    @CoreEvent(coreClientClass = IAuthClient.class)
    public void onNeedLogin() {
        NewLoginActivity.start(MainActivity.this);
    }

    @CoreEvent(coreClientClass = IIMLoginClient.class)
    public void onImLoginSuccess(LoginInfo loginInfo) {
        int unreadCount = CoreManager.getCore(IIMMessageCore.class).queryUnreadMsg();
        mMainTabLayout.setMsgNum(unreadCount);
        // 如果需要跳转,在这里实现
        if (!shown && (System.currentTimeMillis() - startTime) <= 2000 && getIntent().hasExtra("url") && getIntent().hasExtra("type")) {
            shown = true;
            int type = getIntent().getIntExtra("type", 0);
            String url = getIntent().getStringExtra("url");
            if (type == 3) {
                Intent intent = new Intent(this, CommonWebViewActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
            } else if (type == 2) {
                AVRoomActivity.start(this, JavaUtil.str2long(url));
            }
        }
    }

    @CoreEvent(coreClientClass = IIMLoginClient.class)
    public void onImLoginFaith(String error) {
        toast(error);
        NewLoginActivity.start(MainActivity.this);
    }

    @CoreEvent(coreClientClass = IIMLoginClient.class)
    public void onKickedOut(StatusCode code) {
        toast("您已被踢下线，若非正常行为，请及时修改密码");
        CoreManager.getCore(IAuthCore.class).logout();
    }

    @CoreEvent(coreClientClass = IIMMessageCoreClient.class)
    public void onReceiveRecentContactChanged(List<RecentContact> imMessages) {
        int unreadCount = CoreManager.getCore(IIMMessageCore.class).queryUnreadMsg();
        mMainTabLayout.setMsgNum(unreadCount);
    }


    @CoreEvent(coreClientClass = IUserClient.class)
    public void onNeedCompleteInfo() {
        getDialogManager().dismissDialog();
        mCurrentMainPosition = 0;
        mMainTabLayout.select(mCurrentMainPosition);
        UIHelper.showAddInfoAct(this);
    }

    @CoreEvent(coreClientClass = IUserClient.class)
    public void onCurrentUserInfoUpdateFail(String msg) {
        onLogout();
    }

    @CoreEvent(coreClientClass = IUserClient.class)
    public void onCurrentUserInfoUpdate(UserInfo userInfo) {
        //判断是否绑定手机
        isRequest = true;
        getMvpPresenter().hasBindPhone();
        CoreManager.getCore(IAuthCore.class).setThirdUserInfo(null);
        int unreadCount = CoreManager.getCore(IIMMessageCore.class).queryUnreadMsg();
        mMainTabLayout.setMsgNum(unreadCount);
    }

    @CoreEvent(coreClientClass = IRedPacketCoreClient.class)
    public void onReceiveNewPacket(final RedPacketInfoV2 redPacketInfo) {
        CommonPref.instance(this).putBoolean("redPacketPoint", true);
        if (redPacketInfo.isNeedAlert()) {
            //新手推荐弹框检测
            if (redPacketInfo.getType() == 1) {
                checkRecommendShow(redPacketInfo);
            } else {
                RedPacketDialog.start(MainActivity.this, redPacketInfo);
            }
        }
    }

    /**
     * 判断是否需要提示新手推荐
     */
    private void checkRecommendShow(RedPacketInfoV2 redPacketInfo) {
        getMvpPresenter().newUserRecommend(redPacketInfo);
    }

    @Override
    public void onNewUserRecommendSuccessView(NewRecommendBean bean) {
        NewUserDialog user = NewUserDialog.newInstance(bean.getTitle(), bean.getAvatar(), bean.getUid());
        user.show(getSupportFragmentManager(), "new_user");
    }

    @Override
    public void onNewUserRecommendFailView(RedPacketInfoV2 redPacketInfo) {
        RedPacketDialog.start(MainActivity.this, redPacketInfo);
    }

    @Override
    public void hasBindPhone() {
        if (!isRequest) {
            return;
        }
        isRequest = false;
    }

    @Override
    public void hasBindPhoneFail(String error) {
        if (!isRequest) {
            return;
        }
    }

    @Override
    public void isShowTeenager() {

    }

    @CoreEvent(coreClientClass = IUserClient.class)
    public void onCurrentUserInfoCompleteFaith(String msg) {
        toast("用户信息加载失败，请检查网络后重新登录");
        getDialogManager().dismissDialog();
    }

    private void updateRoomState() {
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo != null) {
            UserInfo userInfo = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(roomInfo.getUid());
            if (userInfo != null) {
                avatarLayout.clearAnimation();
                avatarLayout.setVisibility(View.VISIBLE);
                Animation operatingAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate_anim);
                LinearInterpolator lin = new LinearInterpolator();
                operatingAnim.setInterpolator(lin);
                avatarImage.startAnimation(operatingAnim);
                tvNick.setText(userInfo.getNick());
                ImageLoadUtils.loadAvatar(MainActivity.this, userInfo.getAvatar(), avatarImage);
            } else {
                NimUserInfo nimUserInfo = NimUserInfoCache.getInstance().getUserInfo(roomInfo.getUid() + "");
                if (nimUserInfo == null) {
                    NimUserInfoCache.getInstance().getUserInfoFromRemote(roomInfo.getUid() + "", new RequestCallbackWrapper<NimUserInfo>() {

                        @Override
                        public void onResult(int i, NimUserInfo nimUserInfo, Throwable throwable) {
                            if (nimUserInfo != null) {
                                avatarLayout.clearAnimation();
                                avatarLayout.setVisibility(View.VISIBLE);
                                Animation operatingAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate_anim);
                                LinearInterpolator lin = new LinearInterpolator();
                                operatingAnim.setInterpolator(lin);
                                avatarImage.startAnimation(operatingAnim);
                                tvNick.setText(nimUserInfo.getName());
                                ImageLoadUtils.loadAvatar(MainActivity.this, nimUserInfo.getAvatar(), avatarImage);
                            }
                        }
                    });
                } else {
                    avatarLayout.clearAnimation();
                    avatarLayout.setVisibility(View.VISIBLE);
                    Animation operatingAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate_anim);
                    LinearInterpolator lin = new LinearInterpolator();
                    operatingAnim.setInterpolator(lin);
                    avatarImage.startAnimation(operatingAnim);
                    tvNick.setText(nimUserInfo.getName());
                    ImageLoadUtils.loadAvatar(MainActivity.this, nimUserInfo.getAvatar(), avatarImage);
                }
            }
        }
    }

    public void onEnter(RoomInfo roomInfo) {
        updateRoomState();
        DaemonService.start(this, roomInfo);
    }

    @CoreEvent(coreClientClass = ILinkedCoreClient.class)
    public void onLinkedInfoUpdateNotLogin() {
        onLogout();
    }

    @CoreEvent(coreClientClass = ILinkedCoreClient.class)
    public void onLinkedInfoUpdate(LinkedInfo linkedInfo) {
        if (!StringUtil.isEmpty(linkedInfo.getRoomUid())) {
            AVRoomActivity.start(this, JavaUtil.str2long(linkedInfo.getRoomUid()));
        }
    }

    @CoreEvent(coreClientClass = IShareFansCoreClient.class)
    public void onShareFansJoin(long uid) {
        AVRoomActivity.start(this, uid);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onTabClick(int position) {
        switchFragment(position);
    }

    private void switchFragment(int position) {
        nosvp.setCurrentItem(position, false);
        mCurrentMainPosition = position;
    }


    private class MainVpAdapter extends FragmentPagerAdapter {

        public MainVpAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragment.get(position);
        }

        @Override
        public int getCount() {
            return mFragment.size();
        }
    }

    @Override
    public void exitRoom() {
        closeOpenRoomAnimation();
        DaemonService.stop(MainActivity.this);
    }

    private int xDelta;
    private int yDelta;

    @Override
    public boolean onTouch(View view, @NotNull MotionEvent event) {
        final int x = (int) event.getRawX();
        final int y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:

                mDownTimeMillis = System.currentTimeMillis();
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view
                        .getLayoutParams();


                xDelta = x - params.leftMargin;
                yDelta = y - params.topMargin;
                break;
            case MotionEvent.ACTION_MOVE:

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                        .getLayoutParams();
                int width = layoutParams.width;
                int height = layoutParams.height;


                int xDistance = x - xDelta;
                int yDistance = y - yDelta;


                int outX = (mWidthPixels - width) - 10;
                if (xDistance > outX) {
                    xDistance = outX;
                }

                int outY = mHeightPixels - height - mMainTabHeight;
                if (yDistance > outY) {
                    yDistance = outY;
                }

                if (yDistance < 100) {
                    yDistance = 100;
                }
                if (xDistance < 10) {
                    xDistance = 10;
                }


                layoutParams.leftMargin = xDistance;
                layoutParams.topMargin = yDistance;
                view.setLayoutParams(layoutParams);
                break;
            case MotionEvent.ACTION_UP:
                if (System.currentTimeMillis() - mDownTimeMillis < 150) {
                    RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
                    if (roomInfo != null) {
                        AVRoomActivity.start(MainActivity.this, roomInfo.getUid());
                    }
                }
                break;
            default:
                break;
        }
        mViewGroup.invalidate();
        return true;
    }

}
