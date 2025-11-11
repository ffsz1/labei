package com.vslk.lbgx.ui.find.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.vslk.lbgx.presenter.invite.IInviteFriendView;
import com.vslk.lbgx.presenter.invite.InviteFriendPresenter;
import com.vslk.lbgx.room.widget.dialog.InviteCodeDialog;
import com.vslk.lbgx.ui.me.wallet.activity.BinderPhoneActivity;
import com.vslk.lbgx.ui.me.withdraw.RedPacketWithdrawActivity;
import com.vslk.lbgx.ui.web.CommonWebViewActivity;
import com.vslk.lbgx.ui.widget.dialog.ShareDialog;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.databinding.FragmentInviteAwardBinding;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.xchat_core.WebUrl;
import com.tongdaxing.xchat_core.auth.IAuthClient;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.redpacket.IRedPacketCoreClient;
import com.tongdaxing.xchat_core.redpacket.bean.RedPacketInfo;
import com.tongdaxing.xchat_core.redpacket.bean.WebViewInfo;
import com.tongdaxing.xchat_core.redpacket.bean.WithdrawRedSucceedInfo;
import com.tongdaxing.xchat_core.share.IShareCore;
import com.tongdaxing.xchat_core.user.IUserClient;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.util.StringUtils;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * @author dell
 */
