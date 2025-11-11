package com.tongdaxing.erban.libcommon.base.factory;

import android.os.Bundle;
import android.util.Log;

import com.tongdaxing.erban.libcommon.base.AbstractMvpPresenter;
import com.tongdaxing.erban.libcommon.base.IMvpBaseView;


/**
 * <p> 管理Presenter的声明周期以及与View之间的关联</p>
 *
 * @author jiahui
 * @date 2017/12/8
 */
public class BaseMvpProxy<V extends IMvpBaseView, P extends AbstractMvpPresenter<V>>
        implements PresenterProxyInterface<V, P> {
    private static final String TAG = "super-mvp";
    private static final String KEY_PRESENTER = "key_presenter";

    private PresenterMvpFactory<V, P> mMvpFactory;
    private P mPresenter;
    private Bundle mBundle;
    private boolean mIsAttachView;

    public BaseMvpProxy(PresenterMvpFactory<V, P> mvpFactory) {
        this.mMvpFactory = mvpFactory;
    }


    @Override
    public void setPresenterFactory(PresenterMvpFactory<V, P> presenterFactory) {
        if (mPresenter != null) {
            throw new IllegalArgumentException("这个方法只能在getMvpPresenter()之前调用，如果Presenter已经创建了则不能再更改！！！");
        }
        this.mMvpFactory = presenterFactory;
    }

    @Override
    public PresenterMvpFactory<V, P> getPresenterFactory() {
        return mMvpFactory;
    }

    @Override
    public P getMvpPresenter() {
        Log.i(TAG, "Proxy getMvpPresenter...");
        //如果之前创建过且是意外销毁则从Bundle中恢复
        if (mMvpFactory != null) {
            if (mPresenter == null) {
                mPresenter = mMvpFactory.createMvpPresenter();
                mPresenter.onCreatePresenter(mBundle == null ? null : mBundle.getBundle(KEY_PRESENTER));
            }
        }
        Log.i(TAG, "Proxy getMvpPresenter..." + mPresenter);
        return mPresenter;
    }

    /** 销毁Presenter持有的View */
    private void onDetachMvpView() {
        Log.i(TAG, "Proxy onDetachMvpView...");
        if (mPresenter != null && mIsAttachView) {
            mPresenter.detachMvpView();
            mIsAttachView = false;
        }
    }

    public void onStart() {
        Log.i(TAG, "Proxy onStart...");
        if (mPresenter != null)
            mPresenter.onStartPresenter();
    }

    /**
     * 绑定Presenter与View
     *
     * @param mvpView 当前view接口类型
     */
    public void onResume(V mvpView) {
        getMvpPresenter();
        Log.i(TAG, "Proxy onResume...");
        if (mPresenter != null && !mIsAttachView) {
            mPresenter.attachMvpView(mvpView);
            mIsAttachView = true;
            mPresenter.onResumePresenter();
        }
    }

    public void onPause() {
        Log.i(TAG, "Proxy onPause...");
        if (mPresenter != null)
            mPresenter.onPausePresenter();
    }

    public void onStop() {
        Log.i(TAG, "Proxy onStop...");
        if (mPresenter != null)
            mPresenter.onStopPresenter();
    }


    /** 销毁Presenter */
    public void onDestroy() {
        Log.i(TAG, "Proxy onDestroy...");
        if (mPresenter != null) {
            onDetachMvpView();
            mPresenter.onDestroyPresenter();
            mPresenter = null;
        }
    }

    /**
     * 意外销毁的时候调用
     *
     * @return Bundle ，存入回调给Presenter的Bundle和当前Presenter的id,在调用方（activity）中保存
     */
    public Bundle onSaveInstanceState() {
        Log.i(TAG, "Proxy onSaveInstanceState...");
        Bundle bundle = new Bundle();
        getMvpPresenter();
        if (mPresenter != null) {
            Bundle presenterBundle = new Bundle();
            //回到Presenter
            mPresenter.onSaveInstanceState(presenterBundle);
            //保存presenterBundle
            bundle.putBundle(KEY_PRESENTER, presenterBundle);
        }
        return bundle;
    }

    /***
     * 意外关闭Presenter的时候恢复Presenter
     * @param saveInstanceState 意外关闭Presenter时存储的Bundle
     */
    public void onRestoreInstanceState(Bundle saveInstanceState) {
        Log.i(TAG, "Proxy onRestoreInstanceState... Presenter=" + mPresenter);
        mBundle = saveInstanceState;
    }

}
