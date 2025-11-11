package com.vslk.lbgx.ui.me.setting.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.tongdaxing.xchat_core.WebUrl;
import com.vslk.lbgx.base.activity.BaseActivity;
import com.vslk.lbgx.ui.common.widget.dialog.DialogManager;
import com.vslk.lbgx.ui.me.wallet.activity.BinderPhoneActivity;
import com.vslk.lbgx.ui.me.wallet.activity.SetPasswordActivity;
import com.vslk.lbgx.ui.me.wallet.activity.WalletDetailsActivity;
import com.vslk.lbgx.ui.web.CommonWebViewActivity;
import com.vslk.lbgx.utils.UIHelper;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.databinding.ActivitySettingBinding;
import com.tongdaxing.xchat_core.PreferencesUtils;
import com.tongdaxing.xchat_core.auth.IAuthClient;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.user.VersionsCore;
import com.tongdaxing.xchat_core.user.VersionsCoreClient;
import com.tongdaxing.xchat_core.user.bean.CheckUpdataBean;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.data.BaseConstants;
import com.tongdaxing.xchat_framework.util.ApkUtils;
import com.tongdaxing.xchat_framework.util.config.BasicConfig;

/**
 * Created by zhouxiangfeng on 2017/4/16.
 */
public class SettingActivity extends BaseActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    private ActivitySettingBinding settingBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingBinding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        settingBinding.setClick(this);
        initView();
        initData();
    }

    private void initData() {
        settingBinding.versions.setText("内测版本V" + BasicConfig.getLocalVersionName(getApplicationContext()));
    }

    /**
     * 判断是绑定手机还是设置手机密码
     */
    private boolean isBindPhone;

    /**
     * 查询是否已经绑定手机
     *
     * @param isBindPhone true 绑定手机 false
     */
    private void queryBindPhone(boolean isBindPhone) {
        this.isBindPhone = isBindPhone;
        getDialogManager().showProgressDialog(SettingActivity.this, "正在查询请稍后...");
        CoreManager.getCore(IAuthCore.class).isPhone(CoreManager.getCore(IAuthCore.class).getCurrentUid());
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            //设置登录密码
            case R.id.rly_bind_phone:
                queryBindPhone(false);
                break;
            case R.id.rly_binder://绑定手机
                queryBindPhone(true);
                break;
            case R.id.tv_feedback:
                startActivity(new Intent(this, FeedbackActivity.class));
                break;
//            case R.id.rly_help:
//                UIHelper.showUsinghelp(this);
//                break;
            case R.id.rly_update://关于我们
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                break;
            case R.id.rly_check://检查更新
                CoreManager.getCore(VersionsCore.class).checkVersion();
                break;
            case R.id.rly_lab:
                startActivity(new Intent(getApplicationContext(), LabActivity.class));
                break;
            case R.id.me_item_about:
                UIHelper.openContactUs(this);
                break;
            case R.id.tv_blacklist:
                //黑名单
                WalletDetailsActivity.start(this, 1);
                break;
            case R.id.tv_system_right:
                //系统权限
                gotoAppDetailIntent(this);
                break;
            case R.id.tv_delete_num:
                //注销账号
//                deleteNum();
                CommonWebViewActivity.start(this, WebUrl.DELE_NUM);
                break;
            case R.id.btn_login_out://退出登录
                CoreManager.getCore(IAuthCore.class).logout();
                PreferencesUtils.setFristQQ(true);
                finish();
                break;
            default:
                break;
        }
    }

    private void deleteNum() {
        getDialogManager().showOkCancelDialog("注销账号请联系微信客服\n"+getString(R.string.txt_kefu_num), "复制","取消", new DialogManager.AbsOkDialogListener() {
            @Override
            public void onOk() {
                ClipboardManager mClipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("复制成功!", getString(R.string.txt_kefu_num));
                mClipboardManager.setPrimaryClip(clipData);
                toast("复制成功!");
            }
        });
    }

    /**
     * 跳转到应用详情界面
     */
    public static void gotoAppDetailIntent(Activity activity) {
        Intent intent = new Intent();
        intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        activity.startActivity(intent);
    }

    private void isShowLab() {
        if (ApkUtils.isApkDebugable(this)) {
            settingBinding.rlyLab.setVisibility(View.VISIBLE);
        } else {
            settingBinding.rlyLab.setVisibility(View.GONE);
        }
    }

    private void initView() {
        isShowLab();
        settingBinding.toolbar.setOnBackBtnListener(view -> finish());
    }

    @CoreEvent(coreClientClass = IAuthClient.class)
    public void onIsPhone() {
        if (isBindPhone) {//点击账号管理
            getDialogManager().dismissDialog();
            Intent intent = new Intent(this, BinderPhoneActivity.class);
            intent.putExtra(BinderPhoneActivity.hasBand, BinderPhoneActivity.modifyBand);
            startActivity(intent);
        } else {//点击设置登录密码
            CoreManager.getCore(IAuthCore.class).checkSetPwd();
        }
    }

    @CoreEvent(coreClientClass = IAuthClient.class)
    public void onSetPassWordFail(String msg) {
        getDialogManager().dismissDialog();
        toast(msg);
    }

    @CoreEvent(coreClientClass = IAuthClient.class)
    public void onSetPassWord(boolean isSetPassWord) {
        getDialogManager().dismissDialog();
        if (isSetPassWord) {
            SetPasswordActivity.start(this, true);
        } else {
            SetPasswordActivity.start(this, false);
        }
    }

    @CoreEvent(coreClientClass = IAuthClient.class)
    public void onIsphoneFail(String error) {
        getDialogManager().dismissDialog();
        Intent intent = new Intent(this, BinderPhoneActivity.class);
        startActivity(intent);
    }

    @CoreEvent(coreClientClass = VersionsCoreClient.class)
    public void onVersionUpdataDialog(CheckUpdataBean checkUpdataBean) {

        if (checkUpdataBean == null) {
            return;
        }

        if (checkUpdataBean.getStatus() == 1) {
            toast("已经是最新版本");
            settingBinding.rlyCheck.setEnabled(false);
            return;
        }

        getDialogManager().showOkCancelDialog("检测到有最新的版本,是否更新", true, new DialogManager.AbsOkDialogListener() {
            @Override
            public void onOk() {
                String download_url = checkUpdataBean.getDownloadUrl();
                if (TextUtils.isEmpty(download_url)) {
                    download_url = BaseConstants.NEWEST_APK_FILE_URL;
                }
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(download_url));
                startActivity(intent);
            }
        });
    }
}
