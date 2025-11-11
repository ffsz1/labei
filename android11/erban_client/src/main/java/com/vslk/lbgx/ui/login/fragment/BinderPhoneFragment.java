//package com.hule.xxm.ui.login.fragment;
//
//import android.text.Editable;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.hule.xxm.base.fragment.BaseMvpFragment;
//import com.tongdaxing.xchat_core.login.presenter.BinderPhonePresenter;
//import com.tongdaxing.xchat_core.login.view.IBinderPhoneView;
//import com.hule.xxm.ui.widget.NewCodeDownTimer;
//import com.hule.xxm.ui.widget.TextWatcherListener;
//import com.tongdaxing.erban.R;
//import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
//
//import butterknife.BindView;
//
///**
// * Function:
// * Author: Edward on 2019/6/5
// */
//@CreatePresenter(BinderPhonePresenter.class)
//public class BinderPhoneFragment extends BaseMvpFragment<IBinderPhoneView, BinderPhonePresenter> implements IBinderPhoneView, View.OnClickListener {
//    public static final String TAG = "BinderPhoneFragment";
//    @BindView(R.id.et_phone)
//    EditText etPhone;
//    @BindView(R.id.et_auth_code)
//    EditText etAuthCode;
//    @BindView(R.id.tv_auth_code)
//    TextView tvGetAuthCode;
//    @BindView(R.id.tv_next)
//    TextView tvNext;
//
//    @Override
//    public int getRootLayoutId() {
//        return R.layout.fragment_binder_phone;
//    }
//
//    @Override
//    public void onFindViews() {
//        tvNext.setOnClickListener(this);
//        tvGetAuthCode.setOnClickListener(this);
//    }
//
//    @Override
//    public void initiate() {
//    }
//
//    private boolean checkEnableLogin() {
//        return (!TextUtils.isEmpty(etPhone.getText().toString()) && !TextUtils.isEmpty(etAuthCode.getText().toString()));
//    }
//
//    @Override
//    public void onSetListener() {
//        etPhone.addTextChangedListener(new TextWatcherListener() {
//            @Override
//            public void afterTextChanged(Editable editable) {
//                tvNext.setEnabled(checkEnableLogin());
//            }
//        });
//
//        etAuthCode.addTextChangedListener(new TextWatcherListener() {
//            @Override
//            public void afterTextChanged(Editable editable) {
//                tvNext.setEnabled(checkEnableLogin());
//            }
//        });
//    }
//
//    @Override
//    public void onClick(View v) {
//        String phone = etPhone.getText().toString();
//        String smsCode = etAuthCode.getText().toString();
//        switch (v.getId()) {
//            case R.id.tv_auth_code:
//                getMvpPresenter().getSMSCode(phone);
//                break;
//            case R.id.tv_next:
//                getDialogManager().showProgressDialog(getActivity(), "正在验证请稍后...");
//                if (check(phone, smsCode)) {
//                    getMvpPresenter().binderPhone(phone, smsCode);
//                }
//                break;
//        }
//    }
//
//    private boolean check(String phone, String smsCode) {
//        if (TextUtils.isEmpty(phone)) {
//            toast("手机不能为空!");
//            return false;
//        }
//
//        if (TextUtils.isEmpty(smsCode)) {
//            toast("验证码不能为空!");
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public void onBinderPhone() {
//        getDialogManager().dismissDialog();
//        getActivity().finish();
//    }
//
//    @Override
//    public void onBinderPhoneFail(String error) {
//        getDialogManager().dismissDialog();
//        toast(error);
//    }
//
//    private NewCodeDownTimer mTimer;
//
//    @Override
//    public void getModifyPhoneSMSCodeSuccess() {
//        mTimer = new NewCodeDownTimer(tvGetAuthCode, 60000, 1000);
//        mTimer.start();
//    }
//
//    @Override
//    public void getModifyPhoneSMSCodeFail(String msg) {
//        toast(msg);
//    }
//}
