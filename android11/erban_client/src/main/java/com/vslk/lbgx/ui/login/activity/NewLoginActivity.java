package com.vslk.lbgx.ui.login.activity;

import android.Manifest;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ScreenUtils;
import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.tongdaxing.xchat_core.login.presenter.LoginPresenter;
import com.vslk.lbgx.ui.MainActivity;
import com.vslk.lbgx.ui.home.view.NoScrollViewPager;
import com.vslk.lbgx.ui.login.fragment.LoginOptionFragment;
import com.vslk.lbgx.ui.login.fragment.PhoneLoginFragment;
import com.vslk.lbgx.ui.login.fragment.RegisterFragment;
import com.vslk.lbgx.ui.login.fragment.ResetPwFragment;
import com.tongdaxing.xchat_core.login.view.ILoginView;
import com.vslk.lbgx.ui.web.CommonWebViewActivity;
import com.vslk.lbgx.utils.JPushHelper;
import com.vslk.lbgx.utils.UIHelper;
import com.netease.nis.captcha.CaptchaListener;
import com.opensource.svgaplayer.SVGAImageView;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.xchat_core.WebUrl;
import com.tongdaxing.xchat_core.auth.AccountInfo;
import com.tongdaxing.xchat_core.auth.IAuthClient;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.coremanager.IAppInfoCore;
import com.tongdaxing.xchat_framework.util.SvgaUtils;
import com.tongdaxing.xchat_framework.util.config.BasicConfig;
import com.tongdaxing.xchat_framework.util.config.SpEvent;
import com.tongdaxing.xchat_framework.util.util.SpUtils;

import java.util.ArrayList;
import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Function: 新登录UI
 * Author: Edward on 2019/6/4
 */
@CreatePresenter(LoginPresenter.class)
public class NewLoginActivity extends BaseMvpActivity<ILoginView, LoginPresenter> implements ILoginView, View.OnClickListener {
    //    @BindView(R.id.iv_login_logo)
//    ImageView ivLoginLogo;
    @BindView(R.id.tv_user_protocol)
    TextView tvUserProtocol;
    @BindView(R.id.tv_self_protocol)
    TextView tvSelfProtocol;
    @BindView(R.id.tv_live_protocol)
    TextView tvLiveProtocol;
    @BindView(R.id.cb)
    CheckBox cb;

    public static String agreeStr = "agreement";

