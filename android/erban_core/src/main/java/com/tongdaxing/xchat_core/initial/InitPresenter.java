package com.tongdaxing.xchat_core.initial;


import com.tongdaxing.erban.libcommon.base.AbstractMvpPresenter;

/**
 * @author xiaoyu
 * @date 2017/12/29
 */
public class InitPresenter extends AbstractMvpPresenter<IInitView> {

    private InitModel model;

    public InitPresenter() {
        model = InitModel.get();
    }

    public InitInfo getLocalSplashVo() {
        return model.getCacheInitInfo();
    }

    public void init() {
        model.init();
    }
}
