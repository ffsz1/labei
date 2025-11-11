package com.vslk.lbgx.ui.me.withdraw;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.vslk.lbgx.ui.login.CodeDownTimer;
import com.vslk.lbgx.ui.me.withdraw.presenter.WithdrawPresenter;
import com.vslk.lbgx.ui.me.withdraw.view.IWithdrawView;
import com.hncxco.library_ui.widget.AppToolBar;
import com.hncxco.library_ui.widget.DrawableTextView;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_core.withdraw.bean.RefreshInfo;
import com.tongdaxing.xchat_core.withdraw.bean.WithdrawInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.config.BasicConfig;
import com.tongdaxing.xchat_framework.util.util.StringUtils;

import org.greenrobot.eventbus.EventBus;

import java.text.NumberFormat;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * 文件描述：绑定微信提现页面
 *
 * @auther：zwk
 * @data：2019/2/15
 */
@CreatePresenter(WithdrawPresenter.class)
public class BinderWeixinPayActivity extends BaseMvpActivity<IWithdrawView, WithdrawPresenter> implements IWithdrawView, View.OnClickListener {

    private AppToolBar mToolBar;
    private EditText etCode, etPhone, etRealName;
    private TextView tvCode;
    private DrawableTextView btnCommit;
    private Platform wechat;
    private WithdrawInfo mWithdrawInfo;

    private CodeDownTimer timer;

    public static void start(Context context, WithdrawInfo withdrawInfo) {
        Intent intent = new Intent(context, BinderWeixinPayActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("withdrawInfo", withdrawInfo);
        intent.putExtras(mBundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder_weixin_pay);
        initView();
        initData();
        initClickListener();
    }

    private void initView() {
        etPhone = (EditText) findViewById(R.id.et_phone_number);
        etRealName = (EditText) findViewById(R.id.et_realName);
        tvCode = (TextView) findViewById(R.id.btn_get_code);
        mToolBar = (AppToolBar) findViewById(R.id.toolbar);
        btnCommit = (DrawableTextView) findViewById(R.id.btn_binder);
        etCode = (EditText) findViewById(R.id.et_smscode);
    }

    private void initData() {
        mWithdrawInfo = (WithdrawInfo) getIntent().getSerializableExtra("withdrawInfo");
        /*if (mWithdrawInfo != null) {
            etRealName.setText(mWithdrawInfo.getWxRealName());
            etPhone.setText(mWithdrawInfo.getWxPhone());
        }*/
    }

    private void initClickListener() {
        btnCommit.setOnClickListener(this);
        tvCode.setOnClickListener(this);
        back(mToolBar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_get_code:
                String phoneNumber = etPhone.getText().toString().trim();
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
                UserInfo userInfo = CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo();
                // 验证手机
                if (userInfo == null || TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(userInfo.getPhone()) || !userInfo.getPhone().equals(phoneNumber)) {
                    toast("未绑定手机或当前号码与绑定的手机号码不一致，请检查重试！");
                    return;
                }
                getMvpPresenter().getInviteCode();
                break;
            case R.id.btn_binder:
                String phone = etPhone.getText().toString().trim();
                String code = etCode.getText().toString().trim();
                if (StringUtil.isEmpty(phone)) {
                    toast("手机号码不能为空哦！");
                    return;
                }
                if (StringUtil.isEmpty(code)) {
                    toast("验证码不能为空哦！");
                    return;
                }
                getMvpPresenter().checkCode(code);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRemindToastError(String error) {
        toast(error);
    }

    @Override
    public void onRemindToastSuc() {
        timer = new CodeDownTimer(tvCode, 60000, 1000);
        timer.start();
    }

    @Override
    public void onCheckCodeFailToast(String error) {
        getDialogManager().dismissDialog();
        toast(error);
    }

    @Override
    public void onCheckCodeSucWeixinLogin() {
        String code = etCode.getText().toString().trim();
        String realName = etRealName.getText().toString().trim();
        String phoneNumber = etPhone.getText().toString().trim();
        if (StringUtils.isEmpty(phoneNumber) || StringUtils.isEmpty(code) || StringUtils.isEmpty(realName)) {
            if (getDialogManager() != null) {
                getDialogManager().dismissDialog();
            }
            toast("手机号、验证码或真实姓名未填写完整哦");
            return;
        }
        wechat = ShareSDK.getPlatform(Wechat.NAME);
        if (!wechat.isClientValid()) {
            toast("未安装微信");
            return;
        }
        if (wechat.isAuthValid()) {
            wechat.removeAccount(true);
        }
        getDialogManager().showProgressDialog(this, "加载中...");

        wechat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(final Platform platform, int i, HashMap<String, Object> hashMap) {
                if (i == Platform.ACTION_USER_INFOR) {
                    String openid = platform.getDb().getUserId();
                    String unionid = platform.getDb().get("unionid");
                    String accessToken = platform.getDb().getToken();
                    String nickName = platform.getDb().getUserName();
                    getMvpPresenter().bindWithdrawWeixin(accessToken, openid, unionid, phoneNumber, code, nickName, realName);
                } else {
                    if (getDialogManager() != null) {
                        getDialogManager().dismissDialog();
                    }
                    toast("微信登录失败，错误码：" + i);
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                if (getDialogManager() != null) {
                    getDialogManager().dismissDialog();
                }
                toast("微信授权错误");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                if (getDialogManager() != null) {
                    getDialogManager().dismissDialog();
                }
                toast("取消微信授权");
            }
        });
        wechat.SSOSetting(false);
        wechat.showUser(null);
    }

    @Override
    public void onRemindBindWeixinSucFail(String error) {
        getDialogManager().dismissDialog();
        toast(error);
    }

    @Override
    public void onRemindBindWeixinSucToast(int type) {
        getDialogManager().dismissDialog();
        toast("绑定成功");
        EventBus.getDefault().post(new RefreshInfo());
        finish();
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
