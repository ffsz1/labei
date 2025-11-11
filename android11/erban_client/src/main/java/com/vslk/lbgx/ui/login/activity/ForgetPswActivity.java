package com.vslk.lbgx.ui.login.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.vslk.lbgx.base.activity.BaseActivity;
import com.vslk.lbgx.ui.login.CodeDownTimer;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.databinding.ActivityForgetPswBinding;
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
public class ForgetPswActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    public static void start(Context context) {
        Intent intent = new Intent(context, ForgetPswActivity.class);
        context.startActivity(intent);
    }

    private String errorStr;
    private static final String TAG = "RegisterActivity_11";
    private ActivityForgetPswBinding forgetPswBinding;
    private CodeDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forgetPswBinding = DataBindingUtil.setContentView(this, R.layout.activity_forget_psw);
        onFindViews();
        onSetListener();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if ((forgetPswBinding.etPhone.getText() != null && forgetPswBinding.etPhone.getText().length() == 11) &&
                (forgetPswBinding.etCode.getText() != null && forgetPswBinding.etCode.getText().length() == 5) &&
                (forgetPswBinding.etPassword.getText() != null && forgetPswBinding.etPassword.getText().length() > 5)) {
            forgetPswBinding.btnModify.setEnabled(true);
        } else {
            forgetPswBinding.btnModify.setEnabled(false);
        }
    }


    public void onFindViews() {
        forgetPswBinding.etPhone.addTextChangedListener(this);
        forgetPswBinding.etCode.addTextChangedListener(this);
        forgetPswBinding.etPassword.addTextChangedListener(this);
    }

    public void onSetListener() {
        forgetPswBinding.setClick(this);
        forgetPswBinding.toolbar.setOnBackBtnListener(view -> finish());
    }

    @CoreEvent(coreClientClass = IAuthClient.class)
    public void onSmsFail(String error) {
        toast(error);
        Logger.error(TAG, "获取短信失败!");
    }

    @CoreEvent(coreClientClass = IAuthClient.class)
    public void onModifyPsw() {
        toast("重置密码成功！");
        finish();
    }

    @CoreEvent(coreClientClass = IAuthClient.class)
    public void onModifyPswFail(String error) {
        toast(error);
        System.out.println("error===" + error);
    }

    @Override
    public void onClick(View v) {
        String phone = forgetPswBinding.etPhone.getText().toString();
        switch (v.getId()) {
            case R.id.btn_modify:
                String psw = forgetPswBinding.etPassword.getText().toString();
                String smsCode = forgetPswBinding.etCode.getText().toString();
                if (!StringUtils.isEmpty(smsCode)) {
                    if (isOK(phone, psw)) {
                        CoreManager.getCore(IAuthCore.class).requestResetPsw(phone, smsCode, psw);
                    } else {
                        toast(errorStr);
                    }
                } else {
                    toast("验证码不能为空！");
                }
                break;
            case R.id.btn_get_code:
                if (phone.length() == 11 && VerifyPhoneUtils.isMatch(phone)) {
                    timer = new CodeDownTimer(forgetPswBinding.btnGetCode, 60000, 1000);
                    timer.start();
                    CoreManager.getCore(IAuthCore.class).requestSMSCode(phone, 3);
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
        if (forgetPswBinding.etPhone != null) {
            forgetPswBinding.etPhone.addTextChangedListener(null);
        }
        if (forgetPswBinding.etCode != null) {
            forgetPswBinding.etCode.addTextChangedListener(null);
        }
        if (forgetPswBinding.etPassword != null) {
            forgetPswBinding.etPassword.addTextChangedListener(null);
        }
    }
}
