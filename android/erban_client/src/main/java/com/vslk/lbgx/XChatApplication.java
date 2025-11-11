package com.vslk.lbgx;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.AppUtils;
import com.bumptech.glide.request.target.ViewTarget;
import com.didichuxing.doraemonkit.DoraemonKit;
import com.jph.takephoto.uitl.UserBehaviorLimitUtils;
import com.llew.huawei.verifier.LoadedApkHuaWei;
import com.mob.MobApplication;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.msg.MessageNotifierCustomization;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.pingplusplus.android.Pingpp;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.tencent.bugly.crashreport.CrashReport;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.listener.CallBack;
import com.tongdaxing.erban.libcommon.net.ErBanAllHostnameVerifier;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.erban.libcommon.net.rxnet.RequestManager;
import com.tongdaxing.erban.libcommon.net.rxnet.RxNet;
import com.tongdaxing.erban.libcommon.net.rxnet.model.HttpParams;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_core.CoreRegisterCenter;
import com.tongdaxing.xchat_core.DemoCache;
import com.tongdaxing.xchat_core.LoginSyncDataStatusObserver;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.activity.IActivityCore;
import com.tongdaxing.xchat_core.gift.IGiftCore;
import com.tongdaxing.xchat_core.im.avroom.IAVRoomCore;
import com.tongdaxing.xchat_core.im.custom.bean.CustomAttachment;
import com.tongdaxing.xchat_core.im.friend.IIMFriendCore;
import com.tongdaxing.xchat_core.im.login.IIMLoginCore;
import com.tongdaxing.xchat_core.im.message.IIMMessageCore;
import com.tongdaxing.xchat_core.im.notification.INotificationCore;
import com.tongdaxing.xchat_core.im.room.IIMRoomCore;
import com.tongdaxing.xchat_core.im.sysmsg.ISysMsgCore;
import com.tongdaxing.xchat_core.pay.IPayCore;
import com.tongdaxing.xchat_core.realm.IRealmCore;
import com.tongdaxing.xchat_core.redpacket.IRedPacketCore;
import com.tongdaxing.xchat_core.room.auction.IAuctionCore;
import com.tongdaxing.xchat_core.room.face.IFaceCore;
import com.tongdaxing.xchat_core.room.model.AvRoomModel;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.BuildConfig;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.image.ImageManager;
import com.tongdaxing.xchat_framework.util.config.BasicConfig;
import com.tongdaxing.xchat_framework.util.util.ChannelUtil;
import com.tongdaxing.xchat_framework.util.util.TempCallback;
import com.tongdaxing.xchat_framework.util.util.VersionUtil;
import com.vslk.lbgx.reciever.ConnectiveChangedReceiver;
import com.vslk.lbgx.ui.MainActivity;
import com.vslk.lbgx.ui.launch.activity.NimMiddleActivity;
import com.vslk.lbgx.utils.ContactHelper;
import com.vslk.lbgx.utils.CrashHandler;
import com.vslk.lbgx.utils.SessionHelper;
import com.zhy.autolayout.utils.ScreenUtils;

import java.io.File;
import java.io.IOException;

import cn.jpush.android.api.JPushInterface;
import io.reactivex.plugins.RxJavaPlugins;
import me.jessyan.autosize.AutoSizeConfig;

import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_OPEN_ROOM_NOTI;
import static com.tongdaxing.xchat_framework.util.util.VersionUtil.HUAWEI;
import static com.tongdaxing.xchat_framework.util.util.VersionUtil.VERSION_5_1;
import static com.tongdaxing.xchat_framework.util.util.VersionUtil.VERSION_5_1_1;

/**
 * @author chenran
 * @date 2017/2/11
 */

public class XChatApplication extends MobApplication implements Application.ActivityLifecycleCallbacks {
    public static final String TAG = "XChatApplication";

    private static XChatApplication instance;

