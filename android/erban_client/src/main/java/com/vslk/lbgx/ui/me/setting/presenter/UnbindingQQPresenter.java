package com.vslk.lbgx.ui.me.setting.presenter;

import com.vslk.lbgx.ui.me.setting.model.BindingQQModel;
import com.vslk.lbgx.ui.me.setting.vew.IBindingQQView;
import com.tongdaxing.erban.libcommon.base.AbstractMvpPresenter;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_framework.util.util.Json;

/**
 * Function:
 * Author: Edward on 2019/4/11
 */
public class UnbindingQQPresenter extends AbstractMvpPresenter<IBindingQQView> {
    private BindingQQModel bindingQQModel;

    public UnbindingQQPresenter() {
        bindingQQModel = new BindingQQModel();
    }

    public void sendSmsCode() {
        bindingQQModel.sendSmsCode(new OkHttpManager.MyCallBack<Json>() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(Json response) {

            }
        });
    }

    /**
     * @param code
     * @param type 1是微信，2是QQ
     */
    public void verificationCodePresenter(String code, int type) {
        bindingQQModel.verificationCodeModel(code, new OkHttpManager.MyCallBack<Json>() {
            @Override
            public void onError(Exception e) {
                if (e != null) {
                    getMvpView().bindingFailure(e.getMessage());
                }
            }

            @Override
            public void onResponse(Json response) {
                if (response == null) {
                    onError(new Exception());
                    return;
                }
                if (response.num("code") == 200) {
                    unbindingThird(type);
                } else if (response.num("code") == 4003) {
                    onError(new Exception(response.str("message")));
                } else {
                    onError(new Exception());
                }
            }
        });
    }

    public void unbindingThird(int type) {
        bindingQQModel.unbindingThird(type, new OkHttpManager.MyCallBack<Json>() {
            @Override
            public void onError(Exception e) {
                if (e != null) {
                    getMvpView().bindingFailure(e.getMessage());
                }
            }

            @Override
            public void onResponse(Json response) {
                if (response.num("code") == 200) {
                    getMvpView().bindingSucceed(type);
                } else {
                    onError(new Exception(response.str("message")));
                }
            }
        });
    }

}
