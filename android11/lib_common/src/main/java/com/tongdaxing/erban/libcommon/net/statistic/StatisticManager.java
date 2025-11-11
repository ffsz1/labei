package com.tongdaxing.erban.libcommon.net.statistic;

import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.erban.libcommon.net.rxnet.utils.RxNetLog;
import com.tongdaxing.xchat_framework.util.util.Json;
import com.tongdaxing.xchat_framework.util.util.LogUtil;

import java.util.Map;

import io.reactivex.functions.BiConsumer;

/**
 * <p> 统计接口管理 </p>
 *
 * @author jiahui
 * @date 2018/1/4
 */
public class StatisticManager {
    private static final Object SYNC_OBJ = new Object();
    private volatile static StatisticManager mInstance;
    private final StatisticModel mStatisticModel;

    private StatisticManager() {
        mStatisticModel = new StatisticModel();
    }

    public static StatisticManager get() {
        if (mInstance == null) {
            synchronized (SYNC_OBJ) {
                if (mInstance == null) {
                    mInstance = new StatisticManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 向服务器发送日志
     *
     * @param params
     * @return
     */
    public void sendStatisticToService(final Map<String, String> params) {
        mStatisticModel.sendStatisticToService(params).subscribe(new BiConsumer<Object, Throwable>() {
            @Override
            public void accept(Object o, Throwable throwable) throws Exception {
                if (throwable != null) {
                    RxNetLog.e("向服务端发送日志失败....");
                    throwable.printStackTrace();
                } else {
                    RxNetLog.i("向服务端发送日志成功:%s", params.toString());
                }
            }
        });
    }

    /**
     * 向服务器发送日志
     *
     * @param params
     * @return
     */
    public void sendStatisticToService(String url, Map<String, String> params) {
        mStatisticModel.sendStatisticToService(url,params,new OkHttpManager.MyCallBack<Json>() {
            @Override
            public void onError(Exception e) {
                if (e != null)
                    LogUtil.d("statistic","向服务端发送日志失败....");
            }

            @Override
            public void onResponse(Json response) {
                if (response != null && response.num("code") == 200){
                    LogUtil.d("statistic","向服务端发送日志成功....");
                }else {
                    LogUtil.d("statistic","向服务端发送日志失败....");
                }
            }
        });
    }
}
