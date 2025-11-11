package com.vslk.lbgx.ui.me.wallet.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.vslk.lbgx.ui.login.CodeDownTimer;
import com.vslk.lbgx.ui.me.MePresenter;
import com.vslk.lbgx.ui.me.task.view.IMeView;
import com.vslk.lbgx.ui.widget.TextWatcherListener;
import com.hncxco.library_ui.widget.AppToolBar;
import com.hncxco.library_ui.widget.DrawableTextView;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.xchat_core.PreferencesUtils;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.IUserDbCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.config.BasicConfig;

import java.text.NumberFormat;

/**
 * @author dell
 */
@CreatePresenter(MePresenter.class)
public class BinderPhoneActivity extends BaseMvpActivity<IMeView, MePresenter> implements IMeView {

    private EditText etAlipayAccount;
    private EditText etSmsCode;
    private DrawableTextView btnGetCode;
    private Button btnBinder;
    private Button btnBinderRquest;
    private TextWatcherListener textWatcher;

    public static final String hasBand = "hasBand";
    public static final String validatePhone = "validatePhone";
    public static final String isSetPassword = "isSetPassword";
    public static final int modifyBand = 1;
    private boolean isModifyBand = false;
    //    private TextInputLayout mTilPhone;
    private int mHasBandType;
    private boolean mIsValidatePhone;
    private boolean mIsSetPassword;
    private CodeDownTimer mTimer;
    private String mSubmitPhone;
    private AppToolBar mAppToolBar;

    private static final String BAN_BACK = "banBack";
    private String phoneNumber;

    public static void start(Context context) {
        start(context, false);
    }

    /**
     * @param context
     * @param isBanBack 禁止后退键返回
     */
    public static void start(Context context, boolean isBanBack) {
        Intent intent = new Intent(context, BinderPhoneActivity.class);
        intent.putExtra(BAN_BACK, isBanBack);
        context.startActivity(intent);
    }

    private boolean registerBack;

    /**
     * 设置编辑状态
     */
    private void setEditableStatus(boolean flag) {
        etAlipayAccount.setFocusable(!flag);
        etAlipayAccount.setFocusableInTouchMode(!flag);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder_phone);
        initView();
        setListener();
        registerBack = getIntent().getBooleanExtra(BAN_BACK, false);
        mHasBandType = getIntent().getIntExtra(hasBand, 0);
        mIsSetPassword = getIntent().getBooleanExtra(isSetPassword, false);
        mIsValidatePhone = getIntent().getBooleanExtra(validatePhone, false);
        if (mHasBandType == modifyBand) {
            isModifyBand = true;
            setEditableStatus(true);
            UserInfo userInfo = CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo();
            if (userInfo != null) {
                etAlipayAccount.setText(userInfo.getPhone());
            }
            setMode(isModifyBand);
        } else {
            btnBinder.setText(isModifyBand ? "确认替换" : mIsValidatePhone ? "立即验证" : "确认绑定");
            btnBinderRquest.setText(isModifyBand ? "确认替换" : mIsValidatePhone ? "立即验证" : "确认绑定");
        }
        mAppToolBar.setTitle(isModifyBand ? "更换绑定手机号码" : mIsValidatePhone ? "验证手机号码" : "绑定手机号码");
        mAppToolBar.setLeftImageVisibility(isModifyBand ? View.VISIBLE : View.GONE);
        if (registerBack) {
            mAppToolBar.setLeftImageVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && registerBack) {//用户通过第三方注册，在绑定手机号店面点击返回
            CoreManager.getCore(IAuthCore.class).logout();//退出此账号，回到登陆页面
            PreferencesUtils.setFristQQ(true);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initView() {
        mAppToolBar = (AppToolBar) findViewById(R.id.toolbar);
//        mTilPhone = (TextInputLayout) findViewById(R.id.til_phone);
        etAlipayAccount = (EditText) findViewById(R.id.et_phone);
        etSmsCode = (EditText) findViewById(R.id.et_smscode);
        btnGetCode = (DrawableTextView) findViewById(R.id.btn_get_code);
        btnBinder = (Button) findViewById(R.id.btn_binder);
        btnBinderRquest = (Button) findViewById(R.id.btn_binder_request);
    }

    private void setMode(boolean isModifyBand) {
        mAppToolBar.setTitle(isModifyBand ? "更换绑定手机号码" : "绑定手机号码");
        btnBinder.setText(isModifyBand ? "确认替换" : "确认绑定");
    }

    private void setListener() {
        //获取绑定手机验证码
        btnGetCode.setOnClickListener(v -> {
            phoneNumber = etAlipayAccount.getText().toString().trim();
            Number parse = null;
            try {
                parse = NumberFormat.getIntegerInstance().parse(phoneNumber);
            } catch (Exception e) {
                Log.e("bind phone", e.getMessage(), e);
            }
            if (parse == null || parse.intValue() == 0 || phoneNumber.length() != 11) {
                Toast.makeText(BasicConfig.INSTANCE.getAppContext(), "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                return;
            }
            if (mHasBandType == modifyBand) {
                if (isModifyBand) {
                    //确认久手机type=3
                    getMvpPresenter().getModifyPhoneSMSCode(phoneNumber, "3");
                } else {
                    //确认久手机type=2
                    getMvpPresenter().getModifyPhoneSMSCode(phoneNumber, "2");
                }
            } else {
                if (mIsValidatePhone) {
                    //验证手机号
                    getMvpPresenter().getPwSmsCode(phoneNumber);
                    Log.e(TAG, "setListener: " + phoneNumber);
                } else {
                    //绑定手机
                    getMvpPresenter().getSMSCode(phoneNumber);
                }
            }
        });

        //请求绑定手机号码
        btnBinderRquest.setOnClickListener(v -> {
            getDialogManager().showProgressDialog(BinderPhoneActivity.this, "正在验证请稍后...");
            String phone = etAlipayAccount.getText().toString();
            String smsCode = etSmsCode.getText().toString();
            if (mHasBandType == modifyBand) {
                if (isModifyBand) {
                    getMvpPresenter().modifyBinderPhone(phone, smsCode, UriProvider.modifyBinderPhone());
                } else {
                    mSubmitPhone = phone;
                    getMvpPresenter().modifyBinderPhone(phone, smsCode, UriProvider.modifyBinderNewPhone());
                }
            } else {
                if (mIsValidatePhone) {
                    //验证手机号
                    getMvpPresenter().verifierPhone(phone, smsCode);
                } else {
                    //绑定手机
                    getMvpPresenter().binderPhone(phone, smsCode);
                }
            }
        });
        //输入框监听改变
        textWatcher = new TextWatcherListener() {

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(etAlipayAccount.getText()) && !TextUtils.isEmpty(etSmsCode.getText())) {
                    btnBinder.setVisibility(View.GONE);
                    btnBinderRquest.setVisibility(View.VISIBLE);
                } else {
                    btnBinder.setVisibility(View.VISIBLE);
                    btnBinderRquest.setVisibility(View.GONE);
                }
            }
        };

        etAlipayAccount.addTextChangedListener(textWatcher);
        etSmsCode.addTextChangedListener(textWatcher);

        mAppToolBar.setOnBackBtnListener(view -> {
            if (registerBack) {
                CoreManager.getCore(IAuthCore.class).logout();//退出此账号，回到登陆页面
                PreferencesUtils.setFristQQ(true);
            }
            finish();
        });
    }

