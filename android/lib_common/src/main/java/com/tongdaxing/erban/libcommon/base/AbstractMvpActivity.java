package com.tongdaxing.erban.libcommon.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.tongdaxing.erban.libcommon.base.factory.BaseMvpProxy;
import com.tongdaxing.erban.libcommon.base.factory.PresenterMvpFactory;
import com.tongdaxing.erban.libcommon.base.factory.PresenterMvpFactoryImpl;
import com.tongdaxing.erban.libcommon.base.factory.PresenterProxyInterface;
import com.tongdaxing.erban.libcommon.swipeactivity.SwipeBackActivity;

/**
 * <p> 1. 子类的Presenter必须继承自AbstractMvpPresenter；
 * 2. 子类的View必须继承自IMvpBaseView
 * </p>
 *
 * @author jiahui
 * @date 2017/12/7
 */
public abstract class AbstractMvpActivity<V extends IMvpBaseView, P extends AbstractMvpPresenter<V>> extends SwipeBackActivity
        implements PresenterProxyInterface<V, P> {
    protected final String TAG = getClass().getSimpleName();
    private static final String TAG_LOG = "Super-mvp";
    private static final String KEY_SAVE_PRESENTER = "key_save_presenter";

    /** 创建代理对象，传入默认的Presenter工厂 */
    private BaseMvpProxy<V, P> mMvpProxy = new BaseMvpProxy<>(PresenterMvpFactoryImpl.<V, P>createFactory(getClass()));
    private final String activityName = getClass().getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBackEnable(false);
        Log.i(TAG_LOG, activityName + " V onCreate...");
        Log.i(TAG_LOG, activityName + " V onCreate... mProxy=" + mMvpProxy);
        Log.i(TAG_LOG, activityName + " V onCreate... this=" + this.hashCode());
        if (savedInstanceState != null) {
            mMvpProxy.onRestoreInstanceState(savedInstanceState.getBundle(KEY_SAVE_PRESENTER));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG_LOG, activityName + " V onStart...");
        mMvpProxy.onStart();
    }

    @Override
    protected void onResume() {
        try {
            super.onResume();
        } catch (Exception i) {
            i.printStackTrace();
        }
        Log.i(TAG_LOG, activityName + " V onResume...");
        mMvpProxy.onResume((V) this);
    }

    @Override
    protected void onPause() {
        mMvpProxy.onPause();
        super.onPause();
        Log.i(TAG_LOG, activityName + " V onPause...");
    }

    @Override
    protected void onStop() {
        mMvpProxy.onStop();
        super.onStop();
        Log.i(TAG_LOG, activityName + " V onStop...");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMvpProxy.onDestroy();
        Log.i(TAG_LOG, activityName + " V onDestroy...");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG_LOG, activityName + " V onSaveInstanceState...");
        outState.putBundle(KEY_SAVE_PRESENTER, mMvpProxy.onSaveInstanceState());
    }

    @Override
    public void setPresenterFactory(PresenterMvpFactory<V, P> presenterFactory) {
        Log.i(TAG_LOG, activityName + " V setPresenterFactory...");
        mMvpProxy.setPresenterFactory(presenterFactory);
    }

    @Override
    public PresenterMvpFactory<V, P> getPresenterFactory() {
        Log.i(TAG_LOG, activityName + " V getPresenterFactory...");
        return mMvpProxy.getPresenterFactory();
    }

    @Override
    public P getMvpPresenter() {
        Log.i(TAG_LOG, activityName + " V getMvpPresenter...");
        return mMvpProxy.getMvpPresenter();
    }
}