    /**
     * 基本权限管理
     */
    private final String[] BASIC_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO,
    };
    @BindView(R.id.vp_container)
    NoScrollViewPager vpContainer;
    private ArrayList<Fragment> fragments;

    public static void start(Context context) {
        Intent intent = new Intent(context, NewLoginActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_login);
        ButterKnife.bind(this);
        selectAgreement();
        tvUserProtocol.setOnClickListener(this);
        tvSelfProtocol.setOnClickListener(this);
        tvLiveProtocol.setOnClickListener(this);
        vpContainer.setNoScroll(true);
        fragments = new ArrayList<>();
//        fragments.add(new LoginOptionFragment());
        fragments.add(new PhoneLoginFragment());
        fragments.add(new RegisterFragment());
        fragments.add(new ResetPwFragment());
        vpContainer.setAdapter(new LoginAdapter(getSupportFragmentManager()));
    }

    private class LoginAdapter extends FragmentPagerAdapter {

        public LoginAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }


    private void switchPage(int position) {
        vpContainer.setCurrentItem(position, false);
    }


    private void selectAgreement() {
        boolean agreeBln = (boolean) SpUtils.get(this, agreeStr, false);
        if (!agreeBln) {
            agreementDialog();
        } else {
            permission();
        }
    }

    private Dialog mDialog;
    private AlertDialog.Builder mBuilder;

    public void agreementDialog() {
        mBuilder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        mDialog = mBuilder.create();

        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {

                return true;
            } else if (keyCode == KeyEvent.KEYCODE_MENU) {

                return true;
            }
            return false;
        });
        try {
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Window window = mDialog.getWindow();
        window.setContentView(R.layout.layout_agreement_dialog);
        window.setLayout((ScreenUtils.getScreenWidth() / 4 * 3), LinearLayout.LayoutParams.WRAP_CONTENT);

        window.findViewById(R.id.user_agree).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonWebViewActivity.start(getApplicationContext(), WebUrl.USER_AGREEMENT);
            }
        });

        window.findViewById(R.id.self_agree).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonWebViewActivity.start(getApplicationContext(), WebUrl.USER_POLICY);
            }
        });

        window.findViewById(R.id.btn_ok).setOnClickListener(v -> {
            mDialog.dismiss();
            permission();
            SpUtils.put(this, "agreement", true);

        });

        window.findViewById(R.id.btn_cancel).setOnClickListener(v -> {
            mDialog.dismiss();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(MainActivity.TAG_EXIT, true);
            startActivity(intent);
        });


    }


    private void permission() {
        checkPermission(() -> {
        }, R.string.ask_again, BASIC_PERMISSIONS);
    }

    /**
     * 网易图形验证码操作回调
     */
    private CaptchaListener myCaptchaListener = new CaptchaListener() {
        @Override
        public void onValidate(String result, String validate, String message) {
            captchaIsShowing = false;
            //验证结果，valiadte，可以根据返回的三个值进行用户自定义二次验证
            //验证成功
            if (validate.length() > 0) {
                nextStep();
            } else {
                toast("验证失败：" + message);
            }
        }

        @Override
        public void closeWindow() {
            //请求关闭页面
            captchaIsShowing = false;
        }

        @Override
        public void onError(String errormsg) {
            //出错
            captchaIsShowing = false;
        }

        @Override
        public void onCancel() {
            captchaIsShowing = false;
        }

        @Override
        public void onReady(boolean ret) {
            //该为调试接口，ret为true表示加载Sdk完成
        }

    };

    /**
     * 图形验证码验证成功后操作
     */
    private void nextStep() {
        switch (loginType) {
            case 1:
                getDialogManager().showProgressDialog(this, "请稍后");
                CoreManager.getCore(IAuthCore.class).wxLogin();
                break;
            case 2:
                getDialogManager().showProgressDialog(this, "请稍后");
                CoreManager.getCore(IAuthCore.class).qqLogin();
                break;
            case 3:
                if (checkAccountAndPw()) {
                    CoreManager.getCore(IAuthCore.class).login(account, pw);
                    getDialogManager().showProgressDialog(this, "正在登录...");
                }
                break;
            case R.id.btn_register:
                UIHelper.showRegisterAct(this);
                break;
            default:
                toast("请选择登录类型");
                break;
        }
    }

    private boolean pleaseAgree(boolean flag) {
        if (flag) {
            return true;
        } else {
            toast("请同意拉贝星球用户协议!");
            return false;
        }
    }

    private boolean checkAccountAndPw() {
        if (TextUtils.isEmpty(account)) {
            toast("手机号码不能为空!");
            return false;
        } else if (TextUtils.isEmpty(pw)) {
            toast("密码不能为空!");
            return false;
        } else if (pw.length() < 6 || pw.length() > 20) {
            toast("请输入6~20位密码");
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        getDialogManager().dismissDialog();
        super.onDestroy();
    }

    @CoreEvent(coreClientClass = IAuthClient.class)
    public void onLogout() {
        getDialogManager().dismissDialog();
    }

    @CoreEvent(coreClientClass = IAuthClient.class)
    public void onLoginFail(String error) {
        getDialogManager().dismissDialog();
//        toast(error);
        Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        captchaIsShowing = false;
    }

    @CoreEvent(coreClientClass = IAuthClient.class)
    public void onLogin(AccountInfo accountInfo) {
        CoreManager.getCore(IAppInfoCore.class).checkBanned(true);
        long uid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
        //这个接口是覆盖逻辑，而不是增量逻辑。即新的调用会覆盖之前的设置 -- 所以不用考虑登陆需要删除之前的alias
        if (uid > 0) {
            JPushHelper.getInstance().onAliasAction(this, uid + "", JPushHelper.ACTION_SET_ALIAS);
        }
        getDialogManager().dismissDialog();
        finish();
//        checkUserHasPhone(accountInfo.getUid());
    }


    @Override
    public void openPhoneLoginPage() {
        switchPage(0);
//            zoomOutView();
    }

    /**
     * 缩小logo
     */
    /*private void zoomOutView() {
        Keyframe ks1 = Keyframe.ofFloat(0.2f, 0.9f);
        Keyframe ks2 = Keyframe.ofFloat(0.4f, 0.8f);
        Keyframe ks3 = Keyframe.ofFloat(0.6f, 0.7f);
        Keyframe ks4 = Keyframe.ofFloat(0.8f, 0.6f);
        Keyframe ks5 = Keyframe.ofFloat(1f, 0.5f);
        PropertyValuesHolder p2 = PropertyValuesHolder.ofKeyframe("scaleX", ks1, ks2, ks3, ks4, ks5);
        PropertyValuesHolder p3 = PropertyValuesHolder.ofKeyframe("scaleY", ks1, ks2, ks3, ks4, ks5);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(ivLoginLogo, p2, p3);
        objectAnimator.setDuration(400);
        objectAnimator.start();
    }*/
    @Override
    public void openLoginPage() {
        switchPage(0);
    }

    @Override
    public void openRegisterPage() {
        switchPage(1);
    }

    @Override
    public void openResetPwPage() {
        switchPage(2);
    }

    @Override
    public boolean isAgreeCb() {
        return cb.isChecked();
    }

    @Override
    public void openBinderPhonePage() {
//        switchPage(4);
    }

    //避免重复点击多吃执行
    private boolean captchaIsShowing = false;

    /**
     * 10分钟内操作是否是要验证的
     */
    private boolean cleckLoginTimeNeedCaptcha() {
        long l = (Long) SpUtils.get(this, SpEvent.cleckLoginTime, 0L);
        long currentTimeMillis = System.currentTimeMillis();
        int i = 600000;
        if (BasicConfig.INSTANCE.isDebuggable()) {
            i = 60000;
        }
        if (currentTimeMillis - l > i) {
            SpUtils.put(this, SpEvent.cleckLoginTime, currentTimeMillis);
            return false;
        }
        return true;
    }

    /**
     * 登录类型1是微信登录，2是QQ登录，3是手机号码登录
     */
    private int loginType = -1;

    @Override
    public void onWechatLogin(boolean agreeUserProtocol) {
        if (pleaseAgree(agreeUserProtocol)) {
            loginType = 1;
            startLogin();
        }
    }

    @Override
    public void onQQLogin(boolean agreeUserProtocol) {
        if (pleaseAgree(agreeUserProtocol)) {
            loginType = 2;
            startLogin();
        }
    }

    private void startLogin() {
        if (captchaIsShowing) {
            return;
        }
        if (cleckLoginTimeNeedCaptcha()) {
            captchaIsShowing = true;
            nextStep();
        } else {
            nextStep();
        }
    }

    @Override
    public void openProtocolPage() {
        CommonWebViewActivity.start(this, WebUrl.USER_AGREEMENT);
    }

    @Override
    public void openSelfProtocolPage() {
        CommonWebViewActivity.start(this, WebUrl.USER_POLICY);
    }

    @Override
    public void openLiveProtocolPage() {
        CommonWebViewActivity.start(this, WebUrl.LIVE_RULE);
    }

    private String account;
    private String pw;

    @Override
    public void onPhoneLogin(String account, String pw, boolean agreeUserProtocol) {
        if (pleaseAgree(agreeUserProtocol)) {
            this.account = account;
            this.pw = pw;
            loginType = 3;
            startLogin();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_user_protocol:
                openProtocolPage();
                break;
            case R.id.tv_self_protocol:
                openSelfProtocolPage();
                break;
            case R.id.tv_live_protocol:
                openLiveProtocolPage();
                break;
        }
    }

    public static final String LOGIN_SHOW_BINDER_PHONE_PAGE = "is_skip";

    public String getLoginShowBinderPhonePageKey() {
        return LOGIN_SHOW_BINDER_PHONE_PAGE + "_" + CoreManager.getCore(IAuthCore.class).getCurrentUid();
    }

    public void saveSkipBinderPhone() {
        SharedPreferences sharedPreferences = getSharedPreferences(getLoginShowBinderPhonePageKey(), Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(getLoginShowBinderPhonePageKey(), true);
        editor.apply();
        finish();
    }
}







