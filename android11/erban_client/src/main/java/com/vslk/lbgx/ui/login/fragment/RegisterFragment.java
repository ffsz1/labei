package com.vslk.lbgx.ui.login.fragment;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.tongdaxing.xchat_core.login.view.ILoginView;
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
//    @BindView(R.id.et_pw_sure)
//    EditText etPwSure;
    @BindView(R.id.iv_pw_is_show)
    ImageView ivPwIsShow;
//    @BindView(R.id.iv_pw_is_show_sure)
//    ImageView ivPwIsShowSure;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.tv_login1)
    TextView tvLogin1;
    //    @BindView(R.id.ll_login)
//    LinearLayout llLogin;
    private NewCodeDownTimer timer;
    private String errorStr;
    private String phone;
    private String psw/*,pswSure*/;
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
        return R.layout.fragment_register;
    }

    @Override
    public void onFindViews() {
        tvGetCode.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        tvLogin1.setOnClickListener(this);
        ivPwIsShow.setOnClickListener(this);
//        ivPwIsShowSure.setOnClickListener(this);
//        llLogin.setOnClickListener(v -> {
//            if (mRegisterListener != null) mRegisterListener.onToLogin();
//        });
    }


    @Override
    public void initiate() {
    }


    private boolean checkEnableLogin() {
        return !TextUtils.isEmpty(etPhone.getText().toString()) &&
                !TextUtils.isEmpty(etAuthCodel.getText().toString()) &&
                !TextUtils.isEmpty(etPw.getText().toString())/*&&
                !TextUtils.isEmpty(etPwSure.getText().toString())*/;
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

//        etPwSure.addTextChangedListener(new TextWatcherListener() {
//            @Override
//            public void afterTextChanged(Editable editable) {
//                tvLogin.setEnabled(checkEnableLogin());
//            }
//        });
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
        ToastUtils.showShort(error);
    }

    @CoreEvent(coreClientClass = IAuthClient.class)
    public void onSmsSuccess() {
        timer = new NewCodeDownTimer(tvGetCode, 60000, 1000);
        timer.start();
    }

    private boolean isOK(String phone, String psw /*,String pswSure*/) {
        if (StringUtils.isEmpty(phone)) {
            errorStr = "请填写手机号码！";
            return false;
        }
        if (psw.length() < 6) {
            errorStr = "请输入6~20位密码！";
            return false;
        }
//        if (!psw.equals(pswSure)) {
//            errorStr = "前后密码输入不一致！";
//            return false;
//        }
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
//        if (etPwSure != null) {
//            etPwSure.addTextChangedListener(null);
//        }
        if (getDialogManager() != null) {
            getDialogManager().dismissDialog();
        }
    }

    private boolean flag = false;
    private boolean flagSure = false;

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
            /*case R.id.iv_pw_is_show_sure:
                if (flagSure) {
                    flagSure = false;
                    ivPwIsShowSure.setImageResource(R.mipmap.ic_password_pre);
                    etPwSure.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    flagSure = true;
                    ivPwIsShowSure.setImageResource(R.mipmap.ic_password_nor);
                    etPwSure.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                etPwSure.setSelection(etPwSure.length());
                break;*/
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
//                pswSure = etPwSure.getText().toString();
                String smsCode = etAuthCodel.getText().toString();
                if (!StringUtils.isEmpty(smsCode)) {
                    if (isOK(phone, psw/*,pswSure*/)) {
                        getDialogManager().showProgressDialog(getActivity(), "正在注册...");
                        CoreManager.getCore(IAuthCore.class).register(phone, smsCode, psw);
                    } else {
                        toast(errorStr);
                    }
                } else {
                    toast("验证码不能为空");
                }
                break;
            case R.id.tv_login1:
                if (iLoginView != null) {
                    iLoginView.openLoginPage();
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
