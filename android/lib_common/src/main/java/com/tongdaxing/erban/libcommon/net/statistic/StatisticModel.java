package com.tongdaxing.erban.libcommon.net.statistic;

import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.erban.libcommon.net.rxnet.RxNet;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.util.util.Json;

import java.util.Map;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * <p>  </p>
 *
 * @author jiahui
 * @date 2018/1/4
 */
public class StatisticModel {

    public interface EventId {
        String EVENT_COMPLETE = "complete";
        String EVENT_LOGIN = "login";
        String LINKED_ME_CHANNEL = "LINKED_ME_CHANNEL";
        String EVENT_OPENROOM = "openRoom";
        String EVENT_ENTERROOM = "enterRoom";
        String KICK_USER = "kickUser";
        String ON_KICK_EXIT = "onKickExit";
    }

    private static final String SERVICE_ERROR = "服务器异常";

    /**
     * 向服务器发送日志
     *
     * @param params
     * @return
     */
    public Single<Object> sendStatisticToService(Map<String, String> params) {
        if (params == null || params.size() == 0)
            return Single.error(new Throwable("params == null"));
        return RxNet.create(StatisticService.class)
                .sendStatsticToService(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .flatMap(new Function<ServiceResult<Object>, SingleSource<?>>() {
                    @Override
                    public SingleSource<?> apply(final ServiceResult<Object> objectServiceResult) throws Exception {
                        if (objectServiceResult == null || !objectServiceResult.isSuccess())
                            return Single.error(new Throwable(
                                    objectServiceResult == null ? SERVICE_ERROR : objectServiceResult.getMessage()));
                        return Single.create(new SingleOnSubscribe<Object>() {
                            @Override
                            public void subscribe(SingleEmitter<Object> e) throws Exception {
                                e.onSuccess(objectServiceResult.getData() == null ? "成功" : objectServiceResult.getData());
                            }
                        });
                    }
                });
    }

    /**
     * 向服务器发送日志 -- 最新
     *
     * @param params
     * @return
     */
    public void sendStatisticToService(String url, Map<String, String> params, OkHttpManager.MyCallBack<Json> callBack) {
        OkHttpManager.getInstance().doPostRequest(url,params,callBack);
    }

}
