package com.vslk.lbgx.ui.login.fragment;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.vslk.lbgx.base.fragment.BaseMvpFragment;
import com.tongdaxing.xchat_core.login.presenter.ResetPwPresenter;
import com.tongdaxing.xchat_core.login.view.ILoginView;
import com.tongdaxing.xchat_core.login.view.IResetPwView;
import com.vslk.lbgx.ui.widget.NewCodeDownTimer;
import com.vslk.lbgx.ui.widget.TextWatcherListener;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.xchat_core.auth.IAuthClient;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.utils.VerifyPhoneUtils;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import butterknife.BindView;

/**
 * Function: 密码重置
 * Author: Edward on 2019/6/5
 */
@CreatePresenter(ResetPwPresenter.class)
public class ResetPwFragment extends BaseMvpFragment<IResetPwView, ResetPwPresenter> implements IResetPwView, View.OnClickListener {
    public static final String TAG = "ResetPwFragment";
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_auth_code)
    EditText etAuthCode;
    @BindView(R.id.et_pw)
    EditText etPw;
    @BindView(R.id.et_repeat_pw)
    EditText etRepeatPw;
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;
    @BindView(R.id.iv_pw_is_show)
    ImageView ivPwIsShow;
    @BindView(R.id.iv_pw_is_show_repeat)
    ImageView ivPwIsShowRepeat;
    private NewCodeDownTimer timer;
    private ILoginView iLoginView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ILoginView) {
            iLoginView = (ILoginView) context;
        }
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_reset_pw;
    }

    @Override
    public void onFindViews() {
        ivPwIsShow.setOnClickListener(this);
        ivPwIsShowRepeat.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        tvGetCode.setOnClickListener(this);
    }

    @Override
    public void initiate() {
    }

    private boolean checkEnableLogin() {
        return !TextUtils.isEmpty(etPhone.getText().toString()) &&
                !TextUtils.isEmpty(etAuthCode.getText().toString()) &&
                !TextUtils.isEmpty(etPw.getText().toString()) &&
                !TextUtils.isEmpty(etRepeatPw.getText().toString());
    }

    @Override
    public void onSetListener() {
        etPhone.addTextChangedListener(new TextWatcherListener() {
            @Override
            public void afterTextChanged(Editable editable) {
                tvConfirm.setEnabled(checkEnableLogin());
            }
        });

        etAuthCode.addTextChangedListener(new TextWatcherListener() {
            @Override
            public void afterTextChanged(Editable editable) {
                tvConfirm.setEnabled(checkEnableLogin());
            }
        });

        etPw.addTextChangedListener(new TextWatcherListener() {
            @Override
            public void afterTextChanged(Editable editable) {
                tvConfirm.setEnabled(checkEnableLogin());
            }
        });

        etRepeatPw.addTextChangedListener(new TextWatcherListener() {
            @Override
            public void afterTextChanged(Editable editable) {
                tvConfirm.setEnabled(checkEnableLogin());
            }
        });
    }

    @CoreEvent(coreClientClass = IAuthClient.class)
    public void onSmsFail(String error) {
        toast(error);
    }

    private void clearContextText() {
        etPhone.setText("");
        etAuthCode.setText("");
        etPw.setText("");
        etRepeatPw.setText("");
    }

    @CoreEvent(coreClientClass = IAuthClient.class)
    public void onModifyPsw() {
        toast("重置密码成功！");
        clearContextText();
        if (iLoginView != null) {//重置密码成功之后跳转到登陆页
            iLoginView.openPhoneLoginPage(true);
        }
    }

    @CoreEvent(coreClientClass = IAuthClient.class)
    public void onModifyPswFail(String error) {
        toast(error);
    }

    private boolean flag = false;
    private boolean flagRepeat = false;

    @Override
    public void onClick(View v) {
        String phone = etPhone.getText().toString();
        switch (v.getId()) {
            case R.id.iv_pw_is_show:
                if (flag) {
                    flag = false;
                    ivPwIsShow.setImageResource(R.mipmap.ic_password_pre);
                    etPw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    flag = true;
                    ivPwIsShow.setImageResource(R.mipmap.ic_password_nor);
                    etPw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                etPw.setSelection(etPw.length());
                break;
            case R.id.iv_pw_is_show_repeat:
                if (flagRepeat) {
                    flagRepeat = false;
                    ivPwIsShowRepeat.setImageResource(R.mipmap.ic_password_pre);
                    etRepeatPw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    flagRepeat = true;
                    ivPwIsShowRepeat.setImageResource(R.mipmap.ic_password_nor);
                    etRepeatPw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                etRepeatPw.setSelection(etRepeatPw.length());
                break;
            case R.id.tv_get_code:
                if (phone.length() == 11 && VerifyPhoneUtils.isMatch(phone)) {
                    timer = new NewCodeDownTimer(tvGetCode, 60000, 1000);
                    timer.start();
                    CoreManager.getCore(IAuthCore.class).requestSMSCode(phone, 3);
                } else {
                    toast("手机号码不正确");
                }
                break;
            case R.id.tv_confirm:
                String psw = etPw.getText().toString();
                String pswRepeat = etRepeatPw.getText().toString();
                String smsCode = etAuthCode.getText().toString();
                if (isOK(phone, psw, pswRepeat, smsCode)) {
                    CoreManager.getCore(IAuthCore.class).requestResetPsw(phone, smsCode, psw);
                }
                break;
        }
    }

    private boolean isOK(String phone, String psw, String pswRepeat, String smsCode) {
        if (TextUtils.isEmpty(phone)) {
            toast("手机号码不能为空！");
            return false;
        }

        if (TextUtils.isEmpty(smsCode)) {
            toast("验证码不能为空！");
            return false;
        }

        if (TextUtils.isEmpty(psw) || TextUtils.isEmpty(pswRepeat)) {
            toast("密码不能为空！");
            return false;
        }

        if (psw.length() < 6 || pswRepeat.length() < 6) {
            toast("密码不能少于6位！");
            return false;
        }

        if (!psw.equals(pswRepeat)) {
            toast("密码不一致！");
            return false;
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
