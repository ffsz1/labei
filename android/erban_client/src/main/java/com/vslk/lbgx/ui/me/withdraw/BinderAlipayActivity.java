package com.vslk.lbgx.ui.me.withdraw;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.vslk.lbgx.base.activity.BaseActivity;
import com.vslk.lbgx.ui.login.CodeDownTimer;
import com.hncxco.library_ui.widget.AppToolBar;
import com.hncxco.library_ui.widget.DrawableTextView;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.redpacket.bean.RedPacketInfo;
import com.tongdaxing.xchat_core.withdraw.IWithdrawCore;
import com.tongdaxing.xchat_core.withdraw.IWithdrawCoreClient;
import com.tongdaxing.xchat_core.withdraw.bean.RefreshInfo;
import com.tongdaxing.xchat_core.withdraw.bean.WithdrawInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import org.greenrobot.eventbus.EventBus;

public class BinderAlipayActivity extends BaseActivity {

    private AppToolBar mToolBar;
    private EditText etAlipayAccount;
    private EditText etAlipayName;
    private EditText etSmsCode;
    private DrawableTextView btnGetCode;
    private DrawableTextView btnBinder;
    private DrawableTextView btnBinderRquest;

    private CodeDownTimer timer;

    private TextWatcher textWatcher;

    public static void start(Context context) {
        Intent intent = new Intent(context, BinderAlipayActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder_alipay);
        initView();
        initData();
        onSetListener();
    }

    private void onSetListener() {
        //获取绑定支付宝验证码
        btnGetCode.setOnClickListener(v -> {
            timer = new CodeDownTimer(btnGetCode, 60000, 1000);
            timer.start();
            CoreManager.getCore(IWithdrawCore.class).getSmsCode(CoreManager.getCore(IAuthCore.class).getCurrentUid());
        });

        //请求绑定支付宝
        btnBinderRquest.setOnClickListener(v -> CoreManager.getCore(IWithdrawCore.class).binderAlipay(etAlipayAccount.getText().toString(), etAlipayName.getText().toString(), etSmsCode.getText().toString()));
        //输入框监听改变
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etAlipayAccount.getText() != null && etAlipayAccount.getText().length() > 0
                        && etAlipayName.getText() != null && etAlipayName.getText().length() > 0
                        && etSmsCode.getText() != null && etSmsCode.getText().length() > 0) {
                    btnBinder.setVisibility(View.GONE);
                    btnBinderRquest.setVisibility(View.VISIBLE);
                } else {
                    btnBinder.setVisibility(View.VISIBLE);
                    btnBinderRquest.setVisibility(View.GONE);
                }
            }
        };

        etAlipayAccount.addTextChangedListener(textWatcher);
        etAlipayName.addTextChangedListener(textWatcher);
        etSmsCode.addTextChangedListener(textWatcher);
        mToolBar.setOnBackBtnListener(view -> finish());
    }

    @CoreEvent(coreClientClass = IWithdrawCoreClient.class)
    public void onGetSmsCodeFail(String error) {
        toast(error);
    }

    @CoreEvent(coreClientClass = IWithdrawCoreClient.class)
    public void onBinderAlipay() {
        toast("绑定成功");
        EventBus.getDefault().post(new RefreshInfo());
        finish();
    }

    @CoreEvent(coreClientClass = IWithdrawCoreClient.class)
    public void onBinderAlipayFail(String error) {
        toast(error);
    }

    private void initData() {
        WithdrawInfo info = (WithdrawInfo) getIntent().getSerializableExtra("withdrawInfo");
        RedPacketInfo redPacketInfos = (RedPacketInfo) getIntent().getSerializableExtra("redPacketInfo");
        try {
            if (info != null && !info.isNotBoundPhone) {
//                etAlipayAccount.setText(info.alipayAccount);
//                etAlipayName.setText(info.alipayAccountName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        etAlipayAccount = (EditText) findViewById(R.id.et_phone);
        etAlipayName = (EditText) findViewById(R.id.et_name);
        etSmsCode = (EditText) findViewById(R.id.et_smscode);
        etSmsCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
        btnGetCode = (DrawableTextView) findViewById(R.id.btn_get_code);
        btnBinder = (DrawableTextView) findViewById(R.id.btn_binder);
        btnBinderRquest = (DrawableTextView) findViewById(R.id.btn_binder_request);
        mToolBar = (AppToolBar) findViewById(R.id.toolbar);

    }

    @Override
    protected void onDestroy() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        super.onDestroy();
    }
}
