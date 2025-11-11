package com.vslk.lbgx.ui.login.fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vslk.lbgx.base.fragment.BaseMvpFragment;
import com.tongdaxing.xchat_core.login.presenter.RegisterPresenter;
import com.tongdaxing.xchat_core.login.view.IRegisterView;
import com.vslk.lbgx.ui.widget.NewCodeDownTimer;
import com.vslk.lbgx.ui.widget.TextWatcherListener;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.xchat_core.auth.IAuthClient;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.utils.VerifyPhoneUtils;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.util.StringUtils;

import butterknife.BindView;

/**
 * Function:
 * Author: Edward on 2019/6/4
 */
@CreatePresenter(RegisterPresenter.class)
public class RegisterFragment extends BaseMvpFragment<IRegisterView, RegisterPresenter> implements IRegisterView, View.OnClickListener {
    public static final String TAG = "RegisterFragment";
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_auth_code)
    EditText etAuthCodel;
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;
    @BindView(R.id.et_pw)
    EditText etPw;
    @BindView(R.id.iv_pw_is_show)
    ImageView ivPwIsShow;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.ll_login)
    LinearLayout llLogin;
    private NewCodeDownTimer timer;
    private String errorStr;
    private String phone;
    private String psw;

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_register;
    }

    @Override
    public void onFindViews() {
        tvGetCode.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        ivPwIsShow.setOnClickListener(this);
        llLogin.setOnClickListener(v -> {
            if (mRegisterListener != null) mRegisterListener.onToLogin();
        });
    }


    @Override
    public void initiate() {
    }


    private boolean checkEnableLogin() {
        return !TextUtils.isEmpty(etPhone.getText().toString()) &&
                !TextUtils.isEmpty(etAuthCodel.getText().toString()) &&
                !TextUtils.isEmpty(etPw.getText().toString());
    }

    @Override
    public void onSetListener() {
        etPhone.addTextChangedListener(new TextWatcherListener() {
            @Override
            public void afterTextChanged(Editable editable) {
                tvLogin.setEnabled(checkEnableLogin());
            }
        });

        etAuthCodel.addTextChangedListener(new TextWatcherListener() {
            @Override
            public void afterTextChanged(Editable editable) {
                tvLogin.setEnabled(checkEnableLogin());
            }
        });

        etPw.addTextChangedListener(new TextWatcherListener() {
            @Override
            public void afterTextChanged(Editable editable) {
                tvLogin.setEnabled(checkEnableLogin());
            }
        });
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
        getActivity().finish();
    }

    @CoreEvent(coreClientClass = IAuthClient.class)
    public void onSmsFail(String error) {
        toast(error);
    }

    @CoreEvent(coreClientClass = IAuthClient.class)
    public void onSmsSuccess() {
        timer = new NewCodeDownTimer(tvGetCode, 60000, 1000);
        timer.start();
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
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (etPhone != null) {
            etPhone.addTextChangedListener(null);
        }
        if (etAuthCodel != null) {
            etAuthCodel.addTextChangedListener(null);
        }
        if (etPw != null) {
            etPw.addTextChangedListener(null);
        }
        if (getDialogManager() != null) {
            getDialogManager().dismissDialog();
        }
    }

    private boolean flag = false;

    @Override
    public void onClick(View v) {
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
            case R.id.tv_get_code:
                if (etPhone.getText().length() == VerifyPhoneUtils.PHONE_LENGTH && VerifyPhoneUtils.isMatch(etPhone.getText())) {
                    CoreManager.getCore(IAuthCore.class).requestSMSCode(etPhone.getText().toString(), 1);
                } else {
                    toast("手机号码不正确");
                }
                break;
            case R.id.tv_login:
                phone = etPhone.getText().toString();
                psw = etPw.getText().toString();
                String smsCode = etAuthCodel.getText().toString();
                if (!StringUtils.isEmpty(smsCode)) {
                    if (isOK(phone, psw)) {
                        getDialogManager().showProgressDialog(getActivity(), "正在注册...");
                        CoreManager.getCore(IAuthCore.class).register(phone, smsCode, psw);
                    } else {
                        toast(errorStr);
                    }
                } else {
                    toast("验证码不能为空");
                }
                break;
            default:
                break;
        }
    }

    private IRegisterListener mRegisterListener;

    public void setRegisterListener(IRegisterListener mRegisterListener) {
        this.mRegisterListener = mRegisterListener;
    }

    public interface IRegisterListener {
        void onToLogin();
    }
}
