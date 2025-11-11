package com.vslk.lbgx.ui.me.setting.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.vslk.lbgx.ui.login.CodeDownTimer;
import com.vslk.lbgx.ui.me.setting.presenter.UnbindingQQPresenter;
import com.vslk.lbgx.ui.me.setting.vew.IBindingQQView;
import com.hncxco.library_ui.widget.AppToolBar;
import com.orhanobut.logger.Logger;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tongdaxing.xchat_core.Constants.QQ_PLATFORM_CODE;

/**
 * Function:
 * Author: Edward on 2019/4/11
 */
@CreatePresenter(UnbindingQQPresenter.class)
public class UnbindingQQActivity extends BaseMvpActivity<IBindingQQView, UnbindingQQPresenter> implements IBindingQQView, View.OnClickListener {
    @BindView(R.id.et_phone)
    TextView etPhone;
    @BindView(R.id.et_verification_code)
    EditText etVerficationCode;
    @BindView(R.id.tv_get_verification_code)
    TextView tvGetVerificationCode;
    @BindView(R.id.tv_confirm_binding)
    TextView tvConfirmBinding;
    @BindView(R.id.app_tool_bar)
    AppToolBar appToolBar;
    private final String TAG = "UnbindingQQActivity";

    public static void startForResult(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, UnbindingQQActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_account_binding);
        ButterKnife.bind(this);
        UserInfo userInfo = CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo();
        if (userInfo != null) {
            etPhone.setText(userInfo.getPhone());
        }
        tvGetVerificationCode.setOnClickListener(this);
        tvConfirmBinding.setOnClickListener(this);
        setAppToolBar();
    }

    protected void setAppToolBar() {
        appToolBar.setOnBackBtnListener(v -> finish());
    }

    private CodeDownTimer mTimer;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_confirm_binding:
                commitBinding();
                break;
            case R.id.tv_get_verification_code:
                getMvpPresenter().sendSmsCode();
                mTimer = new CodeDownTimer(tvGetVerificationCode, 60000, 1000);
                mTimer.start();
                break;
        }
    }

    @Override
    public void sendSmsSucceed() {
        Logger.i(TAG, "发送验证码成功");
    }

    @Override
    public void sendSmsFailure() {
        Logger.i(TAG, "发送验证码失败");
    }

    private void commitBinding() {
        if (TextUtils.isEmpty(etPhone.getText().toString())) {
            toast("手机号不能为空");
            return;
        }
        String codeStr = etVerficationCode.getText().toString();
        if (TextUtils.isEmpty(codeStr)) {
            toast("验证码不能为空");
            return;
        }
        getDialogManager().showProgressDialog(this, "请稍后...");
        Logger.i(TAG, "验证码: " + codeStr);
        unbinding(codeStr);
    }

    protected void unbinding(String codeStr) {
        getMvpPresenter().verificationCodePresenter(codeStr, QQ_PLATFORM_CODE);
    }

//    protected void getThirdPlatformAccessToken() {
//        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
//        if (!wechat.isClientValid()) {
//            toast("未安装微信");
//            return;
//        }
//        if (wechat.isAuthValid()) {
//            wechat.removeAccount(true);
//        }
//
//        wechat.setPlatformActionListener(new PlatformActionListener() {
//            @Override
//            public void onComplete(final Platform platform, int i, HashMap<String, Object> hashMap) {
//                if (i == Platform.ACTION_USER_INFOR) {
//                    String token = platform.getDb().getToken();
//                    String openId = platform.getDb().getUserId();
//                    String unionId = platform.getDb().get("unionid");
//                    getMvpPresenter().bindingThird(2, openId, unionId, token);
//                }
//            }
//
//            @Override
//            public void onError(Platform platform, int i, Throwable throwable) {
//                getDialogManager().dismissDialog();
//                if (null != throwable) {
//                    toast("绑定失败");
//                }
//            }
//
//            @Override
//            public void onCancel(Platform platform, int i) {
//                getDialogManager().dismissDialog();
//            }
//        });
//        wechat.SSOSetting(false);
//        wechat.showUser(null);
//    }

    @Override
    public void bindingSucceed(int type) {
        Logger.i(TAG, "解绑成功返回数据: " + type);
        Intent intent = new Intent();
        intent.putExtra("type", type);
        setResult(Activity.RESULT_OK, intent);
        SingleToastUtil.showToast("解绑成功");
        getDialogManager().dismissDialog();
        finish();
    }

    @Override
    public void bindingFailure(String errorStr) {
        toast(errorStr);
        getDialogManager().dismissDialog();
    }
}













