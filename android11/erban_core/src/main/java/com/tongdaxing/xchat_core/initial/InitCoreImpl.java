package com.tongdaxing.xchat_core.initial;

import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;

/**
 * @author xiaoyu
 * @date 2017/12/8
 */

public class InitCoreImpl extends AbstractBaseCore implements IInitCore {
//    private static final String TAG = "InitCoreImpl";
//    private boolean requesting;
//    private boolean success;
//    private Context context;
//    private BroadcastReceiver receiver;
//
//    public InitCoreImpl() {
//        context = BasicConfig.INSTANCE.getAppContext();
//        IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
//        receiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                boolean available = NetworkUtils.isNetworkAvailable(context);
//                if (!requesting && available) {
//                    init(true);
//                }
//            }
//        };
//        context.registerReceiver(receiver, filter);
//    }
//
//    @Override
//    public void init(boolean force) {
//        if (requesting) return;
//        if (!force && success) {
//            success = false;
//            return;
//        }
//        requesting = true;
//        RequestParam requestParam = CommonParamUtil.fillCommonParam();
//        ResponseListener listener = new ResponseListener<InitResult>() {
//            @Override
//            public void onResponse(InitResult response) {
//                if (!response.isSuccess()) return;
//                requesting = false;
//                success = true;
//                if (receiver != null) {
//                    context.unregisterReceiver(receiver);
//                    receiver = null;
//                }
//                Init data = response.getData();
//                // 表情
//                if (data != null &&
//                        data.getFaceJson() != null) {
//                    CoreManager.getCore(IFaceCore.class)
//                            .onReceiveOnlineFaceJson(data.getFaceJson().getJson());
//                }
//                // 闪屏
//                if (data != null && data.getSplashVo() != null) {
//                    requesting = false;
//
//                }
//                // 以后初始化模块通知
//            }
//        };
//
//        ResponseErrorListener errorListener = new ResponseErrorListener() {
//            @Override
//            public void onErrorResponse(RequestError error) {
//                requesting = false;
//                LogUtil.e(TAG, error.getErrorStr());
//            }
//        };
//
//        RequestManager.instance()
//                .submitJsonResultQueryRequest(UriProvider.getInit(),
//                        CommonParamUtil.getDefaultHeaders(getContext()),
//                        requestParam, listener, errorListener,
//                        InitResult.class, Request.Method.GET);
//    }
}
