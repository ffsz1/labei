package com.tongdaxing.erban.libcommon.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.tongdaxing.erban.libcommon.listener.CallBack;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.OutsideLifecycleException;
import com.trello.rxlifecycle2.RxLifecycle;

import java.net.UnknownHostException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

/**
 * <p>MVP模式Present 基类 ,跟Activity，Fragment等生命周期绑定</p>
 *
 * @author jiahui
 * @date 2017/12/7
 */
public abstract class AbstractMvpPresenter<V extends IMvpBaseView> implements LifecycleProvider<PresenterEvent> {
    private static final String TAG = "super-mvp";
    private final BehaviorSubject<PresenterEvent> lifecycleSubject = BehaviorSubject.create();
    protected V mMvpView;

    /**
     * 绑定View
     */
    public void attachMvpView(V mvpView) {
        this.mMvpView = mvpView;
        Log.e(TAG, "Presenter attachMvpView...");
    }

    /**
     * 解除绑定的View
     */
    public void detachMvpView() {
        mMvpView = null;
        Log.e(TAG, "Presenter detachMvpView...");
    }

    /**
     * 获取V层的接口View
     *
     * @return 当前的接口View
     */
    public V getMvpView() {
        return mMvpView;
    }

    /**
     * Presenter 被创建后调用
     *
     * @param saveState 被意外销毁后的Bundle
     */
    public void onCreatePresenter(@Nullable Bundle saveState) {
        Log.e(TAG, "Presenter onCreatePresenter...");
        lifecycleSubject.onNext(PresenterEvent.CREATE);
    }

    public void onStartPresenter() {
        Log.e(TAG, "Presenter onStartPresenter...");
        lifecycleSubject.onNext(PresenterEvent.START);
    }

    public void onResumePresenter() {
        Log.e(TAG, "Presenter onResumePresenter...");
        lifecycleSubject.onNext(PresenterEvent.RESUME);
    }

    public void onPausePresenter() {
        Log.e(TAG, "Presenter onPausePresenter...");
        lifecycleSubject.onNext(PresenterEvent.PAUSE);
    }

    public void onStopPresenter() {
        Log.e(TAG, "Presenter onStopPresenter...");
        lifecycleSubject.onNext(PresenterEvent.STOP);
    }

    /**
     * Presenter被销毁的时候调用，可以在此释放资源等
     */
    public void onDestroyPresenter() {
        Log.e(TAG, "Presenter onDestroyPresenter...");
        lifecycleSubject.onNext(PresenterEvent.DESTROY);
    }

    /**
     * 在Presenter被意外销毁时调用，它的调用时机和Activity，Fragment，View中的onSaveInstanceState()方法调用时机相同
     *
     * @param outState 保存消息的Bundle
     */
    public void onSaveInstanceState(Bundle outState) {
        Log.e(TAG, "Presenter onSaveInstanceState...");
    }

    @Override
    @NonNull
    public Observable<PresenterEvent> lifecycle() {
        return lifecycleSubject.hide();
    }

    @Override
    @NonNull
    public <T> LifecycleTransformer<T> bindUntilEvent(@NonNull PresenterEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    @NonNull
    public <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycle.bind(lifecycleSubject, PRESENTER_LIFECYCLE);
//            return RxLifecycleAndroid.bindActivity(lifecycleSubject);
    }

    private final Function<PresenterEvent, PresenterEvent> PRESENTER_LIFECYCLE =
            new Function<PresenterEvent, PresenterEvent>() {
                @Override
                public PresenterEvent apply(PresenterEvent lastEvent) throws Exception {
                    switch (lastEvent) {
                        case CREATE:
                            return PresenterEvent.DESTROY;
                        case START:
                            return PresenterEvent.STOP;
                        case RESUME:
                            return PresenterEvent.PAUSE;
                        case PAUSE:
                            return PresenterEvent.STOP;
                        case STOP:
                            return PresenterEvent.DESTROY;
                        case DESTROY:
                            throw new OutsideLifecycleException("Cannot bind to Presenter lifecycle when outside of it.");
                        default:
                            throw new UnsupportedOperationException("Binding to " + lastEvent + " not yet implemented");
                    }
                }
            };


    public <T> ObservableTransformer<ServiceResult<T>, ServiceResult<T>> handleResponse() {
        return new ObservableTransformer<ServiceResult<T>, ServiceResult<T>>() {
            @Override
            public ObservableSource<ServiceResult<T>> apply(Observable<ServiceResult<T>> observable) {
                return observable;
            }
        };
    }

    public <T> void execute(Observable<ServiceResult<T>> observable, final CallBack<T> callBack) {
        observable
                .compose(this.<T>handleResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ServiceResult<T>>() {
                    @Override
                    public void onNext(ServiceResult<T> tServiceResult) {
                        if (tServiceResult != null) {
                            Log.i(TAG, "onNext: service result: " + tServiceResult);
                            if (tServiceResult.isSuccess()) {
                                if (callBack != null) {
                                    if (getMvpView() != null) {
                                        callBack.onSuccess(tServiceResult.getData());
                                    }

                                }
                            } else if (tServiceResult.getCode() == 401) {
                                //: 2018/4/24 错误提示
                                if (callBack != null) {
                                    if (getMvpView() != null) {
                                        callBack.onFail(tServiceResult.getCode(), tServiceResult.getMessage() + "");
                                    }

                                }

                            } else {
                                if (callBack != null) {
                                    if (getMvpView() != null) {
                                        callBack.onFail(-1, tServiceResult.getError());
                                    }
                                }
                            }
                        } else {
                            if (callBack != null) {
                                if (getMvpView() != null) {
                                    callBack.onFail(-1, "未知错误!");
                                }
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
                            if (getMvpView() != null) {
                                callBack.onFail(-1, error);
                            }
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