@CreatePresenter(InviteFriendPresenter.class)
public class InviteAwardActivity extends BaseMvpActivity<IInviteFriendView, InviteFriendPresenter> implements
        IInviteFriendView, View.OnClickListener, ShareDialog.OnShareDialogItemClick,
        InviteCodeDialog.OnInviteCodeSaveListener {

    private UserInfo userInfo;
    private ClipboardManager mClipboardManager;
    private FragmentInviteAwardBinding redbagBinding;

    public static void start(Context context) {
        Intent intent = new Intent(context, InviteAwardActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void checkSucceed() {
        CoreManager.getCore(IAuthCore.class).isPhone(CoreManager.getCore(IAuthCore.class).getCurrentUid());
    }

    @Override
    public void checkFailure(String errorStr) {
        toast(errorStr);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        redbagBinding = DataBindingUtil.setContentView(this, R.layout.fragment_invite_award);
        initView();
        initData();
    }

    private void initView() {
        redbagBinding.setClick(this);
        redbagBinding.toolbar.setOnBackBtnListener(view -> finish());
        mClipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
    }

    private void initData() {
        getLocalWxInfo();
        getMvpPresenter().getRedPacket();
        userInfo = CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo();
        if (userInfo != null) {
            redbagBinding.erbanNo.setText(String.valueOf(userInfo.getErbanNo()));
            showInviteCode();
        }
    }

    @Override
    public void onGetRedPacketSuccessView(RedPacketInfo info) {
        if (info != null) {
            redbagBinding.setRedPacketInfo(info);
        }
    }

    @Override
    public void onGetRedPacketFailView(String msg) {
        toast(msg);
    }

    @Override
    public void onSaveInviteCodeSuccessView(String inviteCode) {
        toast("保存成功");
        if (getDialogManager() != null) {
            getDialogManager().dismissDialog();
        }

    }

    @Override
    public void onSaveInviteCodeFailView(String msg) {
        if (getDialogManager() != null) {
            getDialogManager().dismissDialog();
        }
        toast(msg);
    }

    @Override
    public void onRemindToastSuc() {
//        smcAwardVerificationDialog.countDown();
    }

    @Override
    public void onRemindToastError(String error) {
        toast(error);
    }

    private String openid, unionid, accessToken, nickName;

    private void getLocalWxInfo() {

        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        if (!wechat.isClientValid()) {
            toast("未安装微信");
            return;
        }
        if (wechat.isAuthValid()) {
            wechat.removeAccount(true);
        }
        getDialogManager().showProgressDialog(this, "获取参数中...");

        wechat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(final Platform platform, int i, HashMap<String, Object> hashMap) {
                if (getDialogManager() != null) {
                    getDialogManager().dismissDialog();
                }
                if (i == Platform.ACTION_USER_INFOR) {
                    openid = platform.getDb().getUserId();
                    unionid = platform.getDb().get("unionid");
                    accessToken = platform.getDb().getToken();
                    nickName = platform.getDb().getUserName();
                } else {
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

    //获取和验证短信验证码弹框
//    private void showSmcDialog() {
//        SMCAwardVerificationDialog smcAwardVerificationDialog = SMCAwardVerificationDialog.newInstance(getMvpPresenter());
//        smcAwardVerificationDialog.show(getSupportFragmentManager(), null);
//        smcAwardVerificationDialog.iOnSubmit = sms -> {
//            getMvpPresenter().getCheckCode(sms);
//        };
//    }

    @Override
    public void onClick(View view) {
        String url = null;
        switch (view.getId()) {
            case R.id.rly_people:
                url = WebUrl.MY_INVITE_PEOPLE + "?uid=" + CoreManager.getCore(IAuthCore.class).getCurrentUid();
                break;
            //邀请分成
            case R.id.rly_share_bonus:
                url = WebUrl.MY_INVITE_PERCENTAGE + "?uid=" + CoreManager.getCore(IAuthCore.class).getCurrentUid();
                break;
            case R.id.withdraw:
                if (userInfo != null) {

                    //必须绑定手机才能提现
                    //是否有openid
                    if (!TextUtils.isEmpty(openid) && !TextUtils.isEmpty(unionid) && !TextUtils.isEmpty(accessToken)) {
                        //如果只是QQ登录,则需要验证手机号

                        if (userInfo.isHasQq()) {
                            Log.e(TAG, "onClick: withdraw 1 " + userInfo.getPhone() + " " + userInfo.getErbanNo());

                            if (userInfo.getPhone().equals(String.valueOf(userInfo.getErbanNo()))) {
                                onIsphoneFail(null);

                            } else {
//                                showSmcDialog();
                                onIsPhone();
                            }

                            //如果只是微信登录
                        } else if (userInfo.isHasWx()) {

                            Log.e(TAG, "onClick: withdraw 2 " + userInfo.getPhone() + " " + userInfo.getErbanNo());

                            if (userInfo.getPhone().equals(String.valueOf(userInfo.getErbanNo()))) {

                                onIsphoneFail(null);

                            } else {
//                                showSmcDialog();
                                onIsPhone();
                            }

                            //如果只是手机登录
                        } else {
                            Log.e(TAG, "onClick: withdraw 3 " + userInfo.getPhone() + " " + userInfo.getErbanNo());

//                            showSmcDialog();

                            onIsPhone();

                        }
                    } else {
                        toast("获取参数失败，请重试!");
                        getLocalWxInfo();
                    }
                }
                break;
            case R.id.clipboard:
                saveClipboard();
                break;
            case R.id.invite:
                ShareDialog shareDialog = new ShareDialog(this);
                shareDialog.setOnShareDialogItemClick(this);
                shareDialog.show();
                break;
            case R.id.invite_msg:
                saveInviteCode();
                break;
            default:
                break;
        }
        if (TextUtils.isEmpty(url)) {
            return;
        }
        openWebView(url);
    }

    private void saveInviteCode() {
        InviteCodeDialog dialog = new InviteCodeDialog(this);
        dialog.setOnInviteCodeSaveListener(this);
        dialog.show();
    }

    @CoreEvent(coreClientClass = IRedPacketCoreClient.class)
    public void onGetWithdraw(WithdrawRedSucceedInfo succeedInfo) {
        if (null != succeedInfo) {
            Double packetNum = succeedInfo.getPacketNum();
            packetNum = (double) Math.round(packetNum * 100) / 100;
            redbagBinding.tvPacketNum.setText(String.valueOf(packetNum));
        }

    }

    private void saveClipboard() {
        if (userInfo == null) {
            toast("复制用户ID失败");
            return;
        }
        ClipData clipData = ClipData.newPlainText("用户ID复制成功!", String.valueOf(userInfo.getErbanNo()));
        mClipboardManager.setPrimaryClip(clipData);
        toast("专属邀请码复制成功!");
    }

    @Override
    public void onSaved(String inviteCode) {
        if (userInfo != null) {
            getDialogManager().showProgressDialog(this);
            CoreManager.getCore(IUserCore.class).saveInviteCode(userInfo, inviteCode);
        }
    }

    private void showInviteCode() {
        if (userInfo != null && StringUtils.isNotEmpty(userInfo.getShareCode())) {
            redbagBinding.inviteCode.setText(userInfo.getShareCode());
            redbagBinding.inviteCode.setVisibility(View.VISIBLE);
            redbagBinding.inviteMsg.setVisibility(View.GONE);
        }
    }

    private void openWebView(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        CommonWebViewActivity.start(this, url);
    }

    @CoreEvent(coreClientClass = IUserClient.class)
    public void onSaveShareCode(UserInfo userInfo) {
        if (getDialogManager() != null) {
            getDialogManager().dismissDialog();
        }
        toast("保存邀请码成功");
        this.userInfo = userInfo;
        CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(userInfo.getUid(), true);
        showInviteCode();
    }

    @CoreEvent(coreClientClass = IUserClient.class)
    public void onSaveShareCodeFail(String error) {
        if (getDialogManager() != null) {
            getDialogManager().dismissDialog();
        }
        toast(error);
    }

    @CoreEvent(coreClientClass = IAuthClient.class)
    public void onIsphoneFail(String error) {
        BinderPhoneActivity.start(this);
    }

    @CoreEvent(coreClientClass = IAuthClient.class)
    public void onIsPhone() {
//        Log.e(TAG, "onIsPhone: " + nickName);
        Intent intent = new Intent(this, RedPacketWithdrawActivity.class);
        intent.putExtra("WxNickName", nickName);
        intent.putExtra("WxOpenid", openid);
        startActivity(intent);
    }

    @Override
    public void onSharePlatformClick(Platform platform) {
        WebViewInfo webViewInfo = new WebViewInfo();
        webViewInfo.setTitle(getString(R.string.share_h5_title));
        webViewInfo.setImgUrl(WebUrl.SHARE_DEFAULT_LOGO);
        webViewInfo.setDesc(getString(R.string.share_h5_desc));
        webViewInfo.setShowUrl(WebUrl.SHARE_DOWNLOAD);
        CoreManager.getCore(IShareCore.class).shareH5(webViewInfo, platform);
    }
}
