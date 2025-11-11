package com.tongdaxing.xchat_core.initial;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;
import com.netease.nim.uikit.glide.GlideApp;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.DemoCache;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.room.face.IFaceCore;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.tongdaxing.xchat_framework.util.config.BasicConfig;
import com.tongdaxing.xchat_framework.util.util.NetworkUtils;

import java.io.File;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * @author xiaoyu
 * @date 2017/12/29
 */

public class InitModel {
    private static final String TAG = "InitModel";
    private boolean requesting;
    private BroadcastReceiver receiver;
    /**
     * 缓存过期时间
     */
    private static final long TIME_EXPIRED = 24 * 60 * 60 * 1000;

    private static InitModel model;

    public static InitModel get() {
        if (model == null) {
            synchronized (InitModel.class) {
                if (model == null) {
                    model = new InitModel();
                }
            }
        }
        return model;
    }

    /**
     * 如果返回的是null，则可能是过期了，也有可能是因为图片还没有下载
     *
     * @return -
     */
    public InitInfo getCacheInitInfo() {
        // 过期了
        if (System.currentTimeMillis() - DemoCache.readInitInfoSavingTime() > TIME_EXPIRED)
            return null;
        // 没有缓存
        InitInfo initInfo = DemoCache.readInitInfo();

        if (initInfo == null) {
            return null;
        } else if (initInfo.getSplashVo() == null) {
            return null;
        }
        // 图片是否存在
        String path = DemoCache.readSplashPicture();
        if (TextUtils.isEmpty(path)) {
            return null;
        } else {
            if (!new File(path).exists()) {
                downloadSplashPicture(path);
                return null;
            }
        }
        return initInfo;
    }

    private InitModel() {
        IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean available = NetworkUtils.isNetworkAvailable(context);
                if (!requesting && available) {
                    init();
                }
            }
        };
        BasicConfig.INSTANCE.getAppContext().registerReceiver(receiver, filter);
    }

    public void init() {
        if (requesting) {
            return;
        }
        requesting = true;
        OkHttpManager.getInstance().getRequest(UriProvider.getInit(), CommonParamUtil.getDefaultParam(), new OkHttpManager.MyCallBack<ServiceResult<InitInfo>>() {
            @Override
            public void onError(Exception e) {
                requesting = false;
            }

            @Override
            public void onResponse(ServiceResult<InitInfo>  initResult) {
                requesting = false;
                if (receiver != null) {
                    BasicConfig.INSTANCE.getAppContext().unregisterReceiver(receiver);
                    receiver = null;
                }
                if (initResult != null && initResult.getData() != null) {
                    // 如果在线的和本地的不一致，则需要更新
                    DemoCache.saveInitInfo(initResult.getData());
                    DemoCache.saveInitInfoSavingTime(System.currentTimeMillis());
                    if(null != initResult.getData().getSplashVo()){
                        downloadSplashPicture(initResult.getData().getSplashVo().getPict());
                    }

                    InitInfo data = initResult.getData();
                    // 表情
                    if (data != null && data.getFaceJson() != null) {
                        CoreManager.getCore(IFaceCore.class)
                                .onReceiveOnlineFaceJson(data.getFaceJson().getJson());
                    }
                }
            }
        });
    }

    private void downloadSplashPicture(final String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        if (path.equals(DemoCache.readSplashPicture()) && new File(path).exists()) {
            return;
        }
        Single.create((SingleOnSubscribe<Boolean>) e -> {
            FutureTarget<File> target = GlideApp.with(BasicConfig.INSTANCE.getAppContext())
                    .asFile().load(path).submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
            try {
                File file = target.get();
                if (file != null) {
                    DemoCache.saveSplashPicture(file.getAbsolutePath());
                }
                if (e != null) {
                    e.onSuccess(true);
                }
            } catch (Exception e1) {
                DemoCache.saveSplashPicture("");
                if (e != null) {
                    e.onError(e1);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe();
    }
}
