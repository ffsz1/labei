package com.vslk.lbgx.ui.me.withdraw;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.vslk.lbgx.ui.me.withdraw.presenter.NewWithdrawPresenter;
import com.vslk.lbgx.ui.me.withdraw.view.INewWithdrawView;
import com.hncxco.library_ui.widget.AppToolBar;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.xchat_framework.util.util.StringUtils;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;

import static com.tongdaxing.xchat_core.Constants.QQ_PLATFORM_CODE;

/**
 * Function:
 * Author: Edward on 2019/4/15
 */
@CreatePresenter(NewWithdrawPresenter.class)
public class BinderQQActivity extends NewBinderWeixinPayActivity implements INewWithdrawView {
    public static void startForResult(Activity context, int requestCode) {
        Intent intent = new Intent(context, BinderQQActivity.class);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onCheckCodeSucWeixinLogin() {
        String code = etCode.getText().toString().trim();
//        String realName = etRealName.getText().toString().trim();
        String phoneNumber = etPhone.getText().toString().trim();
        if (StringUtils.isEmpty(phoneNumber) || StringUtils.isEmpty(code)) {
            if (getDialogManager() != null) {
                getDialogManager().dismissDialog();
            }
            toast("手机号、验证码或真实姓名未填写完整哦");
            return;
        }
        wechat = ShareSDK.getPlatform(QQ.NAME);
        if (!wechat.isClientValid()) {
            toast("未安QQ");
            return;
        }
        if (wechat.isAuthValid()) {
            wechat.removeAccount(true);
        }
        getDialogManager().showProgressDialog(this, "加载中...");

        wechat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(final Platform platform, int i, HashMap<String, Object> hashMap) {
                if (i == Platform.ACTION_USER_INFOR) {
                    String openid = platform.getDb().getUserId();
                    String unionid = platform.getDb().get("unionid");
                    String accessToken = platform.getDb().getToken();
                    getMvpPresenter().bindWithdrawWeixin(accessToken, openid, unionid, QQ_PLATFORM_CODE);//绑定QQ，QQ没有unionid，因此不传
                } else {
                    if (getDialogManager() != null) {
                        getDialogManager().dismissDialog();
                    }
                    toast("QQ登录失败，错误码：" + i);
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                if (getDialogManager() != null) {
                    getDialogManager().dismissDialog();
                }
                toast("QQ授权错误");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                if (getDialogManager() != null) {
                    getDialogManager().dismissDialog();
                }
                toast("取消QQ授权");
            }
        });
        wechat.SSOSetting(false);
        wechat.showUser(null);
    }

    @Override
    protected void back(AppToolBar toolBar) {
        super.back(toolBar);
        toolBar.setTitle("绑定QQ");
        tvHint.setVisibility(View.GONE);
    }
}
