package com.tongdaxing.erban.libcommon.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.tongdaxing.erban.libcommon.base.factory.BaseMvpProxy;
import com.tongdaxing.erban.libcommon.base.factory.PresenterMvpFactory;
import com.tongdaxing.erban.libcommon.base.factory.PresenterMvpFactoryImpl;
import com.tongdaxing.erban.libcommon.base.factory.PresenterProxyInterface;
import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * <p>  1. 子类的Presenter必须继承自AbstractMvpPresenter；
 * 2. 子类的View必须继承自IMvpBaseView
 * </p>
 *
 * @author jiahui
 * @date 2017/12/8
 */
public class AbstractMvpFragment<V extends IMvpBaseView, P extends AbstractMvpPresenter<V>> extends RxFragment
        implements PresenterProxyInterface<V, P> {
    protected final String TAG = getClass().getSimpleName();
    private static final String TAG_LOG = "Super-mvp";
    private static final String KEY_SAVE_PRESENTER = "key_save_presenter";
    /** 创建代理对象，传入默认的Presenter工厂 */
    private BaseMvpProxy<V, P> mMvpProxy = new BaseMvpProxy<>(PresenterMvpFactoryImpl.<V, P>createFactory(getClass()));
    private String mFragmentName = getClass().getName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG_LOG, mFragmentName + " V onCreate...");
        Log.i(TAG_LOG, mFragmentName + " V onCreate... mProxy=" + mMvpProxy);
        Log.i(TAG_LOG, mFragmentName + " V onCreate... this=" + this.hashCode());
        if (savedInstanceState != null) {
            mMvpProxy.onRestoreInstanceState(savedInstanceState.getBundle(KEY_SAVE_PRESENTER));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG_LOG, mFragmentName + " V onStart...");
        mMvpProxy.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG_LOG, mFragmentName + " V onResume...");
        mMvpProxy.onResume((V) this);
    }

    @Override
    public void onPause() {
        mMvpProxy.onPause();
        super.onPause();
        Log.i(TAG_LOG, mFragmentName + " V onPause...");
    }

    @Override
    public void onStop() {
        mMvpProxy.onStop();
        super.onStop();
        Log.i(TAG_LOG, mFragmentName + " V onStop...");
    }

    @Override
    public void onDestroy() {
        mMvpProxy.onDestroy();
        super.onDestroy();
        Log.i(TAG_LOG, mFragmentName + " V onDestroy...");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG_LOG, mFragmentName + " V onSaveInstanceState...");
        outState.putBundle(KEY_SAVE_PRESENTER, mMvpProxy.onSaveInstanceState());
    }

    @Override
    public void setPresenterFactory(PresenterMvpFactory<V, P> presenterFactory) {
        Log.i(TAG_LOG, mFragmentName + " V setPresenterFactory...");
        mMvpProxy.setPresenterFactory(presenterFactory);
    }

    @Override
    public PresenterMvpFactory<V, P> getPresenterFactory() {
        Log.i(TAG_LOG, mFragmentName + " V getPresenterFactory...");
        return mMvpProxy.getPresenterFactory();
    }

    @Override
    public P getMvpPresenter() {
        Log.i(TAG_LOG, mFragmentName + " V getMvpPresenter...");
        return mMvpProxy.getMvpPresenter();
    }
}
