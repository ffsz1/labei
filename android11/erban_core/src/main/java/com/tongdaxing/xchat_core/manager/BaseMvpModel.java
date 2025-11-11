package com.tongdaxing.xchat_core.manager;

import com.netease.nimlib.sdk.util.api.RequestResult;
import com.tongdaxing.erban.libcommon.listener.CallBack;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;

import java.net.UnknownHostException;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;

/**
 * <p>  </p>
 *
 * @author jiahui
 * @date 2017/12/12
 */
public class BaseMvpModel {

    public static final int RESULT_OK = 200;
    protected static final int WAIT_IMMCLIENT_TIME = 3000;

    public <T> void execute(Observable<ServiceResult<T>> observable, final CallBack<T> callBack) {
        observable.subscribe(new DisposableObserver<ServiceResult<T>>() {
            @Override
            public void onNext(ServiceResult<T> tServiceResult) {
                if (tServiceResult != null) {
                    if (tServiceResult.isSuccess()) {
                        if (callBack != null) {
                            callBack.onSuccess(tServiceResult.getData());
                        }
                    } else {
                        if (callBack != null) {
                            callBack.onFail(-1, tServiceResult.getError());
                        }
                    }
                } else {
                    if (callBack != null) {
                        callBack.onFail(-1, "未知错误!");
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (callBack != null) {
                    String error = e.getMessage();
                    if (e instanceof UnknownHostException) {
                        error = "网络错误";
                    }
                    callBack.onFail(-1, error);
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 异步执行云信操作
     *
     * @param requestResult --
     * @param e             --
     * @param <T>           --
     */
    public <T> void executeNIMClient(RequestResult<T> requestResult, ObservableEmitter<T> e) {
        if (e.isDisposed()) return;
        if (requestResult.exception != null) {
            e.onError(requestResult.exception);
        } else if (requestResult.code != RESULT_OK) {
            e.onError(new Throwable(String.valueOf(requestResult.code)));
        } else {
            e.onNext(requestResult.data);
        }
        e.onComplete();
    }

    /**
     * 异步执行云信操作
     *
     * @param requestResult --
     * @param e             --
     * @param <T>           --
     */
    public <T> void executeNIMClient(RequestResult<T> requestResult, SingleEmitter<T> e) {
        if (e.isDisposed()) return;
        if (requestResult.exception != null) {
            e.onError(requestResult.exception);
        } else if (requestResult.code != RESULT_OK) {
            e.onError(new Throwable(String.valueOf(requestResult.code)));
        } else {
            e.onSuccess(requestResult.data);
        }
    }

    protected <T> Function<ServiceResult<T>, ObservableSource<T>> getFunction() {
        return new Function<ServiceResult<T>, ObservableSource<T>>() {
            @Override
            public ObservableSource<T> apply(ServiceResult<T> roomInfoServiceResult) throws Exception {
                if (roomInfoServiceResult == null)
                    return Observable.error(new Throwable("roomInfoServiceResult == null"));
                if (roomInfoServiceResult.isSuccess())
                    Observable.just(roomInfoServiceResult.getData());
                return Observable.error(new Throwable(
                        roomInfoServiceResult.getCode() + "-" + roomInfoServiceResult.getErrorMessage()));
            }
        };
    }

    protected <T> Function<Throwable, ObservableSource<? extends ServiceResult<T>>> getCommonExceptionFunction() {
        return new Function<Throwable, ObservableSource<? extends ServiceResult<T>>>() {
            @Override
            public ObservableSource<? extends ServiceResult<T>> apply(Throwable throwable) throws Exception {
                ServiceResult<String> errorResult = new ServiceResult<>();
                if (throwable instanceof UnknownHostException) {
                    errorResult.setCode(ServiceResult.NOT_NET);
                } else {
                    errorResult.setCode(ServiceResult.OTHER);
                }
                return Observable.error(new Throwable(errorResult.getErrorMessage()));
            }
        };
    }


    protected <T> Function<ServiceResult<T>, SingleSource<T>> getSingleFunction() {
        return new Function<ServiceResult<T>, SingleSource<T>>() {
            @Override
            public SingleSource<T> apply(ServiceResult<T> roomInfoServiceResult) throws Exception {
                if (roomInfoServiceResult == null)
                    return Single.error(new Throwable("roomInfoServiceResult == null"));
                if (roomInfoServiceResult.isSuccess())
                    return Single.just(roomInfoServiceResult.getData());
                return Single.error(new Throwable(roomInfoServiceResult.getErrorMessage()));
            }
        };
    }


    protected <T> Function<Throwable, SingleSource<? extends ServiceResult<T>>> getSingleCommonExceptionFunction() {
        return new Function<Throwable, SingleSource<? extends ServiceResult<T>>>() {
            @Override
            public SingleSource<? extends ServiceResult<T>> apply(Throwable throwable) throws Exception {
                ServiceResult<String> errorResult = new ServiceResult<>();
                if (throwable instanceof UnknownHostException) {
                    errorResult.setCode(ServiceResult.NOT_NET);
                } else {
                    errorResult.setCode(ServiceResult.OTHER);
                }
                return Single.error(new Throwable(errorResult.getErrorMessage()));
            }
        };
    }

    /**
     * get请求（完成返回接口数据，包括status code等）
     */
    protected void getRequest(String url, Map<String, String> params, final OkHttpManager.MyCallBack callBack) {
        OkHttpManager.getInstance().getRequest(url, params, callBack);
    }

    /**
     * get请求（完成返回接口数据，包括status code等）
     */
    protected void postRequest(String url, Map<String, String> params, final OkHttpManager.MyCallBack callBack) {
        OkHttpManager.getInstance().doPostRequest(url, params, callBack);
    }
}
