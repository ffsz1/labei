package com.vslk.lbgx.ui.me.setting.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vslk.lbgx.base.activity.BaseActivity;
import com.vslk.lbgx.ui.me.wallet.activity.BinderPhoneActivity;
import com.vslk.lbgx.ui.me.withdraw.BinderQQActivity;
import com.vslk.lbgx.ui.me.withdraw.NewBinderWeixinPayActivity;
import com.hncxco.library_ui.widget.AppToolBar;
import com.orhanobut.logger.Logger;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.IUserDbCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tongdaxing.xchat_core.Constants.QQ_PLATFORM_CODE;
import static com.tongdaxing.xchat_core.Constants.WX_PLATFORM_CODE;

/**
 * Function: 账号绑定页
 * Author: Edward on 2019/4/11
 */
public class AccountBindingActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.rl_wecaht_binding)
    RelativeLayout rlWechatBinding;
    @BindView(R.id.rl_qq_binding)
    RelativeLayout rlQQBinding;
    @BindView(R.id.et_nick_name)
    TextView etNickName;
    @BindView(R.id.et_phone_number)
    TextView etPhoneNumber;
    @BindView(R.id.iv_change_binding)
    ImageView ivChangeBinding;
    @BindView(R.id.tv_qq_binding_status)
    TextView tvQQBindingStatus;
    @BindView(R.id.tv_wechat_binding_status)
    TextView tvWechatBindingStatus;
    @BindView(R.id.app_tool_bar)
    AppToolBar appToolBar;

    private final int BINDING_ACCOUNT_REQUEST_CODE = 10001;
    private final int UNBINDING_ACCOUNT_REQUEST_CODE = 10002;
    private final int CHANGE_PHONE_REQUEST_CODE = 10003;
    private UserInfo userInfo;
    private final String TAG = "AccountBindingActivity";

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, AccountBindingActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_binding);
        ButterKnife.bind(this);
        appToolBar.setOnBackBtnListener(v -> finish());
        rlQQBinding.setOnClickListener(this);
        rlWechatBinding.setOnClickListener(this);
        ivChangeBinding.setOnClickListener(this);
        initData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UNBINDING_ACCOUNT_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {//解绑成功回调
            int type = data.getIntExtra("type", 0);
            Logger.i(TAG, "解绑类型:" + type);
            if (type == WX_PLATFORM_CODE) {
                CoreManager.getCore(IUserDbCore.class).updateHasWx(userInfo, false);
            } else if (type == QQ_PLATFORM_CODE) {
                CoreManager.getCore(IUserDbCore.class).updateHasQq(userInfo, false);
            }
            initData();
        } else if (requestCode == BINDING_ACCOUNT_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {//绑定成功回调
            int type = data.getIntExtra("type", 0);
            Logger.i(TAG, "绑定类型:" + type);
            if (type == WX_PLATFORM_CODE) {
                CoreManager.getCore(IUserDbCore.class).updateHasWx(userInfo, true);
            } else if (type == QQ_PLATFORM_CODE) {
                CoreManager.getCore(IUserDbCore.class).updateHasQq(userInfo, true);
            }
            initData();
        } else if (requestCode == CHANGE_PHONE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            UserInfo userInfo = CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo();
            if (userInfo != null && !TextUtils.isEmpty(userInfo.getPhone())) {
                Logger.i(TAG, "换绑手机号码:" + userInfo.getPhone());
                etPhoneNumber.setText(userInfo.getPhone());
            }
        }
    }

    private void initData() {
        userInfo = CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo();
        if (userInfo != null) {
            Logger.e(TAG, userInfo.toString());
            etNickName.setText(userInfo.getNick());
            etPhoneNumber.setText(userInfo.getPhone());
            tvQQBindingStatus.setText(userInfo.isHasQq() ? "解绑" : "去绑定");
            tvWechatBindingStatus.setText(userInfo.isHasWx() ? "解绑" : "去绑定");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_change_binding:
                Intent intent = new Intent(this, BinderPhoneActivity.class);
                intent.putExtra(BinderPhoneActivity.hasBand, BinderPhoneActivity.modifyBand);
                startActivityForResult(intent, CHANGE_PHONE_REQUEST_CODE);
                break;
            case R.id.rl_qq_binding:
                if (userInfo.isHasQq()) {
                    UnbindingQQActivity.startForResult(this, UNBINDING_ACCOUNT_REQUEST_CODE);
                } else {
                    BinderQQActivity.startForResult(this, BINDING_ACCOUNT_REQUEST_CODE);
                }
                break;
            case R.id.rl_wecaht_binding:
                if (userInfo.isHasWx()) {
                    UnbindingWechatActivity.startForResult(this, UNBINDING_ACCOUNT_REQUEST_CODE);
                } else {
                    NewBinderWeixinPayActivity.startForResult(this, BINDING_ACCOUNT_REQUEST_CODE);
                }
                break;
        }
    }
}