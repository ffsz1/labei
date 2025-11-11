package com.tongdaxing.erban.libcommon.base.factory;


import com.tongdaxing.erban.libcommon.base.AbstractMvpPresenter;
import com.tongdaxing.erban.libcommon.base.IMvpBaseView;

/**
 * <p> Presenter 工厂接口</p>
 *
 * @author jiahui
 * @date 2017/12/7
 */
public interface PresenterMvpFactory<V extends IMvpBaseView, P extends AbstractMvpPresenter<V>> {
    /**
     * 创建Presenter方法
     *
     * @return 创建的Presenter
     */
    P createMvpPresenter();
}
