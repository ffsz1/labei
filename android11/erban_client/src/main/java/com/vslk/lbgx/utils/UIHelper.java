package com.vslk.lbgx.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import com.vslk.lbgx.room.audio.activity.AudioRecordActivity;
import com.vslk.lbgx.ui.login.activity.AddUserInfoActivity;
import com.vslk.lbgx.ui.login.activity.ForgetPswActivity;
import com.vslk.lbgx.ui.login.activity.RegisterActivity;
import com.vslk.lbgx.ui.me.setting.activity.SettingActivity;
import com.vslk.lbgx.ui.me.user.activity.ModifyInfoActivity;
import com.vslk.lbgx.ui.me.user.activity.UserInfoActivity;
import com.vslk.lbgx.ui.me.user.activity.UserInfoModifyActivity;
import com.vslk.lbgx.ui.me.user.activity.UserModifyPhotosActivity;
import com.vslk.lbgx.ui.me.wallet.activity.WalletActivity;
import com.vslk.lbgx.ui.web.CommonWebViewActivity;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.WebUrl;


/**
 * *************************************************************************
 *
 * @Version 1.0
 * @ClassName: UIHelper
 * @Description: 应用程序UI工具包：封装UI相关的一些操作
 * @Author zhouxiangfeng
 * @date 2013-8-6 下午1:39:11
 * **************************************************************************
 */
public class UIHelper {

    public static void showRegisterAct(Context mContext) {
        mContext.startActivity(new Intent(mContext, RegisterActivity.class));
    }

    public static void showSettingAct(Context mContext) {
        mContext.startActivity(new Intent(mContext, SettingActivity.class));
    }

    public static void showForgetPswAct(Context mContext) {
        mContext.startActivity(new Intent(mContext, ForgetPswActivity.class));
    }

    public static void showAddInfoAct(Context mContext) {
        Intent intent = new Intent(mContext, AddUserInfoActivity.class);
        mContext.startActivity(intent);
    }

    //修改用户资料
    public static void showUserInfoModifyAct(Context mContext, long userId) {
        Intent intent = new Intent(mContext, UserInfoModifyActivity.class);
        intent.putExtra("userId", userId);
        mContext.startActivity(intent);
    }

    //我的钱包
    public static void showWalletAct(Context mContext) {
        Intent intent = new Intent(mContext, WalletActivity.class);
        mContext.startActivity(intent);
    }

    //侧边栏===>帮助
    public static void showUsinghelp(Context mContext) {
        CommonWebViewActivity.start(mContext, WebUrl.HELP);

    }

    public static void showUserInfoAct(Context mContext, long userId) {
        Intent intent = new Intent(mContext, UserInfoActivity.class);
        intent.putExtra("userId", userId);
        mContext.startActivity(intent);
    }

    public static void showAudioRecordAct(Context mContext) {
        Intent intent = new Intent(mContext, AudioRecordActivity.class);
        mContext.startActivity(intent);
    }

    public static void showModifyInfoAct(Activity mActivity, int requestCode, String title) {
        Intent intent = new Intent(mActivity, ModifyInfoActivity.class);
        intent.putExtra("title", title);
        mActivity.startActivityForResult(intent, requestCode);
    }

    public static void showModifyPhotosAct(Activity mActivity, long userId) {
        Intent intent = new Intent(mActivity, UserModifyPhotosActivity.class);
        intent.putExtra("userId", userId);
        mActivity.startActivity(intent);
    }

    public static void showModifyPhotosAndPhotos(Activity mActivity, long userId, boolean isSelf) {
        Intent intent = new Intent(mActivity, UserModifyPhotosActivity.class);
        intent.putExtra("isSelf", isSelf);
        intent.putExtra("userId", userId);
        mActivity.startActivity(intent);
    }

    /**
     * 启动应用的设置
     */
    public static void startAppSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }

    public static void openContactUs(Context context) {
        CommonWebViewActivity.start(context, UriProvider.IM_SERVER_URL + "/front/client/index.html");
    }
}