    @Override
    public void verifierPhone() {
        getDialogManager().dismissDialog();
        SetPasswordActivity.start(this, false);
        toast("手机验证成功");
        finish();

    }

    @Override
    public void verifierPhoneFail(String msg) {
        getDialogManager().dismissDialog();
        toast(msg);
    }

    @Override
    public void onBinderPhone() {
//        toast("手机绑定成功");
        //是否是设置登录密码
        if (mIsSetPassword) {
            SetPasswordActivity.start(this, false);
        }
        getDialogManager().dismissDialog();

        UserInfo userInfo = CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo();

        if (userInfo != null) {
            CoreManager.getCore(IUserDbCore.class).updatePhone(userInfo, phoneNumber);

        } else {
            toast("数据错误,请重新登录!");
        }

        finish();
    }

    @Override
    public void onBinderPhoneFail(String error) {
        getDialogManager().dismissDialog();
        toast(error);
    }

    @Override
    public void onModifyOnBinner() {
        getDialogManager().dismissDialog();
        setResult(Activity.RESULT_OK);
        if (isModifyBand) {
            isModifyBand = false;
            setEditableStatus(false);
            setMode(false);
            etSmsCode.setText("");
            etAlipayAccount.setText("");
            etAlipayAccount.setFocusable(true);
            if (mTimer != null) {
                mTimer.cancel();
                mTimer.onFinish();
            }
        } else {
            UserInfo userInfo = CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo();
            if (userInfo != null) {
                userInfo.setPhone(mSubmitPhone);
            }
            toast("绑定成功");
            finish();
        }
    }

    @Override
    public void onMoidfyOnBinnerFail(String error) {
        getDialogManager().dismissDialog();
        toast(error);
    }

    @Override
    public void getModifyPhoneSMSCodeSuccess() {
        mTimer = new CodeDownTimer(btnGetCode, 60000, 1000);
        mTimer.start();
    }

    @Override
    public void getModifyPhoneSMSCodeFail(String msg) {
        toast(msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }
}