    //static 代码段可以防止内存泄露
    static {
        SmartRefreshLayout.setDefaultRefreshHeaderCreater((context, layout) -> {
            layout.setEnableHeaderTranslationContent(false);
            MaterialHeader materialHeader = new MaterialHeader(context);
            materialHeader.setShowBezierWave(false);
            return materialHeader;
        });
        SmartRefreshLayout.setDefaultRefreshFooterCreater(
                (context, layout) -> new ClassicsFooter(context).setDrawableSize(20));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        NIMClient.init(this, null, options());
        if (inMainProcess(this)) {
            //fixed: Glide Exception:"You must not call setTag() on a view Glide is targeting"
            ViewTarget.setTagId(R.id.tag_glide);
            init(BuildConfig.DEL_EVN);
            setModuleTempCallback();
        }
        registerActivityLifecycleCallbacks(this);
    }

    public static XChatApplication getInstance() {
        if (null == instance) {
            instance = new XChatApplication();
        }
        return instance;
    }

    private void setModuleTempCallback() {
        TempCallback.setiTempCallback(() -> {
            UserInfo userInfo = CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo();
            if (userInfo != null) {
                if (userInfo.getExperLevel() >= UserBehaviorLimitUtils.getLimitSendLevel()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        });// 临时做法，后期要重新整理
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);
        // 安装tinker
//        Beta.installTinker();
    }

    /**
     * 修复华为华为5.1与5.1.1两个版本提示注册广播过多的bug
     */
    private void initHuaweiVerifier() {
        if (VersionUtil.getManufacturer().equals(HUAWEI)) {
            String systemVersion = VersionUtil.getSystemVersion();
            if (VERSION_5_1.equals(systemVersion) || VERSION_5_1_1.equals(systemVersion)) {
                LoadedApkHuaWei.hookHuaWeiVerifier(this);
            }
        }
    }

    public SDKOptions options() {
        SDKOptions options = new SDKOptions();
        options.asyncInitSDK = true;
        if (BasicConfig.INSTANCE.isDebuggable()) {
            options.checkManifestConfig = true;
        }
        // 如果将新消息通知提醒托管给 SDK 完成，需要添加以下配置。否则无需设置。
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        // 点击通知栏跳转到该Activity
        config.notificationEntrance = NimMiddleActivity.class;
//        config.notificationSmallIconId = R.drawable.icon_msg_normal;
        // 呼吸灯配置
        config.ledARGB = Color.GREEN;
        config.ledOnMs = 1000;
        config.ledOffMs = 1500;
        // 通知铃声的uri字符串
        config.notificationSound = "android.resource://com.netease.nim.demo/raw/msg";
        options.statusBarNotificationConfig = config;
        // 定制通知栏提醒文案（可选，如果不定制将采用SDK默认文案）
        options.messageNotifierCustomization = messageNotifierCustomization;

        options.appKey = Constants.nimAppKey;

        // 配置保存图片，文件，log 等数据的目录
        // 如果 options 中没有设置这个值，SDK 会使用下面代码示例中的位置作为 SDK 的数据目录。
        // 该目录目前包含 log, file, image, audio, video, thumb 这6个目录。
        // 如果第三方 APP 需要缓存清理功能， 清理这个目录下面个子目录的内容即可。
        String sdkPath = null;
        try {
            sdkPath = Environment.getExternalStorageDirectory() + "/" + this.getPackageName() + "/nim";
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        options.sdkStorageRootPath = sdkPath;

        // 配置是否需要预下载附件缩略图，默认为 true
        options.preloadAttach = true;

        // 配置附件缩略图的尺寸大小。表示向服务器请求缩略图文件的大小
        // 该值一般应根据屏幕尺寸来确定， 默认值为 Screen.width / 2
        int[] size = ScreenUtils.getScreenSize(this, true);
        options.thumbnailSize = size[0] / 2;
//        // save cache，留做切换账号备用
        DemoCache.setNotificationConfig(config);
        return options;
    }

    private MessageNotifierCustomization messageNotifierCustomization = new MessageNotifierCustomization() {
        @Override
        public String makeNotifyContent(String nick, IMMessage message) {
            if (message.getMsgType() == MsgTypeEnum.custom) {
                CustomAttachment attachment = (CustomAttachment) message.getAttachment();
                if (attachment.getFirst() == CUSTOM_MSG_HEADER_TYPE_OPEN_ROOM_NOTI) {
                    return message.getFromNick();
                }
            }
            // 采用SDK默认文案
            return "收到一条消息";
        }

        @Override
        public String makeTicker(String nick, IMMessage message) {
            if (message.getMsgType() == MsgTypeEnum.custom) {
                CustomAttachment attachment = (CustomAttachment) message.getAttachment();
                if (attachment.getFirst() == CUSTOM_MSG_HEADER_TYPE_OPEN_ROOM_NOTI) {
                    return message.getFromNick();
                }
            }
            // 采用SDK默认文案
            return "收到一条消息";
        }

        @Override
        public String makeRevokeMsgTip(String revokeAccount, IMMessage item) {
            return null;
        }
    };

    public static boolean inMainProcess(Context context) {
        String packageName = context.getPackageName();
        String processName = getProcessName(context);
        return packageName.equals(processName);
    }

    /**
     * 获取当前进程名
     *
     * @return 进程名
     */
    public static String getProcessName(Context context) {
        String processName = null;

        // ActivityManager
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));

        while (true) {
            for (ActivityManager.RunningAppProcessInfo info : am.getRunningAppProcesses()) {
                if (info.pid == android.os.Process.myPid()) {
                    processName = info.processName;
                    break;
                }
            }

            // go home
            if (!TextUtils.isEmpty(processName)) {
                return processName;
            }

            // take a rest and again
            try {
                Thread.sleep(100L);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void initRxNet(Context context, String url) {
        RxNet.init(context)
                .debug(BasicConfig.INSTANCE.isDebuggable())
                .setBaseUrl(url)
                .setHttpParams(new HttpParams())
                .certificates()
                .hostnameVerifier(new ErBanAllHostnameVerifier())
                .build();
    }

    private void setHttpCache() {
        try {
            File cacheDir = new File(getApplicationContext().getCacheDir(), "http");
            HttpResponseCache.install(cacheDir, 1024 * 1024 * 128);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init(int devEnv) {
        //头条适配
        AutoSizeConfig.getInstance().setCustomFragment(true);
        BasicConfig.INSTANCE.setAppContext(getApplicationContext());
        //切换生产坏境和测试环境 true/测试环境 false/生产环境
        BasicConfig.INSTANCE.setDebuggable(devEnv == 1);
        String urlChannel = ChannelUtil.getChannel(this);
        LogUtil.i(TAG, urlChannel);
        BasicConfig.INSTANCE.setChannel(urlChannel);
        BasicConfig.INSTANCE.setRootDir(Constants.ERBAN_DIR_NAME);
        BasicConfig.INSTANCE.setLogDir(Constants.LOG_DIR);
        BasicConfig.INSTANCE.setConfigDir(Constants.CONFIG_DIR);
        BasicConfig.INSTANCE.setVoiceDir(Constants.VOICE_DIR);
        BasicConfig.INSTANCE.setCacheDir(Constants.CACHE_DIR);
        Pingpp.DEBUG = BasicConfig.INSTANCE.isDebuggable();

        UriProvider.init(devEnv);
        OkHttpManager.getInstance().init(this, BasicConfig.INSTANCE.isDebuggable(), () -> {
            //TODO 替换域名
        });
        initNimUIKit();
        setCallback();
        //设置HTTP缓存
        setHttpCache();
        //设置bugly
        setBugly();
        //开启严格模式
        setStrictModel();
        //哆啦A梦工具箱
        setDoraemonKit();
        //内存泄漏监控
        setupLeakCanary();
        //开启奔溃处理
        setCrashHandler();
        initRxNet(BasicConfig.INSTANCE.getAppContext(), UriProvider.JAVA_WEB_URL);
        RequestManager.instance().init(getApplicationContext(), Constants.HTTP_CACHE_DIR);
        ImageManager.instance().init(getApplicationContext(), Constants.IMAGE_CACHE_DIR);
        ConnectiveChangedReceiver.getInstance().init(getApplicationContext());
        JPushInterface.setDebugMode(BasicConfig.INSTANCE.isDebuggable());
        JPushInterface.init(this);
        //华为手机5.1 和 5.1.1系统注册广播过多的异常
        initHuaweiVerifier();
        CoreManager.init(BasicConfig.INSTANCE.getLogDir().getAbsolutePath());
        CoreRegisterCenter.registerCore();

        CoreManager.getCore(IRealmCore.class);
        CoreManager.getCore(IUserCore.class);
        CoreManager.getCore(IAuctionCore.class);
        CoreManager.getCore(IRedPacketCore.class);
        CoreManager.getCore(IActivityCore.class);
        CoreManager.getCore(IIMLoginCore.class);
        CoreManager.getCore(IIMFriendCore.class);
        CoreManager.getCore(IGiftCore.class);
        CoreManager.getCore(IFaceCore.class);
        CoreManager.getCore(IPayCore.class);
        CoreManager.getCore(IIMMessageCore.class);
        CoreManager.getCore(IIMRoomCore.class);
        CoreManager.getCore(IAVRoomCore.class);

        // 监听登录同步数据完成通知
        LoginSyncDataStatusObserver.getInstance().registerLoginSyncDataStatus(true);
        //系统通知的到达和已读
        CoreManager.getCore(ISysMsgCore.class).registSystemMessageObserver(true);
        //注册im用户状态的系统通知
        CoreManager.getCore(IIMLoginCore.class).registAuthServiceObserver(true);
        //全局自定义通知
        CoreManager.getCore(INotificationCore.class).observeCustomNotification(true);
    }

    private void initNimUIKit() {
        NimUIKit.init(this);
        // 可选定制项
        // 注册定位信息提供者类（可选）,如果需要发送地理位置消息，必须提供。
        // demo中使用高德地图实现了该提供者，开发者可以根据自身需求，选用高德，百度，google等任意第三方地图和定位SDK。
//        NimUIKit.setLocationProvider(new NimDemoLocationProvider());

        // 会话窗口的定制: 示例代码可详见demo源码中的SessionHelper类。
        // 1.注册自定义消息附件解析器（可选）
        // 2.注册各种扩展消息类型的显示ViewHolder（可选）
        // 3.设置会话中点击事件响应处理（一般需要）
        SessionHelper.init();

        // 通讯录列表定制：示例代码可详见demo源码中的ContactHelper类。
        // 1.定制通讯录列表中点击事响应处理（一般需要，UIKit 提供默认实现为点击进入聊天界面)
        ContactHelper.init();
    }

    private void setCallback() {
        RxJavaPlugins.setErrorHandler(throwable -> {
            throwable.printStackTrace();
            // print it
            Log.e(TAG, "the subscribe() method default error handler", throwable);
        });
        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BasicConfig.INSTANCE.isDebuggable();
            }
        });
    }

    private void setBugly() {
        //bugly初始化
        if (AppUtils.isAppDebug()) {
            CrashReport.initCrashReport(this, "096b2e0c33", true);
        } else {
            CrashReport.initCrashReport(this, "bea0e23102", false);
        }
    }

    private void setStrictModel() {
        if (!BasicConfig.INSTANCE.isDebuggable()) {
            return;
        }

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());
    }

    private void setDoraemonKit() {
        if (!BasicConfig.INSTANCE.isDebuggable()) {
            return;
        }
        DoraemonKit.install(this);
    }

    private void setupLeakCanary() {
        if (!BasicConfig.INSTANCE.isDebuggable()) {
            return;
        }

//        enabledStrictMode();
    }

    private void setCrashHandler() {
        if (!BasicConfig.INSTANCE.isDebuggable()) {
            return;
        }
        CrashHandler.getInstance().init(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (activity instanceof MainActivity) {
            LogUtil.d("onActivityDestroyed", "onActivityDestroyed");
            new AvRoomModel().exitRoom(new CallBack<String>() {
                @Override
                public void onSuccess(String data) {

                }

                @Override
                public void onFail(int code, String error) {

                }
            });
        }
    }
}
