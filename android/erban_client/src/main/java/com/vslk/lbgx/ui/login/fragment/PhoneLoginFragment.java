package com.vslk.lbgx.ui.login.fragment;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vslk.lbgx.base.fragment.BaseMvpFragment;
import com.tongdaxing.xchat_core.login.presenter.PhoneLoginPresenter;
import com.tongdaxing.xchat_core.login.view.ILoginView;
import com.tongdaxing.xchat_core.login.view.IPhoneLoginView;
import com.vslk.lbgx.ui.widget.TextWatcherListener;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;

import butterknife.BindView;

/**
 * Function:
 * Author: Edward on 2019/6/4
 */
@CreatePresenter(PhoneLoginPresenter.class)
public class PhoneLoginFragment extends BaseMvpFragment<IPhoneLoginView, PhoneLoginPresenter> implements IPhoneLoginView, View.OnClickListener {
    public static final String TAG = "PhoneLoginFragment";
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.tv_register_new)
    TextView tvRegister;
    @BindView(R.id.tv_forget_pw)
    TextView tvForgetPw;
    @BindView(R.id.tv_wechat_login)
    TextView tvWechatLogin;
    @BindView(R.id.tv_qq_login)
    TextView tvQQLogin;
    @BindView(R.id.tv_user_protocol)
    TextView tvUserProtocol;
    @BindView(R.id.tv_self_protocol)
    TextView tvSelfProtocol;
    @BindView(R.id.tv_live_protocol)
    TextView tvLiveProtocol;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_pw)
    EditText etPw;
    @BindView(R.id.iv_is_show)
    ImageView ivIsShow;
    @BindView(R.id.ll_register)
    LinearLayout llRegister;
    @BindView(R.id.cb)
    CheckBox cb;
    private boolean flag = false;
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
        return R.layout.fragment_phone_login;
    }

    @Override
    public void onFindViews() {
        ivIsShow.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        tvQQLogin.setOnClickListener(this);
        tvForgetPw.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        tvWechatLogin.setOnClickListener(this);
        tvUserProtocol.setOnClickListener(this);
        tvSelfProtocol.setOnClickListener(this);
        tvLiveProtocol.setOnClickListener(this);
        llRegister.setOnClickListener(this);
    }

    @Override
    public void initiate() {

    }

    private boolean checkEnableLogin() {
        return (!TextUtils.isEmpty(etPhone.getText().toString())
                && !TextUtils.isEmpty(etPw.getText().toString()));
    }

    @Override
    public void onSetListener() {
        etPhone.addTextChangedListener(new TextWatcherListener() {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_is_show:
                if (flag) {
                    flag = false;
                    ivIsShow.setImageResource(R.mipmap.ic_password_pre);
                    etPw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    flag = true;
                    ivIsShow.setImageResource(R.mipmap.ic_password_nor);
                    etPw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                etPw.setSelection(etPw.length());
                break;
            case R.id.tv_login:
                if (iLoginView != null) {
                    iLoginView.onPhoneLogin(etPhone.getText().toString(), etPw.getText().toString(), cb.isChecked());
                }
                break;
            case R.id.lly_login:
                if (iLoginView != null) {
                    iLoginView.openLoginPage(cb.isChecked());
                }
                break;

            case R.id.ll_register:
            case R.id.tv_register_new:
                if (iLoginView != null) {
                    iLoginView.openRegisterPage(cb.isChecked());
                }
                break;

            case R.id.tv_forget_pw:
                if (iLoginView != null) {
                    iLoginView.openResetPwPage(cb.isChecked());
                }
                break;
            case R.id.tv_wechat_login:
                if (iLoginView != null) {
                    iLoginView.onWechatLogin(cb.isChecked());
                }
                break;
            case R.id.tv_qq_login:
                if (iLoginView != null) {
                    iLoginView.onQQLogin(cb.isChecked());
                }
                break;
            case R.id.tv_user_protocol:
                if (iLoginView != null) {
                    iLoginView.openProtocolPage();
                }
                break;
            case R.id.tv_self_protocol:
                if (iLoginView != null) {
                    iLoginView.openSelfProtocolPage();
                }
                break;
            case R.id.tv_live_protocol:
                if (iLoginView != null) {
                    iLoginView.openLiveProtocolPage();
                }
                break;
        }
    }
}
