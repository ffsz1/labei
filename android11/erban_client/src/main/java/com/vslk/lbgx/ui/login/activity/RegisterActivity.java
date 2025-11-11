package com.vslk.lbgx.ui.login.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;

import com.vslk.lbgx.base.activity.BaseActivity;
import com.vslk.lbgx.ui.login.CodeDownTimer;
import com.vslk.lbgx.ui.widget.TextWatcherListener;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.databinding.ActivityRegisterBinding;
import com.tongdaxing.xchat_core.auth.IAuthClient;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.utils.Logger;
import com.tongdaxing.xchat_core.utils.VerifyPhoneUtils;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.util.StringUtils;

/**
 * @author zhouxiangfeng
 * @date 17/3/5
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "RegisterActivity";
    private String errorStr;
    private String phone;
    private String psw;
    private ActivityRegisterBinding registerBinding;
    private CodeDownTimer timer;

    public static void start(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBinding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        onFindViews();
        onSetListener();
    }


    public void onFindViews() {
        registerBinding.etPassword.addTextChangedListener(new TextWatcherListener() {

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() < 6 || editable.length() > 12) {
                    registerBinding.btnRegist.setEnabled(false);
                } else {
                    registerBinding.btnRegist.setEnabled(true);
                }
            }
        });
    }

    public void onSetListener() {
        registerBinding.setClick(this);
        registerBinding.btnRegist.setOnClickListener(this);
        registerBinding.toolbar.setOnBackBtnListener(view -> finish());
    }

    @CoreEvent(coreClientClass = IAuthClient.class)
    public void onRegisterFail(String error) {
        toast(error);
        getDialogManager().dismissDialog();
    }

    @CoreEvent(coreClientClass = IAuthClient.class)
    public void onRegister() {
        toast("注册成功！");
        CoreManager.getCore(IAuthCore.class).login(phone, psw);
        getDialogManager().dismissDialog();
        finish();
    }

    @CoreEvent(coreClientClass = IAuthClient.class)
    public void onSmsFail(String error) {
        toast(error);
        Logger.error(TAG, "获取短信失败!");
    }

    @CoreEvent(coreClientClass = IAuthClient.class)
    public void onSmsSuccess() {
        timer = new CodeDownTimer(registerBinding.btnGetCode, 60000, 1000);
        timer.start();
    }

    @Override
    public void onClick(View v) {
        phone = registerBinding.etPhone.getText().toString();
        switch (v.getId()) {
            case R.id.btn_regist:
                psw = registerBinding.etPassword.getText().toString();
                String smsCode = registerBinding.etCode.getText().toString();
                if (!StringUtils.isEmpty(smsCode)) {
                    if (isOK(phone, psw)) {
                        getDialogManager().showProgressDialog(RegisterActivity.this, "正在注册...");
                        CoreManager.getCore(IAuthCore.class).register(phone, smsCode, psw);
                    } else {
                        toast(errorStr);
                    }
                } else {
                    toast("验证码不能为空");
                }
                break;
            case R.id.btn_get_code:
                if (phone.length() == VerifyPhoneUtils.PHONE_LENGTH && VerifyPhoneUtils.isMatch(phone)) {
                    CoreManager.getCore(IAuthCore.class).requestSMSCode(phone, 1);
                } else {
                    toast("手机号码不正确");
                }
                break;
            default:
                break;
        }
    }

    private boolean isOK(String phone, String psw) {
        if (StringUtils.isEmpty(psw) && psw.length() < 6) {
            errorStr = "请核对密码！";
            return false;
        }
        if (StringUtils.isEmpty(phone)) {
            errorStr = "请填写手机号码！";
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (registerBinding.etPassword != null) {
            registerBinding.etPassword.addTextChangedListener(null);
        }
        if (getDialogManager() != null) {
            getDialogManager().dismissDialog();
        }
    }
}
