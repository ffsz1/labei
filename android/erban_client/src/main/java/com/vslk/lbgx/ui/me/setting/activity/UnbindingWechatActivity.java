package com.vslk.lbgx.ui.me.setting.activity;

import android.app.Activity;
import android.content.Intent;

import com.vslk.lbgx.ui.me.setting.presenter.UnbindingQQPresenter;
import com.vslk.lbgx.ui.me.setting.vew.IBindingQQView;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;

import static com.tongdaxing.xchat_core.Constants.WX_PLATFORM_CODE;

/**
 * Function:
 * Author: Edward on 2019/4/11
 */
@CreatePresenter(UnbindingQQPresenter.class)
public class UnbindingWechatActivity extends UnbindingQQActivity implements IBindingQQView {
    public static void startForResult(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, UnbindingWechatActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void setAppToolBar() {
        super.setAppToolBar();
        appToolBar.setTitle("解绑微信");
    }

    @Override
    protected void unbinding(String codeStr) {
        getMvpPresenter().verificationCodePresenter(codeStr, WX_PLATFORM_CODE);
    }
}
