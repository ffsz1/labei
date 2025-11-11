package com.tongdaxing.erban.libcommon.net.rxnet;

import android.content.Context;

import com.tongdaxing.erban.libcommon.net.rxnet.manager.RxNetManager;
import com.tongdaxing.erban.libcommon.utils.NullUtils;


/**
 * <p> 网络请求入口</p>
 *
 * @author jiahui
 *         date 2017/12/4
 */

public final class RxNet {

    private static RxNet mInstance;

    private static RxNetManager.Builder sBuilder;

    private static Context mContext;

    private RxNet() {
        sBuilder = new RxNetManager.Builder();
        sBuilder.setContext(mContext);
    }

    public static RxNet get() {
        if (mInstance == null) {
            synchronized (RxNet.class) {
                if (mInstance == null) {
                    mInstance = new RxNet();
                }
            }
        }
        return mInstance;
    }

    public static RxNetManager.Builder init(Context context) {
        mContext = context;
        get();
        return sBuilder;
    }

    public static Context getContext() {
        return mContext;
    }

    public static <T> T create(Class<T> service) {
        checkInstance();
        return sBuilder.getRxNetManager().getRetrofit().create(service);
    }


    private static void checkInstance() {
        NullUtils.checkNull(mInstance, "请在项目中先调用RxNet.init()方法初始化!!!");
    }

}
